package edu.au.cpsc.module6;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.*;

import java.util.regex.Pattern;

/** Pure UI state + validation. No side-effects, no I/O. */
public class UiModel {
    // Inputs
    public final StringProperty designator  = new SimpleStringProperty("");
    public final StringProperty origin      = new SimpleStringProperty(""); // departure airport ident
    public final StringProperty destination = new SimpleStringProperty(""); // arrival airport ident
    public final StringProperty depart      = new SimpleStringProperty(""); // HH:mm
    public final StringProperty arrive      = new SimpleStringProperty(""); // HH:mm

    // Selection state (true when a row is selected in the table)
    public final BooleanProperty existingSelected = new SimpleBooleanProperty(false);

    // --- Validation ----------------------------------------------------------
    private static final Pattern TIME = Pattern.compile("^([01]\\d|2[0-3]):[0-5]\\d$");

    public final BooleanBinding designatorNonEmpty  =
            Bindings.createBooleanBinding(() -> !designator.get().isBlank(),  designator);
    public final BooleanBinding originNonEmpty      =
            Bindings.createBooleanBinding(() -> !origin.get().isBlank(),      origin);
    public final BooleanBinding destinationNonEmpty =
            Bindings.createBooleanBinding(() -> !destination.get().isBlank(), destination);
    public final BooleanBinding departValid         =
            Bindings.createBooleanBinding(() -> TIME.matcher(depart.get()).matches(),  depart);
    public final BooleanBinding arriveValid         =
            Bindings.createBooleanBinding(() -> TIME.matcher(arrive.get()).matches(),  arrive);

    public final BooleanBinding inputValid =
            designatorNonEmpty.and(originNonEmpty).and(destinationNonEmpty).and(departValid).and(arriveValid);

    // --- Modified state (vs. baseline) --------------------------------------
    private final StringProperty baseDesignator  = new SimpleStringProperty("");
    private final StringProperty baseOrigin      = new SimpleStringProperty("");
    private final StringProperty baseDestination = new SimpleStringProperty("");
    private final StringProperty baseDepart      = new SimpleStringProperty("");
    private final StringProperty baseArrive      = new SimpleStringProperty("");

    public final BooleanBinding modified =
            baseDesignator.isNotEqualTo(designator)
                    .or(baseOrigin.isNotEqualTo(origin))
                    .or(baseDestination.isNotEqualTo(destination))
                    .or(baseDepart.isNotEqualTo(depart))
                    .or(baseArrive.isNotEqualTo(arrive));

    // --- Button enablement (bind button.disableProperty() to .not()) --------
    public final BooleanBinding addEnabled    = existingSelected.not().and(inputValid);
    public final BooleanBinding updateEnabled = existingSelected.and(modified).and(inputValid);

    // FIXED: make deleteEnabled a BooleanBinding (not isEqualTo(true))
    public final BooleanBinding deleteEnabled =
            Bindings.createBooleanBinding(existingSelected::get, existingSelected);

    // --- Visual feedback styles ---------------------------------------------
    private static final String OK  = "";
    private static final String BAD = "-fx-border-color:#e11d48; -fx-border-width:1;";

    public final StringBinding designatorStyle  = Bindings.when(designatorNonEmpty).then(OK).otherwise(BAD);
    public final StringBinding originStyle      = Bindings.when(originNonEmpty).then(OK).otherwise(BAD);
    public final StringBinding destinationStyle = Bindings.when(destinationNonEmpty).then(OK).otherwise(BAD);
    public final StringBinding departStyle      = Bindings.when(departValid).then(OK).otherwise(BAD);
    public final StringBinding arriveStyle      = Bindings.when(arriveValid).then(OK).otherwise(BAD);

    // Snapshots current inputs as the "baseline" used by the modified binding.
    public void snapshot() {
        baseDesignator.set(designator.get());
        baseOrigin.set(origin.get());
        baseDestination.set(destination.get());
        baseDepart.set(depart.get());
        baseArrive.set(arrive.get());
    }

    // Convenience for "New"
    public void clear() {
        designator.set("");
        origin.set("");
        destination.set("");
        depart.set("");
        arrive.set("");
        existingSelected.set(false);
        snapshot();
    }
}

