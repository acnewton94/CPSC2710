package edu.au.cpsc.miscstyle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Part1Application extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxml = new FXMLLoader(
                Part1Application.class.getResource("/edu/au/cpsc/miscstyle/part1.fxml")
        );
        Scene scene = new Scene(fxml.load());
        stage.setTitle("Alex Newton's Flight Designator App");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
