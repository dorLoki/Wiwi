package net.heydel;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class AblaufPlanCalculator {
    private List<CsvColumArbeitsplan> arbeitsplanList;
    private List<CsvColumnTeilplan> teilplanList;
    private List<CsvColumn> result = new ArrayList<>();

    public AblaufPlanCalculator(List<CsvColumArbeitsplan> arbeitsplanList, List<CsvColumnTeilplan> teilplanList) {
        this.arbeitsplanList = arbeitsplanList;
        this.teilplanList = teilplanList;
    }

    public List<CsvColumn> calc() {
        calcAblauf();
        return result /*
                       * .stream()
                       * .sorted(Comparator
                       * .comparingInt(CsvColumn::getTeilId)
                       * .thenComparing(CsvColumn::getBeginnDerFertigung))
                       * .collect(Collectors.toList())
                       */;
    }

    private void calcAblauf() {
        for (CsvColumnTeilplan csvColumnTeilplan : teilplanList) {
            calcTeil(csvColumnTeilplan);
        }
    }

    private void calcTeil(CsvColumnTeilplan csvColumnTeilplan) {
        int anzahl = csvColumnTeilplan.getAnzahl();

        // calc needed sub nodes
        for (int i = 0; i < anzahl; i++) {
            // recursive if node is not end node
            if (!csvColumnTeilplan.isEndKnoten()) {
                int knoten = Integer.parseInt(csvColumnTeilplan.getKnoten());
                CsvColumnTeilplan innerNode = teilplanList.stream()
                        .filter(a -> a.getTeil_id() == knoten).findFirst()
                        .orElseThrow(
                                () -> new RuntimeException("Teilplan mit teil_id = " + knoten + " nicht gefunden."));
                calcTeil(innerNode);
                specialCase = true;
            }
            // find arbeitsplan steps
            List<CsvColumArbeitsplan> arbeitsplan = arbeitsplanList.stream()
                    .filter(a -> a.getTeil_id() == csvColumnTeilplan.getTeil_id())
                    .sorted(Comparator.comparingInt(CsvColumArbeitsplan::getAg_nr))
                    .collect(Collectors.toList());
            for (CsvColumArbeitsplan csvColumArbeitsplan : arbeitsplan) {
                addArbeitToResult2(csvColumArbeitsplan);
            }
        }
    }

    private void addArbeitToResult(CsvColumArbeitsplan csvColumArbeitsplan) {
        int time = csvColumArbeitsplan.getDauer(); // Dauer in Minuten
        int maschine = csvColumArbeitsplan.getMaschine();
        LocalDateTime currentTime = null;
        int teilId = csvColumArbeitsplan.getTeil_id();

        // Setze Startzeit, wenn Liste leer
        if (result.isEmpty()) {
            currentTime = LocalDate.of(2025, 1, 6).atTime(8, 0);
        } else {
            CsvColumn lastEntry = result.get(result.size() - 1);
            currentTime = toLocalDateTime(lastEntry.getEndeDerFertigung());
        }

        // end time of the day
        LocalTime arbeitsEnde = LocalTime.of(16, 0);

        // check if time is already after 16 pm
        if (currentTime.toLocalTime().isAfter(arbeitsEnde)) {
            currentTime = naechsterArbeitstag(currentTime.toLocalDate());
        }

        // check if the step can be completed today
        LocalDateTime endTime = currentTime.plusMinutes(time);
        if (endTime.toLocalTime().isAfter(arbeitsEnde)) {
            // begin tomorrow
            currentTime = naechsterArbeitstag(currentTime.toLocalDate());
            endTime = currentTime.plusMinutes(time);
        }

        CsvColumn eintrag = new CsvColumn(maschine, toDate(currentTime), toDate(endTime), teilId);
        result.add(eintrag);

        // Vorbereitung für nächsten Schritt
        currentTime = endTime;
    }

    private LocalDateTime naechsterArbeitstag(LocalDate datum) {
        LocalDate naechster = datum.plusDays(1);
        while (naechster.getDayOfWeek() == DayOfWeek.SATURDAY || naechster.getDayOfWeek() == DayOfWeek.SUNDAY) {
            naechster = naechster.plusDays(1);
        }
        return naechster.atTime(8, 0);
    }

    private Date toDate(LocalDateTime ldt) {
        return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
    }

    private LocalDateTime toLocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    LocalDateTime lastTime = LocalDate.of(2025, 1, 6).atTime(8, 0);
    boolean specialCase = false;

    private void addArbeitToResult2(CsvColumArbeitsplan csvColumArbeitsplan) {
        int time = csvColumArbeitsplan.getDauer();
        int maschine = csvColumArbeitsplan.getMaschine();
        LocalDateTime currentTime = null;
        int teilId = csvColumArbeitsplan.getTeil_id();

        boolean isFirstStep = csvColumArbeitsplan.getAg_nr() == 1;
        // sub node was processed so last time must be checked
        if (specialCase) {
            specialCase = false;
            isFirstStep = false;
        }
        if (isFirstStep) {
            currentTime = LocalDate.of(2025, 1, 6).atTime(8, 0);
        } else {
            currentTime = lastTime;
        }

        // filter for maschine
        List<CsvColumn> onlyGivenMaschine = result.stream()
                .filter(a -> a.getMaschine() == maschine)
                .sorted(Comparator.comparing(CsvColumn::getBeginnDerFertigung))
                .collect(Collectors.toList());

        // check special cases
        if (onlyGivenMaschine.size() == 0) {
            CsvColumn entry = new CsvColumn(
                    maschine,
                    toDate(currentTime),
                    toDate(currentTime.plusMinutes(time)),
                    teilId);
            result.add(entry);
            lastTime = currentTime.plusMinutes(time);
            return;
        }

        CsvColumn firstEntry = onlyGivenMaschine.get(0);
        LocalDateTime endTime = currentTime.plusMinutes(time);
        // there is a timespot between t0 and the first entry
        if (endTime.isBefore(toLocalDateTime(firstEntry.getBeginnDerFertigung()))) {
            CsvColumn entry = new CsvColumn(
                    maschine,
                    toDate(currentTime),
                    toDate(endTime),
                    teilId);
            result.add(entry);
            lastTime = endTime;
            return;
        }
        // else check for spots
        CsvColumn entry = onlyGivenMaschine.stream()
                .reduce((a, b) -> {
                    var a_end = toLocalDateTime(a.getEndeDerFertigung());
                    var b_begin = toLocalDateTime(b.getBeginnDerFertigung());
                    var newEnd = a_end.plusMinutes(time);
                    // check if time is after 16 pm
                    if (newEnd.toLocalTime().isAfter(LocalTime.of(16, 0))) {
                        // get next working day
                        newEnd = naechsterArbeitstag(newEnd.toLocalDate());
                        newEnd.plusMinutes(time);

                        if (newEnd.isBefore(b_begin)) {
                            return a; // time frame is big enough
                        }
                        return b; // time frame is to small
                    }
                    // check if process ends befor beginning of b
                    if (newEnd.isBefore(b_begin)) {
                        return a; // time frame is big enough
                    } else {
                        return b; // time frame is to small
                    }
                }).get();

        var entry_end = toLocalDateTime(entry.getEndeDerFertigung());
        var newEnd = entry_end.plusMinutes(time);
        if (newEnd.toLocalTime().isAfter(LocalTime.of(16, 0))) {
            entry_end = naechsterArbeitstag(newEnd.toLocalDate());
            newEnd = entry_end.plusMinutes(time);
        }
        CsvColumn entry2 = new CsvColumn(maschine, toDate(entry_end), toDate(newEnd), teilId);
        result.add(entry2);
        lastTime = newEnd;
    }
}
