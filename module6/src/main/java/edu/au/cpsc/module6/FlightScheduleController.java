package edu.au.cpsc.module6;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

public class FlightScheduleController {

    // UI elements (must match fx:id in FXML)
    @FXML private TextField designatorField;
    @FXML private TextField originField;
    @FXML private TextField destinationField;
    @FXML private TextField departField;
    @FXML private TextField arriveField;

    @FXML private Button newButton;
    @FXML private Button addButton;
    @FXML private Button deleteButton;
    @FXML private Button updateButton;

    @FXML private TableView<ScheduledFlight> table; // if present in your FXML

    // Your existing data helpers (stub types — keep your implementations)
    private final AirlineDatabase db = new AirlineDatabase(); // or inject via setter
    private final UiModel model = new UiModel();

    @FXML
    public void initialize() {
        // Field <-> model bindings
        designatorField.textProperty().bindBidirectional(model.designatorProperty());
        originField.textProperty().bindBidirectional(model.originProperty());
        destinationField.textProperty().bindBidirectional(model.destinationProperty());
        departField.textProperty().bindBidirectional(model.departTimeProperty());
        arriveField.textProperty().bindBidirectional(model.arriveTimeProperty());

        // Button enable/disable via model state
        newButton.disableProperty().bind(model.newButtonEnabled.not());
        addButton.disableProperty().bind(model.addButtonEnabled.not());
        deleteButton.disableProperty().bind(model.deleteButtonEnabled.not());
        updateButton.disableProperty().bind(model.updateButtonEnabled.not());

        // Visual validity cue on designator (optional)
        designatorField.styleProperty().bind(
                Bindings.when(model.inputValid)
                        .then("")
                        .otherwise("-fx-border-color: red; -fx-border-width: 1;"));

        // Mark modified on edits (only matters in existing state)
        model.designatorProperty().addListener((o,a,b)->model.markModified());
        model.originProperty().addListener((o,a,b)->model.markModified());
        model.destinationProperty().addListener((o,a,b)->model.markModified());
        model.departTimeProperty().addListener((o,a,b)->model.markModified());
        model.arriveTimeProperty().addListener((o,a,b)->model.markModified());

        // If you populate a TableView, set a selection listener:
        if (table != null) {
            table.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, sel) -> {
                if (sel != null) {
                    model.designatorProperty().set(sel.getDesignator());
                    model.originProperty().set(sel.getOrigin());
                    model.destinationProperty().set(sel.getDestination());
                    model.departTimeProperty().set(sel.getDepartureTime().toString());
                    model.arriveTimeProperty().set(sel.getArrivalTime().toString());
                    model.markExisting();
                }
            });
        }

        // Start in "new" mode
        model.markNew();
    }

    // Button actions (wire with onAction in FXML or fx:id + #method names)
    @FXML private void onNew() {
        model.markNew();
        designatorField.clear();
        originField.clear();
        destinationField.clear();
        departField.clear();
        arriveField.clear();
        if (table != null) table.getSelectionModel().clearSelection();
    }

    @FXML private void onAdd() {
        // Create & save a new flight (replace with your db/repo code)
        ScheduledFlight sf = ScheduledFlight.fromStrings(
                model.designatorProperty().get(),
                model.originProperty().get(),
                model.destinationProperty().get(),
                model.departTimeProperty().get(),
                model.arriveTimeProperty().get()
        );
        db.add(sf);
        if (table != null) {
            table.getItems().add(sf);
            table.getSelectionModel().select(sf);
        }
        model.markExisting();
    }

    @FXML private void onUpdate() {
        // Update the selected flight in your DB (you likely have IDs/indices)
        if (table != null) {
            ScheduledFlight sel = table.getSelectionModel().getSelectedItem();
            if (sel != null) {
                sel.updateFromStrings(
                        model.designatorProperty().get(),
                        model.originProperty().get(),
                        model.destinationProperty().get(),
                        model.departTimeProperty().get(),
                        model.arriveTimeProperty().get()
                );
                table.refresh();
            }
        }
        db.save(); // stub; implement as needed
        model.markExisting();
    }

    @FXML private void onDelete() {
        if (table != null) {
            ScheduledFlight sel = table.getSelectionModel().getSelectedItem();
            if (sel != null) {
                table.getItems().remove(sel);
                db.remove(sel);
            }
        }
        onNew();
    }
}
