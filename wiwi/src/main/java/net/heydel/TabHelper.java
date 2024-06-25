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

        // Füge Spalten zur TableView hinzu
        tableView.getColumns().addAll(maschineColumn, datumColumn, beginnColumn, endeColumn);

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
        TableColumn<CsvColumnSatz, Double> kostensatzColumn = new TableColumn<>("Kostensatz");
        kostensatzColumn.setCellValueFactory(new PropertyValueFactory<>("kostensatz"));
        tableView.getColumns().addAll(maschineNrColumn, kostensatzColumn);
        tab.setContent(tableView);
        tabPane.getTabs().add(tab);
    }
    public static void createTabWithResult(TabPane tabPane, List<CsvResult> data){
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
