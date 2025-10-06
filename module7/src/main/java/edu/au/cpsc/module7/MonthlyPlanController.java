package edu.au.cpsc.module7;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import java.time.temporal.ChronoUnit;
import java.time.*;
import java.time.format.TextStyle;
import java.util.Locale;

public class MonthlyPlanController {

    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private ComboBox<Integer> daysPerWeekCombo;
    @FXML
    private Button btnGeneratePlan;
    @FXML
    private GridPane gridCalendar;
    @FXML
    private Label lblSummary;

    private static final int MAX_DAYS = 42; // 6 rows × 7 columns

    @FXML
    private void initialize() {
        // Populate daysPerWeek combo box
        for (int i = 1; i <= 7; i++) {
            daysPerWeekCombo.getItems().add(i);
        }
        daysPerWeekCombo.setValue(5); // default

        // Set default dates
        startDatePicker.setValue(LocalDate.now());
        endDatePicker.setValue(LocalDate.now().plusWeeks(4));

        // Clear calendar cells at start
        clearCalendar();
    }

    @FXML
    private void onGeneratePlan() {
        LocalDate start = startDatePicker.getValue();
        LocalDate end = endDatePicker.getValue();
        int daysPerWeek = daysPerWeekCombo.getValue();

        if (start == null || end == null || start.isAfter(end)) {
            lblSummary.setText("Please select a valid start and end date.");
            return;
        }

        clearCalendar();

        int totalDays = (int) ChronoUnit.DAYS.between(start, end) + 1;
        int readingDays = (totalDays / 7) * daysPerWeek + Math.min(daysPerWeek, totalDays % 7);

        lblSummary.setText(String.format("Planned to read %d days between %s and %s.",
                readingDays, start, end));

        fillCalendar(start, end, daysPerWeek);
    }

    private void clearCalendar() {
        for (var node : gridCalendar.getChildren()) {
            if (node instanceof Label) {
                ((Label) node).setText("");
            }
        }
    }

    private void fillCalendar(LocalDate start, LocalDate end, int daysPerWeek) {
        int maxLabels = gridCalendar.getChildren().size();
        LocalDate current = start;

        int readingDaysPerWeek = daysPerWeek;
        int currentWeekDayCount = 0;
        int labelIndex = getCellIndex(start);  // Index in 0–41 range

        while (!current.isAfter(end) && labelIndex < maxLabels) {
            DayOfWeek dow = current.getDayOfWeek();

            // Only add reading days up to the user-defined limit per week
            if (currentWeekDayCount < readingDaysPerWeek) {
                Label cell = (Label) gridCalendar.getChildren().get(labelIndex);
                cell.setText(String.valueOf(current.getDayOfMonth()));
                currentWeekDayCount++;
            }

            // Reset after Saturday (end of week)
            if (dow == DayOfWeek.SATURDAY) {
                currentWeekDayCount = 0;
            }

            current = current.plusDays(1);
            labelIndex++;
        }
    }


    private int getCellIndex(LocalDate date) {
        // Start with the first of the *selected start month*
        LocalDate firstOfMonth = date.withDayOfMonth(1);
        int dowIndex = firstOfMonth.getDayOfWeek().getValue(); // 1 (Mon) to 7 (Sun)
        int offset = (dowIndex % 7); // Convert to 0 (Sun) to 6 (Sat)
        return offset;  // calendar starts with day 1 in this position
    }
}