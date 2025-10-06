package edu.au.cpsc.module7;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    @FXML private TextField txtPages;
    @FXML private TextField txtTimePerPage;
    @FXML private TextField txtBreaks;
    @FXML private TextField txtBreakDuration;
    @FXML private TextArea txtResults;

    @FXML
    private void onCalculate() {
        try {
            int pages = Integer.parseInt(txtPages.getText());
            double timePerPage = Double.parseDouble(txtTimePerPage.getText());
            int breaks = Integer.parseInt(txtBreaks.getText());
            double breakDuration = Double.parseDouble(txtBreakDuration.getText());

            double totalReadingTime = pages * timePerPage;
            double totalWithBreaks = totalReadingTime + (breaks * breakDuration);

            String result = String.format(
                    "Reading Time (no breaks): %.2f mins\n" +
                            "Reading Time (with breaks): %.2f mins",
                    totalReadingTime, totalWithBreaks
            );

            txtResults.setText(result);
        } catch (NumberFormatException e) {
            txtResults.setText("Please enter valid numbers in all fields.");
        }
    }

    @FXML
    private void onOpenCalendar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MonthlyPlan.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Monthly Reading Plan");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
