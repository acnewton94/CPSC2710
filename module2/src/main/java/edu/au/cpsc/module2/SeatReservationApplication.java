/**
 * Alex Newton
 * azn0100@auburn.edu
 * September 2, 2025
 * Description: This class represents a seat reservation with passenger info and options.
 */
package edu.au.cpsc.module2;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;

public class SeatReservationApplication extends Application {

    @Override
    public void start(Stage ignoredPrimaryStage) {
        showEditorWindow();
    }

    private void showEditorWindow() {
        Stage stage = new Stage();
        stage.setTitle("Seat Reservation Editor");
        stage.setResizable(false);
        stage.initModality(Modality.NONE);

        TextField tfDesignator = new TextField();
        DatePicker dpDate      = new DatePicker(LocalDate.now());
        TextField tfFirst      = new TextField();
        TextField tfLast       = new TextField();
        Spinner<Integer> spBags = new Spinner<>(0, 10, 0);
        spBags.setEditable(true);
        CheckBox cbInfant      = new CheckBox();
        Spinner<Integer> spPassengers = new Spinner<>(0, 10, 0);
        spPassengers.setEditable(true);

        GridPane g = new GridPane();
        g.setHgap(12);
        g.setVgap(10);
        g.setPadding(new Insets(16));

        int r = 0;
        g.add(new Label("Flight designator:"), 0, r); g.add(tfDesignator, 1, r++);
        g.add(new Label("Flight date:"),      0, r); g.add(dpDate,      1, r++);
        g.add(new Label("First name:"),       0, r); g.add(tfFirst,     1, r++);
        g.add(new Label("Last name:"),        0, r); g.add(tfLast,      1, r++);
        g.add(new Label("Bags:"),             0, r); g.add(spBags,      1, r++);
        g.add(new Label("Flying with infant?"),0, r); g.add(cbInfant,   1, r++);
        g.add(new Label("Number of passengers:"), 0, r); g.add(spPassengers, 1, r++);

        GridPane.setHgrow(tfDesignator, Priority.ALWAYS);
        GridPane.setHgrow(dpDate,       Priority.ALWAYS);
        GridPane.setHgrow(tfFirst,      Priority.ALWAYS);
        GridPane.setHgrow(tfLast,       Priority.ALWAYS);

        Button btnCancel = new Button("Cancel");
        Button btnSave   = new Button("Save");
        HBox buttons = new HBox(10, btnCancel, btnSave);
        buttons.setAlignment(Pos.CENTER_RIGHT);
        g.add(buttons, 0, r, 2, 1);

        btnCancel.setOnAction(e -> stage.close());

        btnSave.setOnAction(e -> {
            try {
                SeatReservation s = new SeatReservation();
                s.setFlightDesignator(tfDesignator.getText());
                s.setFlightDate(dpDate.getValue());
                s.setFirstName(tfFirst.getText());
                s.setLastName(tfLast.getText());
                s.setNumberOfBags(spBags.getValue());
                s.setNumberOfPassengers(spPassengers.getValue());
                if (cbInfant.isSelected()) s.makeFlyingWithInfant(); else s.makeNotFlyingWithInfant();

                new Alert(Alert.AlertType.INFORMATION, "Saved:\n" + s).showAndWait();

            } catch (IllegalArgumentException ex) {
                new Alert(Alert.AlertType.ERROR, ex.getMessage()).showAndWait();
            }
        });

        stage.setScene(new Scene(g, 420, 300));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
