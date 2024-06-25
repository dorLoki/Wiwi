package net.heydel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CsvColumnProperties {
    private final StringProperty maschine;
    private final StringProperty datum;
    private final StringProperty beginnDerFertigung;
    private final StringProperty endeDerFertigung;

    public CsvColumnProperties(String maschine, String datum, String beginn, String ende) {
        this.maschine = new SimpleStringProperty(maschine);
        this.datum = new SimpleStringProperty(datum);
        this.beginnDerFertigung = new SimpleStringProperty(beginn);
        this.endeDerFertigung = new SimpleStringProperty(ende);
    }

    public String getMaschine() {
        return maschine.get();
    }

    public StringProperty maschineProperty() {
        return maschine;
    }

    public String getDatum() {
        return datum.get();
    }

    public StringProperty datumProperty() {
        return datum;
    }

    public String getBeginnDerFertigung() {
        return beginnDerFertigung.get();
    }

    public StringProperty beginnDerFertigungProperty() {
        return beginnDerFertigung;
    }

    public String getEndeDerFertigung() {
        return endeDerFertigung.get();
    }

    public StringProperty endeDerFertigungProperty() {
        return endeDerFertigung;
    }
}
