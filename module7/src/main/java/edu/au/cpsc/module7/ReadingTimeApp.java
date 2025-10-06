package edu.au.cpsc.module7;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;

import java.net.URL;
import java.util.Objects;

public class ReadingTimeApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // Load MainView.fxml
        URL fxmlUrl = Objects.requireNonNull(
                getClass().getResource("/edu/au/cpsc/module7/MainView.fxml"),
                "MainView.fxml not found on classpath"
        );

        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Parent root = loader.load();

        Scene scene = new Scene(root);

        // Load and apply CSS
        URL cssUrl = getClass().getResource("/edu/au/cpsc/module7/theme.css");
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
            scene.getStylesheets().add(getClass().getResource("theme.css").toExternalForm());
        }

        stage.setTitle("Reading Time Planner");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

