package edu.au.cpsc.part1;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.IntegerBinding;

public class Part1Controller {

    @FXML private TextField messageTextField;
    @FXML private TextField echoTextField;

    @FXML private TextField firstBidirectionalTextField;
    @FXML private TextField secondBidirectionalTextField;

    @FXML private Slider secretSlider;
    @FXML private ImageView secretOverlayImageView;

    @FXML private CheckBox selectMeCheckBox;
    @FXML private Label selectMeLabel;

    @FXML private TextField tweetTextField;
    @FXML private Label numberOfCharactersLabel;
    @FXML private Label validityLabel;

    @FXML
    public void initialize() {
        // 10) one-way: echo mirrors message
        echoTextField.textProperty().bind(messageTextField.textProperty());

        // 11) bi-directional
        Bindings.bindBidirectional(firstBidirectionalTextField.textProperty(),
                                   secondBidirectionalTextField.textProperty());

        // 12) slider -> image opacity (slider range 0..1)
        secretOverlayImageView.opacityProperty().bind(secretSlider.valueProperty());

        // 13) checkbox -> label true/false
        selectMeLabel.textProperty().bind(selectMeCheckBox.selectedProperty().asString());

        // 14) character count for tweetTextField
        IntegerBinding tweetLen = tweetTextField.textProperty().length();
        numberOfCharactersLabel.textProperty().bind(tweetLen.asString());

        // 15) Valid if <=10 chars; otherwise Invalid
        BooleanBinding isValid = tweetLen.lessThanOrEqualTo(10);
        validityLabel.textProperty().bind(Bindings.when(isValid).then("Valid").otherwise("Invalid"));
    }
}
