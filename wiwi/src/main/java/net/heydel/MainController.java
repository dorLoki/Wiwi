package net.heydel;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.IntStream;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MainController {

    @FXML
    private MenuItem menuOpen;
    @FXML
    private MenuItem menuClose;

    @FXML
    private MenuItem menuQuit;
    @FXML
    private MenuItem menuCalcFile;

    @FXML
    private TabPane tabPane;

    private Stage primaryStage;

    private HashMap<String, List<CsvColumn>> csvColumns = new HashMap<>();
    private HashMap<String, List<CsvColumnSatz>> csvColumnSaetze = new HashMap<>();

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    private void doMenuOpen() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("CSV-Datei öffnen");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        var file = fileChooser.showOpenDialog(primaryStage);
        if (file != null) {
            CsvReader reader = new CsvReader(file);
            List<?> test = reader.readCsv();
            if (test != null && !test.isEmpty()) {
                // check if the file is a CsvColumn or CsvColumnSatz
                if (test.get(0) instanceof CsvColumn) {
                    // create a new tab with the content of the file CsvColumn
                    TabHelper.createTabWithTable(tabPane, file.getName(), (List<CsvColumn>) test);
                    csvColumns.put(file.getName(), (List<CsvColumn>) test);
                } else if (test.get(0) instanceof CsvColumnSatz) {
                    // create a new tab with the content of the file
                    TabHelper.createTabWithTableSatz(tabPane, file.getName(), (List<CsvColumnSatz>) test);
                    csvColumnSaetze.put(file.getName(), (List<CsvColumnSatz>) test);
                }
            } else {
                // show error message
                // TODO
            }
        }
    }

    @FXML
    private void doMenuClose() {
        // Schließt den aktuellen Tab
        int currentIndex = tabPane.getSelectionModel().getSelectedIndex();
        if (currentIndex != -1) {
            String name = tabPane.getTabs().get(currentIndex).getText();
            tabPane.getTabs().remove(currentIndex);
            csvColumns.remove(name);
            csvColumnSaetze.remove(name);
        }
    }

    @FXML
    private void doQuit() {
        System.exit(0);
    }

    @FXML
    public void doCalcFile() {
        // Dialogfenster erstellen
        Dialog<HashMap<String, Object>> dialog = new Dialog<>();
        dialog.setTitle("Berechnung konfigurieren");

        // GridPane für Layout
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        // Auswahl der Sätze Datei
        Label labelSaetze = new Label("Sätze-Datei:");
        ComboBox<String> comboSaetze = new ComboBox<>();
        comboSaetze.getItems().addAll(csvColumnSaetze.keySet());
        grid.add(labelSaetze, 0, 0);
        grid.add(comboSaetze, 1, 0);

        // Auswahl der Ist-Daten Datei
        Label labelIstDaten = new Label("Ist-Daten Datei:");
        ComboBox<String> comboIstDaten = new ComboBox<>();
        comboIstDaten.getItems().addAll(csvColumns.keySet());
        grid.add(labelIstDaten, 0, 1);
        grid.add(comboIstDaten, 1, 1);

        // Zeitraumauswahl
        Label labelZeitraum = new Label("Zeitraum:");
        ComboBox<String> comboZeitraum = new ComboBox<>();
        comboZeitraum.getItems().addAll("Jahr", "Quartal", "Monat", "Individuell");
        grid.add(labelZeitraum, 0, 2);
        grid.add(comboZeitraum, 1, 2);

        // Container für zusätzliche Zeitraumauswahl
        HBox timeSelectionBox = new HBox(10);
        ComboBox<Integer> yearComboBox = new ComboBox<>();
        IntStream.rangeClosed(2014, 2024).forEach(yearComboBox.getItems()::add);
        ComboBox<String> quarterComboBox = new ComboBox<>();
        quarterComboBox.getItems().addAll("1. Quartal", "2. Quartal", "3. Quartal", "4. Quartal");
        ComboBox<String> monthComboBox = new ComboBox<>();
        monthComboBox.getItems().addAll("Januar", "Februar", "März", "April", "Mai", "Juni", "Juli", "August",
                "September", "Oktober", "November", "Dezember");
        DatePicker startDatePicker = new DatePicker(LocalDate.now());
        DatePicker endDatePicker = new DatePicker(LocalDate.now());

        // Standardmäßig unsichtbar setzen
        yearComboBox.setVisible(false);
        quarterComboBox.setVisible(false);
        monthComboBox.setVisible(false);
        startDatePicker.setVisible(false);
        endDatePicker.setVisible(false);

        // Listener für ComboBox-Auswahl
        comboZeitraum.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            yearComboBox.setVisible(!"Individuell".equals(newValue));
            quarterComboBox.setVisible("Quartal".equals(newValue));
            monthComboBox.setVisible("Monat".equals(newValue));
            startDatePicker.setVisible("Individuell".equals(newValue));
            endDatePicker.setVisible("Individuell".equals(newValue));
            timeSelectionBox.getChildren().clear();
            if (newValue != null) {
                switch (newValue) {
                    case "Jahr":
                        timeSelectionBox.getChildren().add(yearComboBox);
                        break;
                    case "Quartal":
                        timeSelectionBox.getChildren().addAll(yearComboBox, quarterComboBox);
                        break;
                    case "Monat":
                        timeSelectionBox.getChildren().addAll(yearComboBox, monthComboBox);
                        break;
                    case "Individuell":
                        timeSelectionBox.getChildren().addAll(startDatePicker, endDatePicker);
                        break;
                }
            }
        });

        grid.add(timeSelectionBox, 1, 3);

        // Button zum Starten der Berechnung
        Button calculateButton = new Button("Berechnen");
        calculateButton.setOnAction(event -> {
            // Berechnung starten, Ergebnisse verarbeiten
            dialog.setResult(convertSelectionToMap(comboZeitraum, comboSaetze, comboIstDaten, yearComboBox,
                    quarterComboBox, monthComboBox, startDatePicker, endDatePicker));
            dialog.close();
        });
        grid.add(calculateButton, 1, 4);

        dialog.getDialogPane().setContent(grid);
        dialog.showAndWait().ifPresent(result -> {
            // Ergebnis verarbeiten
            System.out.println("Ausgewählte Konfiguration: " + result);
            calc(result);
        });
    }

    private HashMap<String, Object> convertSelectionToMap(ComboBox<String> comboZeitraum, ComboBox<String> comboSaetze,
            ComboBox<String> comboIstDaten, ComboBox<Integer> yearComboBox, ComboBox<String> quarterComboBox,
            ComboBox<String> monthComboBox, DatePicker startDatePicker, DatePicker endDatePicker) {
        HashMap<String, Object> result = new HashMap<>();
        result.put("Zeitraum", comboZeitraum.getSelectionModel().getSelectedItem());
        result.put("SaetzeFile", comboSaetze.getSelectionModel().getSelectedItem());
        result.put("IstDatenFile", comboIstDaten.getSelectionModel().getSelectedItem());
        result.put("Year", yearComboBox.getSelectionModel().getSelectedItem());
        result.put("Quarter", quarterComboBox.getSelectionModel().getSelectedItem());
        result.put("Month", monthComboBox.getSelectionModel().getSelectedItem());
        result.put("StartDate", startDatePicker.getValue());
        result.put("EndDate", endDatePicker.getValue());
        return result;
    }

    private void calc(HashMap<String, Object> result) {
        // Berechnung durchführen
        Date startDate = null;
        Date endDate = null;
        switch ((String) result.get("Zeitraum")) {
            case "Jahr":
                int i = (int) result.get("Year");
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, i);
                cal.set(Calendar.DAY_OF_YEAR, 1);
                startDate = cal.getTime();
                cal.set(Calendar.DAY_OF_YEAR, cal.getActualMaximum(Calendar.DAY_OF_YEAR));
                endDate = cal.getTime();
                break;
            case "Quartal":
                int year = (int) result.get("Year");
                switch ((String) result.get("Quarter")) {
                    case "1. Quartal":
                        Calendar cal1 = Calendar.getInstance();
                        cal1.set(Calendar.YEAR, year);
                        cal1.set(Calendar.MONTH, Calendar.JANUARY);
                        cal1.set(Calendar.DAY_OF_MONTH, 1);
                        startDate = cal1.getTime();
                        cal1.set(Calendar.MONTH, Calendar.MARCH);
                        cal1.set(Calendar.DAY_OF_MONTH, cal1.getActualMaximum(Calendar.DAY_OF_MONTH));
                        endDate = cal1.getTime();
                        break;
                    case "2. Quartal":
                        Calendar cal2 = Calendar.getInstance();
                        cal2.set(Calendar.YEAR, year);
                        cal2.set(Calendar.MONTH, Calendar.APRIL);
                        cal2.set(Calendar.DAY_OF_MONTH, 1);
                        startDate = cal2.getTime();
                        cal2.set(Calendar.MONTH, Calendar.JUNE);
                        cal2.set(Calendar.DAY_OF_MONTH, cal2.getActualMaximum(Calendar.DAY_OF_MONTH));
                        endDate = cal2.getTime();
                        break;
                    case "3. Quartal":
                        Calendar cal3 = Calendar.getInstance();
                        cal3.set(Calendar.YEAR, year);
                        cal3.set(Calendar.MONTH, Calendar.JULY);
                        cal3.set(Calendar.DAY_OF_MONTH, 1);
                        startDate = cal3.getTime();
                        cal3.set(Calendar.MONTH, Calendar.SEPTEMBER);
                        cal3.set(Calendar.DAY_OF_MONTH, cal3.getActualMaximum(Calendar.DAY_OF_MONTH));
                        endDate = cal3.getTime();
                        break;
                    case "4. Quartal":
                        Calendar cal4 = Calendar.getInstance();
                        cal4.set(Calendar.YEAR, year);
                        cal4.set(Calendar.MONTH, Calendar.OCTOBER);
                        cal4.set(Calendar.DAY_OF_MONTH, 1);
                        startDate = cal4.getTime();
                        cal4.set(Calendar.MONTH, Calendar.DECEMBER);
                        cal4.set(Calendar.DAY_OF_MONTH, cal4.getActualMaximum(Calendar.DAY_OF_MONTH));
                        endDate = cal4.getTime();
                        break;
                }
                break;
            case "Monat":
                int year2 = (int) result.get("Year");
                int month = 0;
                switch ((String) result.get("Month")) {
                    case "Januar":
                        month = Calendar.JANUARY;
                        break;
                    case "Februar":
                        month = Calendar.FEBRUARY;
                        break;
                    case "März":
                        month = Calendar.MARCH;
                        break;
                    case "April":
                        month = Calendar.APRIL;
                        break;
                    case "Mai":
                        month = Calendar.MAY;
                        break;
                    case "Juni":
                        month = Calendar.JUNE;
                        break;
                    case "Juli":
                        month = Calendar.JULY;
                        break;
                    case "August":
                        month = Calendar.AUGUST;
                        break;
                    case "September":
                        month = Calendar.SEPTEMBER;
                        break;
                    case "Oktober":
                        month = Calendar.OCTOBER;
                        break;
                    case "November":
                        month = Calendar.NOVEMBER;
                        break;
                    case "Dezember":
                        month = Calendar.DECEMBER;
                        break;
                }
                Calendar cal5 = Calendar.getInstance();
                cal5 = Calendar.getInstance();
                cal5.set(Calendar.YEAR, year2);
                cal5.set(Calendar.MONTH, month);
                cal5.set(Calendar.DAY_OF_MONTH, 1);
                startDate = cal5.getTime();
                cal5.set(Calendar.DAY_OF_MONTH, cal5.getActualMaximum(Calendar.DAY_OF_MONTH));
                endDate = cal5.getTime();
                break;
            case "Individuell":
                LocalDate start = (LocalDate) result.get("StartDate");
                LocalDate end = (LocalDate) result.get("EndDate");

                startDate = Date.from(start.atStartOfDay(ZoneId.systemDefault()).toInstant());
                endDate = Date.from(end.atStartOfDay(ZoneId.systemDefault()).toInstant());
                break;
        }
        String saetzeFile = (String) result.get("SaetzeFile");
        String istDatenFile = (String) result.get("IstDatenFile");
        calculate(startDate, endDate, csvColumnSaetze.get(saetzeFile), csvColumns.get(istDatenFile));
    }

    private void calculate(Date startDate, Date endDate, List<CsvColumnSatz> saetze, List<CsvColumn> istDaten) {
        // check null
        if (startDate == null || endDate == null || saetze == null || istDaten == null) {
            // TODO error message
            return;
        }
        // map für saetze
        HashMap<Integer, Double> saetzeMap = new HashMap<>();
        saetze.forEach(s -> saetzeMap.put(s.getMaschinenNr(), s.getKostensatz()));
        // istDaten filtern

        List<CsvColumn> filteredIstDaten = new ArrayList<>();
        for (var i : istDaten) {
            if (i.getBeginnDerFertigung().after(startDate) && i.getEndeDerFertigung().before(endDate)) {
                filteredIstDaten.add(i);
            }
        }
        List<CsvResult> results = new ArrayList<>();
        // berechnung
        for (var i : saetzeMap.keySet()) {
            int maschineNr = i;
            double gesamtdauer = 0;
            double kostensatz = saetzeMap.get(maschineNr);
            for (var j : filteredIstDaten) {
                if (j.getMaschine() == maschineNr) {
                    gesamtdauer += j.getEndeDerFertigung().getTime() - j.getBeginnDerFertigung().getTime();
                }
            }
            double gesamtkosten = gesamtdauer * kostensatz / 1000 / 60 / 60;
            // round to 2 decimal places
            gesamtkosten = Math.round(gesamtkosten * 100.0) / 100.0;
            double gesamtdauerStunden = (double) (gesamtdauer / 1000 / 60 / 60);
            gesamtdauerStunden = Math.round(gesamtdauerStunden * 100.0) / 100.0;
            CsvResult res = new CsvResult(maschineNr, gesamtkosten, gesamtdauerStunden);
            results.add(res);
        }
        TabHelper.createTabWithResult(tabPane, results);
    }
}