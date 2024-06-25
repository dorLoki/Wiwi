package net.heydel;

public class CsvColumnSatz {
    private int maschinenNr;
    private double kostensatz;

    public CsvColumnSatz(int maschinenNr, double kostensatz) {
        this.maschinenNr = maschinenNr;
        this.kostensatz = kostensatz;
    }

    public int getMaschinenNr() {
        return maschinenNr;
    }

    public double getKostensatz() {
        return kostensatz;
    }
}
