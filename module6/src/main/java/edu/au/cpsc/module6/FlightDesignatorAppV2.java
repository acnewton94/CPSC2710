package edu.au.cpsc.module6;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FlightDesignatorAppV2 extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxml = new FXMLLoader(
                FlightDesignatorAppV2.class.getResource("flight-schedule-view.fxml"));
        Scene scene = new Scene(fxml.load());
        stage.setTitle("Alex Newton's Flight Designator App V2");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
