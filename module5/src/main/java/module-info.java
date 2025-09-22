/*
 Project: Module 5
 Author: Alex Newton
 Auburn Email: azn0100@auburn.edu
 Date: 2025-09-22
*/

module module5 {
    requires javafx.controls;
    requires javafx.fxml;

    opens edu.au.cpsc.miscstyle to javafx.fxml;
    exports edu.au.cpsc.miscstyle;

    opens edu.au.cpsc.launcher to javafx.fxml;
    exports edu.au.cpsc.launcher;
}

