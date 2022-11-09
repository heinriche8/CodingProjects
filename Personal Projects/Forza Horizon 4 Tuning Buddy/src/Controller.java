import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class Controller {
    @FXML
    private TextField frontMax, frontMin, frontWeight, frontResult;

    @FXML
    private TextField backMax, backMin, backWeight, backResult;

    @FXML
    private TextField inputValue, inputPercent, outputPercent;

    @FXML
    private void frontCalc() {
        double max = 0, min = 0, weight = 0, result;
        boolean canCalc = true;


        try {
            max = Double.parseDouble(frontMax.getText());
            System.out.println(max);
        } catch (NumberFormatException exception) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid number provided");
            alert.setContentText("Provided maximum \"" + frontMax.getText() + "\" is not a valid number.");
            alert.showAndWait();
            frontMax.setText("");
            canCalc = false;
        }

        try {
            min = Double.parseDouble(frontMin.getText());
            System.out.println(min);
        } catch (NumberFormatException exception) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid number provided");
            alert.setContentText("Provided minimum \"" + frontMin.getText() + "\" is not a valid number.");
            alert.showAndWait();
            frontMin.setText("");
            canCalc = false;
        }

        try {
            weight = Double.parseDouble(frontWeight.getText());
            System.out.println(weight);
        } catch (NumberFormatException exception) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid number provided");
            alert.setContentText("Provided weight distribution \"" + frontWeight.getText() + "\" is not a valid number.");
            alert.showAndWait();
            frontWeight.setText("");
            canCalc = false;
        }

        if(canCalc) {
            result = (max - min) * weight + min;
            frontResult.setText(Double.toString(result));
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Cannot Calculate Value");
            alert.setContentText("One or more of the provided values were invalid.");
            alert.showAndWait();
        }
    }

    @FXML
    private void rearCalc() {
        double max = 0, min = 0, weight = 0, result;
        boolean canCalc = true;


        try {
            max = Double.parseDouble(backMax.getText());
            System.out.println(max);
        } catch (NumberFormatException exception) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid number provided");
            alert.setContentText("Provided maximum \"" + backMax.getText() + "\" is not a valid number.");
            alert.showAndWait();
            backMax.setText("");
            canCalc = false;
        }

        try {
            min = Double.parseDouble(backMin.getText());
            System.out.println(min);
        } catch (NumberFormatException exception) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid number provided");
            alert.setContentText("Provided minimum \"" + backMin.getText() + "\" is not a valid number.");
            alert.showAndWait();
            backMin.setText("");
            canCalc = false;
        }

        try {
            weight = Double.parseDouble(backWeight.getText());
            System.out.println(weight);
        } catch (NumberFormatException exception) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid number provided");
            alert.setContentText("Provided weight distribution \"" + backWeight.getText() + "\" is not a valid number.");
            alert.showAndWait();
            backWeight.setText("");
            canCalc = false;
        }

        if(canCalc) {
            result = (max - min) * weight + min;
            backResult.setText(Double.toString(result));
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Cannot Calculate Value");
            alert.setContentText("One or more of the provided values were invalid.");
            alert.showAndWait();
        }
    }

    @FXML
    private void fetchFront() {
        double frontVal;
        try {
            frontVal = Double.parseDouble(frontResult.getText());
            inputValue.setText(Double.toString(frontVal));
        } catch (NumberFormatException exception) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid number provided");
            alert.setContentText("No value present");
            alert.showAndWait();
            inputValue.setText("");
        }
    }

    @FXML
    private void fetchRear() {
        double backVal;
        try {
            backVal = Double.parseDouble(backResult.getText());
            inputValue.setText(Double.toString(backVal));
        } catch (NumberFormatException exception) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid number provided");
            alert.setContentText("No value present");
            alert.showAndWait();
            inputValue.setText("");
        }
    }

    @FXML
    private void calcPercent() {
        double percent = 0;
        double input = 0;
        double output = 0;

        boolean canCalc = true;

        try {
            percent = Double.parseDouble(inputPercent.getText());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid number");
            alert.setContentText("Provided percentage \"" + inputPercent.getText() + "\" is not valid.");
            alert.showAndWait();
            inputPercent.setText("");
            canCalc = false;
        }

        try {
            input = Double.parseDouble(inputValue.getText());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid number");
            alert.setContentText("Provided input \"" + inputValue.getText() + "\" is not valid.");
            alert.showAndWait();
            inputValue.setText("");
            canCalc = false;
        }

        if(canCalc) {
            output = input * percent;
            outputPercent.setText(Double.toString(output));
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid number");
            alert.setContentText("Cannot calculate value");
            alert.showAndWait();
        }
    }
}
