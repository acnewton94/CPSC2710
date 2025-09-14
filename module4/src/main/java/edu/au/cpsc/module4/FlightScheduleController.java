package edu.au.cpsc.module4;

import edu.au.cpsc.module4.ScheduledFlight.Day;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.EnumSet;
import java.util.Set;

/*
 Project: Module 4 – Flight Designator App
 Author: Alex Newton
 Auburn Email: azn0100@auburn.edu
 Date: 2025-09-14
 Description: JavaFX controller for table/detail editor with CSV persistence.
*/

@SuppressWarnings("unused")
public class FlightScheduleController {

    @FXML private TableView<ScheduledFlight> table;
    @FXML private TableColumn<ScheduledFlight, String> colDesignator;
    @FXML private TableColumn<ScheduledFlight, String> colDepIdent;
    @FXML private TableColumn<ScheduledFlight, String> colArrIdent;
    @FXML private TableColumn<ScheduledFlight, String> colDepTime;
    @FXML private TableColumn<ScheduledFlight, String> colArrTime;
    @FXML private TableColumn<ScheduledFlight, String> colDays;

    @FXML private TextField tfDesignator;
    @FXML private TextField tfDepIdent;
    @FXML private TextField tfDepTime; // HH:mm
    @FXML private TextField tfArrIdent;
    @FXML private TextField tfArrTime; // HH:mm
    @FXML private ToggleButton tbM, tbT, tbW, tbR, tbF, tbU, tbS;

    @FXML private Button btnAddOrUpdate;
    @FXML private Button btnNew;
    @FXML private Button btnDelete;

    private final DateTimeFormatter HM = DateTimeFormatter.ofPattern("HH:mm");
    private final AirlineDatabase db = new AirlineDatabase();

