/*
 * Project: CPSC 2710 — Module 6, Part 1
 * Author: Alex Newton
 * Auburn Email: azn0100@auburn.edu
 * Date: 2025-09-28
 * Description: JavaFX app for bindings practice per Part 1 instructions.
 */

package edu.au.cpsc.part1;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.IntegerBinding;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class Part1Controller {

    @FXML
    private TextField messageTextField, echoTextField, firstBidirectionalTextField, secondBidirectionalTextField;

    @FXML
    private ImageView secretOverlayImageView;

    @FXML
    private Slider secretSlider;

    @FXML
    private CheckBox selectMeCheckBox;

    @FXML
    private Label selectMeLabel;

    @FXML
    private TextField tweetTextField;

    @FXML
    private Label numberOfCharactersLabel, validityLabel;

    @FXML
    private void initialize() {
        echoTextField.textProperty().bind(messageTextField.textProperty());
        firstBidirectionalTextField.textProperty()
                .bindBidirectional(secondBidirectionalTextField.textProperty());

        secretOverlayImageView.opacityProperty().bind(secretSlider.valueProperty());

        selectMeLabel.textProperty().bind(selectMeCheckBox.selectedProperty().asString());

        numberOfCharactersLabel.textProperty()
                .bind(Bindings.length(tweetTextField.textProperty()).asString());

        validityLabel.textProperty().bind(
                Bindings.when(Bindings.length(tweetTextField.textProperty()).lessThanOrEqualTo(10))
                        .then("Valid")
                        .otherwise("Invalid")
        );
    }
}