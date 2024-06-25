package net.heydel;

import java.util.Date;

public class CsvColumn {
    private int maschine;
    private Date beginnDerFertigung;
    private Date endeDerFertigung;

    public CsvColumn(int maschine, Date beginn, Date ende) {
        this.maschine = maschine;
        this.beginnDerFertigung = beginn;
        this.endeDerFertigung = ende;
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

    @Override
    public String toString() {
        return "CsvColumn{" +
                "maschine='" + maschine + '\'' +
                ", beginnDerFertigung=" + beginnDerFertigung +
                ", endeDerFertigung=" + endeDerFertigung +
                '}';
    }
}