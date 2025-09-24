package edu.au.cpsc.miscstyle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URL;

/*
 Project: Module 5
 Author: Alex Newton
 Auburn Email: azn0100@auburn.edu
 Date: 2025-09-22
*/
public class Part1Application extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        URL fxml = Part1Application.class.getResource("/edu/au/cpsc/miscstyle/part1.fxml");
        if (fxml == null) {
            throw new IllegalStateException("Cannot find /edu/au/cpsc/miscstyle/part1.fxml on classpath");
        }

        FXMLLoader loader = new FXMLLoader(fxml);
        Scene scene = new Scene(loader.load());

        URL cssUrl = Part1Application.class.getResource("/edu/au/cpsc/miscstyle/style/main.css");
        if (cssUrl == null) {
            throw new IllegalStateException("Cannot find /edu/au/cpsc/miscstyle/style/main.css on classpath");
        }
        String css = cssUrl.toExternalForm();
        if (!scene.getStylesheets().contains(css)) {
            scene.getStylesheets().add(css);
        }

        stage.setTitle("Alex Newton's Flight Designator App");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}