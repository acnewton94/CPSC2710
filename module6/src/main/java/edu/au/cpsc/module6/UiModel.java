package edu.au.cpsc.module6;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class UiModel {
    private final StringProperty designator  = new SimpleStringProperty("");
    private final StringProperty origin      = new SimpleStringProperty("");
    private final StringProperty destination = new SimpleStringProperty("");
    private final StringProperty departTime  = new SimpleStringProperty(""); // HH:mm
    private final StringProperty arriveTime  = new SimpleStringProperty(""); // HH:mm

    private final BooleanProperty newState      = new SimpleBooleanProperty(true);
    private final BooleanProperty modifiedState = new SimpleBooleanProperty(false);

    public final BooleanBinding designatorValid =
            Bindings.createBooleanBinding(
                    () -> designator.get().matches("^[A-Za-z]{2,3}\\d{1,4}$"),
                    designator);

    public final BooleanBinding departValid =
            Bindings.createBooleanBinding(
                    () -> departTime.get().matches("^\\d{2}:\\d{2}$"),
                    departTime);

    public final BooleanBinding arriveValid =
            Bindings.createBooleanBinding(
                    () -> arriveTime.get().matches("^\\d{2}:\\d{2}$"),
                    arriveTime);

    public final BooleanBinding originNonEmpty =
            Bindings.createBooleanBinding(
                    () -> !origin.get().isBlank(),
                    origin);

    public final BooleanBinding destinationNonEmpty =
            Bindings.createBooleanBinding(
                    () -> !destination.get().isBlank(),
                    destination);

    public final BooleanBinding inputValid =
            designatorValid.and(departValid).and(arriveValid)
                    .and(originNonEmpty).and(destinationNonEmpty);

    public final BooleanBinding newButtonEnabled =
            Bindings.createBooleanBinding(newState::get, newState);

    public final BooleanBinding addButtonEnabled    = newState.and(inputValid);
    public final BooleanBinding deleteButtonEnabled = newState.not().and(inputValid);
    public final BooleanBinding updateButtonEnabled = newState.not().and(modifiedState).and(inputValid);

    public StringProperty designatorProperty()  { return designator; }
    public StringProperty originProperty()      { return origin; }
    public StringProperty destinationProperty() { return destination; }
    public StringProperty departTimeProperty()  { return departTime; }
    public StringProperty arriveTimeProperty()  { return arriveTime; }

    public ReadOnlyBooleanProperty newStateProperty() { return newState; }

    public void markNew() {
        newState.set(true);
        modifiedState.set(false);
    }
    public void markExisting() {
        newState.set(false);
        modifiedState.set(false);
    }
    public void markModified() {
        if (!newState.get()) {
            modifiedState.set(true);
        }
    }
}
