package edu.au.cpsc.module4;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

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