    @FXML
    private void initialize() {
        try {
            AirlineDatabase loaded = AirlineDatabaseIO.loadFromDefault();
            db.getScheduledFlights().setAll(loaded.getScheduledFlights());
            log("Loaded " + db.getScheduledFlights().size() + " rows from airline-db.csv");
        } catch (IOException e) {
            log("No existing DB file. Starting empty.");
        }

        table.setItems(db.getScheduledFlights());

        colDesignator.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getFlightDesignator()));
        colDepIdent.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDepartureAirportIdent()));
        colArrIdent.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getArrivalAirportIdent()));
        colDepTime.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDepartureTime().format(HM)));
        colArrTime.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getArrivalTime().format(HM)));
        colDays.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().daysAsString()));

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, sel) -> {
            if (sel == null) {
                clearEditor();
                btnAddOrUpdate.setText("Add");
            } else {
                fillEditor(sel);
                btnAddOrUpdate.setText("Update");
                log("Loaded into editor: " + sel.getFlightDesignator() + " days=" + sel.daysAsString());
            }
        });

        btnDelete.disableProperty().bind(table.getSelectionModel().selectedItemProperty().isNull());
        btnAddOrUpdate.disableProperty().bind(
                Bindings.createBooleanBinding(() ->
                                tfDesignator.getText().trim().isEmpty()
                                        || tfDepIdent.getText().trim().isEmpty()
                                        || tfDepTime.getText().trim().isEmpty()
                                        || tfArrIdent.getText().trim().isEmpty()
                                        || tfArrTime.getText().trim().isEmpty(),
                        tfDesignator.textProperty(), tfDepIdent.textProperty(),
                        tfDepTime.textProperty(), tfArrIdent.textProperty(),
                        tfArrTime.textProperty()
                )
        );

        tfDesignator.setOnAction(e -> onAddOrUpdate());
        tfDepIdent.setOnAction(e -> onAddOrUpdate());
        tfDepTime.setOnAction(e -> onAddOrUpdate());
        tfArrIdent.setOnAction(e -> onAddOrUpdate());
        tfArrTime.setOnAction(e -> onAddOrUpdate());
    }

    @FXML
    private void onNew() {
        table.getSelectionModel().clearSelection();
        clearEditor();
        btnAddOrUpdate.setText("Add");
        log("Editor cleared (New clicked).");
    }

    @FXML
    private void onAddOrUpdate() {
        try {
            if (table.getSelectionModel().getSelectedItem() == null) {
                // ADD
                ScheduledFlight sf = buildFromEditor();
                db.addScheduledFlight(sf);
                saveNow();
                log("Added: " + formatRow(sf));
                clearEditor();
            } else {
                // UPDATE
                int idx = table.getSelectionModel().getSelectedIndex();
                ScheduledFlight sf = buildFromEditor();
                db.getScheduledFlights().set(idx, sf); // replace row
                saveNow();
                log("Updated row " + idx + " to: " + formatRow(sf));
                table.getSelectionModel().clearSelection();
                clearEditor();
            }
        } catch (IllegalArgumentException ex) {
            showError("Validation error", ex.getMessage());
            log("Validation error: " + ex.getMessage());
        } catch (Exception ex) {
            showError("Error", ex.toString());
            log("Error: " + ex);
        }
    }

    @FXML
    private void onDelete() {
        ScheduledFlight sel = table.getSelectionModel().getSelectedItem();
        if (sel != null) {
            db.removeScheduledFlight(sel);
            try {
                saveNow();
                log("Deleted: " + formatRow(sel));
            } catch (IOException e) {
                showError("Save failed", e.toString());
                log("Save failed: " + e);
            }
            table.getSelectionModel().clearSelection();
            clearEditor();
        }
    }

    // ===== Helpers =====

    private void fillEditor(ScheduledFlight sf) {
        tfDesignator.setText(sf.getFlightDesignator());
        tfDepIdent.setText(sf.getDepartureAirportIdent());
        tfDepTime.setText(sf.getDepartureTime().format(HM));
        tfArrIdent.setText(sf.getArrivalAirportIdent());
        tfArrTime.setText(sf.getArrivalTime().format(HM));

        Set<Day> d = sf.getDaysOfWeek();
        tbM.setSelected(d.contains(Day.M));
        tbT.setSelected(d.contains(Day.T));
        tbW.setSelected(d.contains(Day.W));
        tbR.setSelected(d.contains(Day.R));
        tbF.setSelected(d.contains(Day.F));
        tbU.setSelected(d.contains(Day.U));
        tbS.setSelected(d.contains(Day.S));
    }

    private void clearEditor() {
        tfDesignator.clear();
        tfDepIdent.clear();
        tfDepTime.clear();
        tfArrIdent.clear();
        tfArrTime.clear();
        tbM.setSelected(false);
        tbT.setSelected(false);
        tbW.setSelected(false);
        tbR.setSelected(false);
        tbF.setSelected(false);
        tbU.setSelected(false);
        tbS.setSelected(false);
    }

    private ScheduledFlight buildFromEditor() {
        String designator = tfDesignator.getText().trim();
        String depIdent   = tfDepIdent.getText().trim();
        LocalTime dep     = LocalTime.parse(tfDepTime.getText().trim(), HM);
        String arrIdent   = tfArrIdent.getText().trim();
        LocalTime arr     = LocalTime.parse(tfArrTime.getText().trim(), HM);

        EnumSet<Day> days = EnumSet.noneOf(Day.class);
        if (tbM.isSelected()) days.add(Day.M);
        if (tbT.isSelected()) days.add(Day.T);
        if (tbW.isSelected()) days.add(Day.W);
        if (tbR.isSelected()) days.add(Day.R);
        if (tbF.isSelected()) days.add(Day.F);
        if (tbU.isSelected()) days.add(Day.U); // Sunday
        if (tbS.isSelected()) days.add(Day.S); // Saturday

        ScheduledFlight sf = new ScheduledFlight();
        sf.setFlightDesignator(designator);
        sf.setDepartureAirportIdent(depIdent);
        sf.setDepartureTime(dep);
        sf.setArrivalAirportIdent(arrIdent);
        sf.setArrivalTime(arr);
        sf.setDaysOfWeek(days);
        return sf;
    }

    private void saveNow() throws IOException {
        AirlineDatabaseIO.saveToDefault(db);
    }

    private void showError(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        a.setHeaderText(title);
        a.showAndWait();
    }

    private void log(String msg) { System.out.println(msg); }

    private String formatRow(ScheduledFlight sf) {
        return sf.getFlightDesignator() + " " +
                sf.getDepartureAirportIdent() + "->" + sf.getArrivalAirportIdent() + " " +
                sf.getDepartureTime().format(HM) + "->" + sf.getArrivalTime().format(HM) +
                " " + sf.daysAsString();
    }
}
