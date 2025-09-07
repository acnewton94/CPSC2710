package edu.au.cpsc.module3;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AirportApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(
                getClass().getResource("/edu/au/cpsc/module3/airport-view.fxml")
        );
        Scene scene = new Scene(root, 860, 520);
        stage.setTitle("Alex Newton's Airport Mapview App");
        stage.setScene(scene);
        stage.setMinWidth(800);
        stage.setMinHeight(480);
        stage.show();
    }

    public static void main(String[] args) { launch(args); }
}
