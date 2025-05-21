package net.heydel;

public class CsvColumnSatz {
    private int maschinenNr;
    private String bezeichnung;
    private double kostensatz;

    public CsvColumnSatz(int maschinenNr, String bezeichnung, double kostensatz) {
        this.maschinenNr = maschinenNr;
        this.bezeichnung = bezeichnung;
        this.kostensatz = kostensatz;
    }

    public int getMaschinenNr() {
        return maschinenNr;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public double getKostensatz() {
        return kostensatz;
    }
}
