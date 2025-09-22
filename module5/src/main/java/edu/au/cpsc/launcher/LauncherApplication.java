package edu.au.cpsc.launcher;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/*
 Project: Module 5
 Author: Alex Newton
 Auburn Email: azn0100@auburn.edu
 Date: 2025-09-22
*/

public class LauncherApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        var fxml = new FXMLLoader(
                LauncherApplication.class.getResource("/edu/au/cpsc/launcher/launcher-app.fxml")
        );

        Parent root = fxml.load();

        Scene scene = new Scene(root, 720, 480);

        // ⬇️ Add CSS from code to avoid FXML stylesheet pitfalls
        scene.getStylesheets().add(
                LauncherApplication.class
                        .getResource("/edu/au/cpsc/launcher/style/main.css")
                        .toExternalForm()
        );

        stage.setTitle("Alex Newton — Launcher");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) { launch(); }
}
