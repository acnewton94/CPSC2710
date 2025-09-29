package edu.au.cpsc.module6;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

public class FlightScheduleController {

    @FXML
    private TextField designatorField, originField, destinationField, departField, arriveField;
    @FXML
    private Button newButton, addButton, deleteButton; // no separate updateButton
    @FXML
    private TableView<ScheduledFlight> table;          // if you use one
    private final UiModel model = new UiModel();
    private final AirlineDatabase db = new AirlineDatabase();
    private static final String ERROR_BG = "-fx-background-color: #ffe6e6;";

    public void initialize() {
        designatorField.textProperty().bindBidirectional(model.designatorProperty());
        originField.textProperty().bindBidirectional(model.originProperty());
        destinationField.textProperty().bindBidirectional(model.destinationProperty());
        departField.textProperty().bindBidirectional(model.departTimeProperty());
        arriveField.textProperty().bindBidirectional(model.arriveTimeProperty());

        newButton.disableProperty().bind(model.newButtonEnabled.not());
        deleteButton.disableProperty().bind(model.deleteButtonEnabled.not());

        addButton.textProperty().bind(
                Bindings.when(model.newStateProperty()).then("Add").otherwise("Update"));
        addButton.disableProperty().bind(
                Bindings.when(model.newStateProperty())
                        .then(model.addButtonEnabled.not())
                        .otherwise(model.updateButtonEnabled.not())
        );

        designatorField.styleProperty().bind(
                Bindings.when(model.designatorValid).then("").otherwise(ERROR_BG));
        originField.styleProperty().bind(
                Bindings.when(model.originNonEmpty).then("").otherwise(ERROR_BG));
        destinationField.styleProperty().bind(
                Bindings.when(model.destinationNonEmpty).then("").otherwise(ERROR_BG));
        departField.styleProperty().bind(
                Bindings.when(model.departValid).then("").otherwise(ERROR_BG));
        arriveField.styleProperty().bind(
                Bindings.when(model.arriveValid).then("").otherwise(ERROR_BG));

        model.designatorProperty().addListener((o, a, b) -> model.markModified());
        model.originProperty().addListener((o, a, b) -> model.markModified());
        model.destinationProperty().addListener((o, a, b) -> model.markModified());
        model.departTimeProperty().addListener((o, a, b) -> model.markModified());
        model.arriveTimeProperty().addListener((o, a, b) -> model.markModified());

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

    @FXML
    private void onNew() {
        model.markNew();
        designatorField.clear();
        originField.clear();
        destinationField.clear();
        departField.clear();
        arriveField.clear();
        if (table != null) table.getSelectionModel().clearSelection();
    }

    @FXML
    private void onAddOrUpdate() {
        if (model.newStateProperty().get()) {
            // ADD
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
            db.save();
            model.markExisting();
        } else {
            // UPDATE
            ScheduledFlight sel = (table == null) ? null : table.getSelectionModel().getSelectedItem();
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
            db.save();
            model.markExisting();
        }
    }

    @FXML
    private void onDelete() {
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
