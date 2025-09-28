package edu.au.cpsc.part1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Part1Application extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxml = new FXMLLoader(Part1Application.class.getResource("part1-view.fxml"));
        Scene scene = new Scene(fxml.load());
        stage.setTitle("Alex Newton's Part1 App");
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) { launch(args); }
}
