package net.heydel;

import java.util.Date;

public class CsvColumn {
    private int maschine;
    private Date beginnDerFertigung;
    private Date endeDerFertigung;
    private int teilId;

    public CsvColumn(int maschine, Date beginn, Date ende, int teilId) {
        this.maschine = maschine;
        this.beginnDerFertigung = beginn;
        this.endeDerFertigung = ende;
        this.teilId = teilId;
    }

    public int getMaschine() {
        return maschine;
    }

    public Date getBeginnDerFertigung() {
        return beginnDerFertigung;
    }

    public Date getEndeDerFertigung() {
        return endeDerFertigung;
    }

    public int getTeilId() {
        return teilId;
    }

    @Override
    public String toString() {
        return "CsvColumn{" +
                "maschine='" + maschine + '\'' +
                ", beginnDerFertigung=" + beginnDerFertigung +
                ", endeDerFertigung=" + endeDerFertigung +
                '}';
    }
}