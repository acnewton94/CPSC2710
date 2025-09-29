/*
 * Project: CPSC 2710 — Module 6, Part 1
 * Author: Alex Newton
 * Auburn Email: azn0100@auburn.edu
 * Date: 2025-09-28
 * Description: JavaFX app for bindings practice per Part 1 instructions.
 */
package edu.au.cpsc.part1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Part1Application extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Part1Application.class.getResource("part1-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Alex Newton's Flight Part 1 App");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}