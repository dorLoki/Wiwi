package net.heydel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CsvReader {
    private final File file;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

    public CsvReader(File file) {
        this.file = file;
    }

    public List<?> readCsv() {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            if ((line = br.readLine()) != null) {
                if (line.equals("Maschine;Datum;Beginn der Fertigung;Ende der Fertigung")) {
                    List<CsvColumn> records = new ArrayList<>();
                    while ((line = br.readLine()) != null) {
                        parseCsvLine(records, line);
                    }
                    return records;
                }
                if (line.equals("Nr;Bezeichnung;KS (€/h)")) {
                    List<CsvColumnSatz> records = new ArrayList<>();
                    while ((line = br.readLine()) != null) {
                        parseCsvSatz(records, line);
                    }
                    return records;
                }
                if (line.equals("teil_id;ag_nr;maschine;dauer (min)")) {
                    List<CsvColumArbeitsplan> records = new ArrayList<>();
                    while ((line = br.readLine()) != null) {
                        parseCsvArbeitsplan(records, line);
                    }
                    return records;
                }
                if (line.equals("teil_id;teil_nr;knoten;K_mat;K_fert;Anzahl;Mat")) {
                    List<CsvColumnTeilplan> records = new ArrayList<>();
                    while ((line = br.readLine()) != null) {
                        parseCsvTeilplan(records, line);
                    }
                    return records;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void parseCsvSatz(List<CsvColumnSatz> records, String line) {
        String[] values = line.split(";");
        if (values.length >= 3) {
            try {
                CsvColumnSatz column = new CsvColumnSatz(Integer.parseInt(values[0]), values[1],
                        Double.parseDouble(values[2]));
                records.add(column);
            } catch (NumberFormatException e) {
                System.err.println("Fehler beim Parsen der Daten: " + e.getMessage());
            }
        }
    }

    private void parseCsvLine(List<CsvColumn> records, String line) {
        String[] values = line.split(";");
        if (values.length >= 4) {
            try {
                Date datum = dateFormat.parse(values[1]);
                Date beginn = timeFormat.parse(values[2]);
                // add datum + beginn to get the correct date
                Date beginnDate = new Date(datum.getTime() + beginn.getTime());
                Date ende = timeFormat.parse(values[3]);
                // add datum + ende to get the correct date
                Date endeDate = new Date(datum.getTime() + ende.getTime());
                CsvColumn column = new CsvColumn(Integer.parseInt(values[0]), beginnDate, endeDate);
                records.add(column);
            } catch (ParseException e) {
                System.err.println("Fehler beim Parsen der Daten: " + e.getMessage());
            }
        }
    }

    private void parseCsvArbeitsplan(List<CsvColumArbeitsplan> records, String line) {
        String[] values = line.split(";");
        if (values.length == 4) {
            CsvColumArbeitsplan column = new CsvColumArbeitsplan(Integer.parseInt(values[0]),
                    Integer.parseInt(values[1]), Integer.parseInt(values[2]), Integer.parseInt(values[3]));
            records.add(column);

        }
    }

    private void parseCsvTeilplan(List<CsvColumnTeilplan> records, String line) {
        String[] values = line.split(";");
        if (values.length == 7) {
            CsvColumnTeilplan column = new CsvColumnTeilplan(
                    Integer.parseInt(values[0]),
                    Integer.parseInt(values[1]),
                    values[2],
                    Integer.parseInt(values[5]));
            records.add(column);

        }
    }
}
