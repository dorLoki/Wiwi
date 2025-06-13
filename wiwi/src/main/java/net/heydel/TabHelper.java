package net.heydel;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.text.SimpleDateFormat;
import java.util.List;

public class TabHelper {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

    public static void createTabWithTable(TabPane tabPane, String tabTitle, List<CsvColumn> data) {
        // Erstelle einen neuen Tab
        Tab tab = new Tab(tabTitle);

        // Erstelle die TableView und setze die Spalten
        TableView<CsvColumn> tableView = new TableView<>();
        tableView.setItems(javafx.collections.FXCollections.observableArrayList(data));

        // Spalten definieren
        TableColumn<CsvColumn, String> maschineColumn = new TableColumn<>("Maschine");
        maschineColumn.setCellValueFactory(new PropertyValueFactory<>("maschine"));

        TableColumn<CsvColumn, String> datumColumn = new TableColumn<>("Datum");
        datumColumn.setCellValueFactory(cellData -> {
            CsvColumn csvColumn = cellData.getValue();
            String date = dateFormat.format(csvColumn.getBeginnDerFertigung());
            return new javafx.beans.property.SimpleStringProperty(date);
        });

        TableColumn<CsvColumn, String> beginnColumn = new TableColumn<>("Beginn der Fertigung");
        beginnColumn.setCellValueFactory(cellData -> {
            CsvColumn csvColumn = cellData.getValue();
            String beginn = timeFormat.format(csvColumn.getBeginnDerFertigung());
            return new javafx.beans.property.SimpleStringProperty(beginn);
        });

        TableColumn<CsvColumn, String> endeColumn = new TableColumn<>("Ende der Fertigung");
        endeColumn.setCellValueFactory(cellData -> {
            CsvColumn csvColumn = cellData.getValue();
            String ende = timeFormat.format(csvColumn.getEndeDerFertigung());
            return new javafx.beans.property.SimpleStringProperty(ende);
        });

        TableColumn<CsvColumn, Integer> teilIdColumn = new TableColumn<>("Teil ID");
        teilIdColumn.setCellValueFactory(new PropertyValueFactory<>("teilId"));

        // Füge Spalten zur TableView hinzu
        tableView.getColumns().addAll(teilIdColumn, maschineColumn, datumColumn, beginnColumn, endeColumn);

        // Füge die TableView zum Tab hinzu
        tab.setContent(tableView);

        // Füge den neuen Tab zur TabPane hinzu
        tabPane.getTabs().add(tab);
    }

    public static void createTabWithTableSatz(TabPane tabPane, String tabTitle, List<CsvColumnSatz> data) {
        Tab tab = new Tab(tabTitle);
        TableView<CsvColumnSatz> tableView = new TableView<>();
        tableView.setItems(javafx.collections.FXCollections.observableArrayList(data));

        TableColumn<CsvColumnSatz, Integer> maschineNrColumn = new TableColumn<>("Maschinen-Nr.");
        maschineNrColumn.setCellValueFactory(new PropertyValueFactory<>("maschinenNr"));

        TableColumn<CsvColumnSatz, String> maschineBez = new TableColumn<>("Bezeichnung");
        maschineBez.setCellValueFactory(new PropertyValueFactory<>("bezeichnung"));

        TableColumn<CsvColumnSatz, Double> kostensatzColumn = new TableColumn<>("Kostensatz");
        kostensatzColumn.setCellValueFactory(new PropertyValueFactory<>("kostensatz"));

        tableView.getColumns().addAll(maschineNrColumn, maschineBez, kostensatzColumn);
        tab.setContent(tableView);
        tabPane.getTabs().add(tab);
    }

    public static void createTabWithArbeitsplan(TabPane tabPane, String tabTitle, List<CsvColumArbeitsplan> data) {
        Tab tab = new Tab(tabTitle);
        TableView<CsvColumArbeitsplan> tableView = new TableView<>();
        tableView.setItems(javafx.collections.FXCollections.observableArrayList(data));

        TableColumn<CsvColumArbeitsplan, Integer> teil_idColumn = new TableColumn<>("teil_id");
        teil_idColumn.setCellValueFactory(new PropertyValueFactory<>("teil_id"));

        TableColumn<CsvColumArbeitsplan, Integer> ag_nrColumn = new TableColumn<>("ag_nr");
        ag_nrColumn.setCellValueFactory(new PropertyValueFactory<>("ag_nr"));

        TableColumn<CsvColumArbeitsplan, Integer> maschineColumn = new TableColumn<>("maschine");
        maschineColumn.setCellValueFactory(new PropertyValueFactory<>("maschine"));

        TableColumn<CsvColumArbeitsplan, Integer> dauerColumn = new TableColumn<>("dauer(min)");
        dauerColumn.setCellValueFactory(new PropertyValueFactory<>("dauer"));

        tableView.getColumns().addAll(teil_idColumn, ag_nrColumn, maschineColumn, dauerColumn);
        tab.setContent(tableView);
        tabPane.getTabs().add(tab);
    }

    public static void createTabWithTeilPlan(TabPane tabPane, String tabTitle, List<CsvColumnTeilplan> data) {
        Tab tab = new Tab(tabTitle);
        TableView<CsvColumnTeilplan> tableView = new TableView<>();
        tableView.setItems(javafx.collections.FXCollections.observableArrayList(data));

        TableColumn<CsvColumnTeilplan, Integer> teil_idColumn = new TableColumn<>("teil_id");
        teil_idColumn.setCellValueFactory(new PropertyValueFactory<>("teil_id"));

        TableColumn<CsvColumnTeilplan, Integer> teil_nrColumn = new TableColumn<>("teil_nr");
        teil_nrColumn.setCellValueFactory(new PropertyValueFactory<>("teil_nr"));

        TableColumn<CsvColumnTeilplan, String> knotenColumn = new TableColumn<>("knoten");
        knotenColumn.setCellValueFactory(new PropertyValueFactory<>("knoten"));

        TableColumn<CsvColumnTeilplan, Integer> anzahColumn = new TableColumn<>("Anzahl");
        anzahColumn.setCellValueFactory(new PropertyValueFactory<>("Anzahl"));

        tableView.getColumns().addAll(teil_idColumn, teil_nrColumn, knotenColumn, anzahColumn);
        tab.setContent(tableView);
        tabPane.getTabs().add(tab);
    }

    public static void createTabWithResult(TabPane tabPane, List<CsvResult> data) {
        Tab tab = new Tab("Ergebnis");
        TableView<CsvResult> tableView = new TableView<>();
        tableView.setItems(javafx.collections.FXCollections.observableArrayList(data));
        TableColumn<CsvResult, Integer> maschineNrColumn = new TableColumn<>("Maschine");
        maschineNrColumn.setCellValueFactory(new PropertyValueFactory<>("maschine"));
        TableColumn<CsvResult, Double> datumColumn = new TableColumn<>("Gesamtkosten");
        datumColumn.setCellValueFactory(new PropertyValueFactory<>("gesamtkosten"));
        TableColumn<CsvResult, Double> beginnColumn = new TableColumn<>("Gesamtdauer");
        beginnColumn.setCellValueFactory(new PropertyValueFactory<>("gesamtdauer"));

        tableView.getColumns().addAll(maschineNrColumn, datumColumn, beginnColumn);
        tab.setContent(tableView);
        tabPane.getTabs().add(tab);
    }
}
