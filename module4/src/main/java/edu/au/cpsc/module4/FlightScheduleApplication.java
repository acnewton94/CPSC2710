package edu.au.cpsc.module4;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/*
 Project: Module 4 – Flight Designator App
 Author: Alex Newton
 Auburn Email: azn0100@auburn.edu
 Date: 2025-09-14
 Description: JavaFX controller for table/detail editor with CSV persistence.
*/

public class FlightScheduleApplication extends Application {

    private static final String FIRST = "Alex";
    private static final String LAST  = "Newton";

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                FlightScheduleApplication.class.getResource("flight-schedule-view.fxml")
        );
        Scene scene = new Scene(loader.load());
        stage.setTitle(FIRST + " " + LAST + "'s Flight Designator App");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
