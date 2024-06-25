package net.heydel;

public class CsvResult {
    private int maschine;
    private double gesamtkosten;
    private double gesamtdauer;

    public CsvResult(int maschine, double gesamtkosten, double gesamtdauer) {
        this.maschine = maschine;
        this.gesamtkosten = gesamtkosten;
        this.gesamtdauer = gesamtdauer;
    }

    public int getMaschine() {
        return maschine;
    }

    public double getGesamtkosten() {
        return gesamtkosten;
    }

    public double getGesamtdauer() {
        return gesamtdauer;
    }
}
