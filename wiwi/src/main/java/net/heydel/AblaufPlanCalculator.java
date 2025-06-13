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
        return result;
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
            }
            // find arbeitsplan steps
            List<CsvColumArbeitsplan> arbeitsplan = arbeitsplanList.stream()
                    .filter(a -> a.getTeil_id() == csvColumnTeilplan.getTeil_id())
                    .sorted(Comparator.comparingInt(CsvColumArbeitsplan::getAg_nr))
                    .collect(Collectors.toList());
            for (CsvColumArbeitsplan csvColumArbeitsplan : arbeitsplan) {
                addArbeitToResult(csvColumArbeitsplan);
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
}
