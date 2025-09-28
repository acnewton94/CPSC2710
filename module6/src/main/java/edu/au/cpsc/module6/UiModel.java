package edu.au.cpsc.module6;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class UiModel {
    // Inputs bound to text fields
    private final StringProperty designator  = new SimpleStringProperty("");
    private final StringProperty origin      = new SimpleStringProperty("");
    private final StringProperty destination = new SimpleStringProperty("");
    private final StringProperty departTime  = new SimpleStringProperty(""); // HH:mm
    private final StringProperty arriveTime  = new SimpleStringProperty(""); // HH:mm

    // Screen state
    private final BooleanProperty newState      = new SimpleBooleanProperty(true);
    private final BooleanProperty modifiedState = new SimpleBooleanProperty(false);

    // Validation rules (tweak to your rubric if needed)
    private final BooleanBinding designatorValid =
            Bindings.createBooleanBinding(
                    () -> designator.get().matches("^[A-Za-z]{2,3}\\d{1,4}$"),
                    designator);

    private final BooleanBinding timesValid =
            Bindings.createBooleanBinding(
                    () -> departTime.get().matches("^\\d{2}:\\d{2}$")
                       && arriveTime.get().matches("^\\d{2}:\\d{2}$"),
                    departTime, arriveTime);

    private final BooleanBinding fieldsNonEmpty =
            Bindings.createBooleanBinding(
                    () -> !origin.get().isBlank() && !destination.get().isBlank(),
                    origin, destination);

    public final BooleanBinding inputValid =
            designatorValid.and(timesValid).and(fieldsNonEmpty);

    // Button enables
    public final BooleanBinding newButtonEnabled     = newState;
    public final BooleanBinding addButtonEnabled     = newState.and(inputValid);
    public final BooleanBinding deleteButtonEnabled  = newState.not().and(inputValid);
    public final BooleanBinding updateButtonEnabled  = newState.not().and(modifiedState).and(inputValid);

    // Expose properties
    public StringProperty designatorProperty()  { return designator; }
    public StringProperty originProperty()      { return origin; }
    public StringProperty destinationProperty() { return destination; }
    public StringProperty departTimeProperty()  { return departTime; }
    public StringProperty arriveTimeProperty()  { return arriveTime; }

    public BooleanProperty newStateProperty()      { return newState; }
    public BooleanProperty modifiedStateProperty() { return modifiedState; }

    // State helpers
    public void markNew()      { newState.set(true);  modifiedState.set(false); }
    public void markExisting() { newState.set(false); modifiedState.set(false); }
    public void markModified() { if (!newState.get()) modifiedState.set(true); }
}
