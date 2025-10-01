/*
 Project: Module 4 – Flight Designator App
 Author: Alex Newton
 Auburn Email: azn0100@auburn.edu
 Date: 2025-09-14
 Description: JavaFX controller for table/detail editor with CSV persistence.
*/
package edu.au.cpsc.module6;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;


public class FlightScheduleController {

    @FXML private TableView<ScheduledFlight> table;
    @FXML private TableColumn<ScheduledFlight, String> colDesignator;
    @FXML private TableColumn<ScheduledFlight, String> colDepIdent;
    @FXML private TableColumn<ScheduledFlight, String> colArrIdent;
    @FXML private TableColumn<ScheduledFlight, String> colDepTime;
    @FXML private TableColumn<ScheduledFlight, String> colArrTime;
    @FXML private TableColumn<ScheduledFlight, String> colDays;

    // Editor fields (match FXML)
    @FXML private TextField tfDesignator;
    @FXML private TextField tfOrigin;       // departure ident
    @FXML private TextField tfDestination;  // arrival ident
    @FXML private TextField tfDepart;       // HH:mm
    @FXML private TextField tfArrive;       // HH:mm

    // Buttons (match FXML)
    @FXML private Button btnNew;
    @FXML private Button btnAdd;
    @FXML private Button btnDelete;
    @FXML private Button btnUpdate;

    // Model + DB
    private final UiModel model = new UiModel();
    private final AirlineDatabase db = new AirlineDatabase();

    @FXML
    private void initialize() {
        // Load DB (ignore if CSV not present yet)
        try {
            AirlineDatabase loaded = AirlineDatabaseIO.loadFromDefault();
            db.getScheduledFlights().setAll(loaded.getScheduledFlights());
            log("Loaded " + db.getScheduledFlights().size() + " flights from CSV.");
        } catch (IOException e) {
            log("No CSV found yet — starting with empty database.");
        }

        // Hook table rows
        table.setItems(db.getScheduledFlights());
        colDesignator.setCellValueFactory(r -> new ReadOnlyStringWrapper(r.getValue().getFlightDesignator()));
        colDepIdent.setCellValueFactory(r -> new ReadOnlyStringWrapper(r.getValue().getDepartureAirportIdent()));
        colArrIdent.setCellValueFactory(r -> new ReadOnlyStringWrapper(r.getValue().getArrivalAirportIdent()));
        colDepTime.setCellValueFactory(r -> new ReadOnlyStringWrapper(r.getValue().getDepartureTime().toString()));
        colArrTime.setCellValueFactory(r -> new ReadOnlyStringWrapper(r.getValue().getArrivalTime().toString()));
        colDays.setCellValueFactory(r -> new ReadOnlyStringWrapper(r.getValue().daysAsString()));

        // Two-way bind editor fields <-> UiModel
        tfDesignator.textProperty().bindBidirectional(model.designator);
        tfOrigin.textProperty().bindBidirectional(model.origin);
        tfDestination.textProperty().bindBidirectional(model.destination);
        tfDepart.textProperty().bindBidirectional(model.depart);
        tfArrive.textProperty().bindBidirectional(model.arrive);

        btnAdd.disableProperty().bind(model.addEnabled.not());
        btnUpdate.disableProperty().bind(model.updateEnabled.not());
        btnDelete.disableProperty().bind(model.deleteEnabled.not());
        btnNew.disableProperty().bind(model.existingSelected);

        tfDesignator.styleProperty().bind(model.designatorStyle);
        tfOrigin.styleProperty().bind(model.originStyle);
        tfDestination.styleProperty().bind(model.destinationStyle);
        tfDepart.styleProperty().bind(model.departStyle);
        tfArrive.styleProperty().bind(model.arriveStyle);

        // Selection ↔ model
        model.existingSelected.bind(table.getSelectionModel().selectedItemProperty().isNotNull());
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, sel) -> {
            if (sel == null) {
                model.snapshot();
            } else {
                model.designator.set(sel.getFlightDesignator());
                model.origin.set(sel.getDepartureAirportIdent());
                model.destination.set(sel.getArrivalAirportIdent());
                model.depart.set(sel.getDepartureTime().toString());
                model.arrive.set(sel.getArrivalTime().toString());
                model.snapshot();
            }
        });

        // Initial baseline for a clean "new" state
        model.snapshot();
    }

    @FXML
    private void onNew() {
        table.getSelectionModel().clearSelection();
        model.clear();     // clears fields and sets existingSelected=false
        model.snapshot();  // new baseline
        log("New: editor cleared.");
    }

    @FXML
    private void onAdd() {
        // UiModel guarantees validity for times & nonempty fields when enabled.
        ScheduledFlight sf = buildFromModel(null); // no existing row => no days to preserve
        db.getScheduledFlights().add(sf);
        selectLast();
        saveQuietly();
        model.snapshot();
        log("Added: " + rowString(sf));
    }

    @FXML
    private void onUpdate() {
        int idx = table.getSelectionModel().getSelectedIndex();
        if (idx < 0) return;
        ScheduledFlight existing = table.getItems().get(idx);
        ScheduledFlight updated = buildFromModel(existing); // preserves existing days
        db.getScheduledFlights().set(idx, updated);
        table.getSelectionModel().select(idx);
        saveQuietly();
        model.snapshot();
        log("Updated row " + idx + " -> " + rowString(updated));
    }

    @FXML
    private void onDelete() {
        int idx = table.getSelectionModel().getSelectedIndex();
        if (idx < 0) return;
        ScheduledFlight removed = table.getItems().remove(idx);
        saveQuietly();
        onNew();
        log("Deleted: " + rowString(removed));
    }

    private ScheduledFlight buildFromModel(ScheduledFlight base) {
        ScheduledFlight sf = new ScheduledFlight();
        sf.setFlightDesignator(model.designator.get().trim());
        sf.setDepartureAirportIdent(model.origin.get().trim());
        sf.setArrivalAirportIdent(model.destination.get().trim());
        sf.setDepartureTime(parse(model.depart.get()));
        sf.setArrivalTime(parse(model.arrive.get()));
        if (base != null) {
            sf.setDaysOfWeek(base.getDaysOfWeek()); // no day controls in FXML; keep existing
        }
        return sf;
    }

    private static LocalTime parse(String hhmm) {
        try {
            return LocalTime.parse(hhmm);
        } catch (DateTimeParseException ex) {
            // Should not happen when buttons are enabled, thanks to UiModel validation.
            return LocalTime.MIDNIGHT;
        }
    }

    private void saveQuietly() {
        try {
            AirlineDatabaseIO.saveToDefault(db);
        } catch (IOException e) {
            // non-fatal; log to console so you see it while developing
            System.err.println("[save] " + e);
        }
    }

    private void selectLast() {
        if (!table.getItems().isEmpty()) {
            table.getSelectionModel().select(table.getItems().size() - 1);
        }
    }

    private void log(String m) { System.out.println(m); }

    private String rowString(ScheduledFlight sf) {
        return sf.getFlightDesignator() + " " +
                sf.getDepartureAirportIdent() + "->" + sf.getArrivalAirportIdent() + " " +
                sf.getDepartureTime() + "->" + sf.getArrivalTime() + " " +
                sf.daysAsString();
    }
}
