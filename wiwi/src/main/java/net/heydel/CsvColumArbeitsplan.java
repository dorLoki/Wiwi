package net.heydel;

public class CsvColumArbeitsplan {
    private int teil_id;
    private int ag_nr;
    private int maschine;
    private int dauer;

    public CsvColumArbeitsplan(int teil_id, int ag_nr, int maschine, int dauer){
        this.teil_id = teil_id;
        this.ag_nr = ag_nr;
        this.maschine = maschine;
        this.dauer = dauer;
    }

    public int getTeil_id(){
        return teil_id;
    }

    public int getAg_nr() {
        return ag_nr;
    }

    public int getMaschine() {
        return maschine;
    }

    public int getDauer() {
        return dauer;
    }
}
