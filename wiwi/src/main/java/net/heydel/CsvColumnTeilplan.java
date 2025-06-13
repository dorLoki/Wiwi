package net.heydel;

public class CsvColumnTeilplan {
    private int teil_id;
    private int teil_nr;
    private String knoten;
    private int anzahl;

    public CsvColumnTeilplan(int teil_id, int teil_nr, String knoten, int anzahl) {
        this.teil_id = teil_id;
        this.teil_nr = teil_nr;
        this.knoten = knoten;
        this.anzahl = anzahl;
    }

    public int getTeil_id() {
        return teil_id;
    }

    public int getTeil_nr() {
        return teil_nr;
    }

    public String getKnoten() {
        return knoten;
    }

    public int getAnzahl() {
        return anzahl;
    }
}
