/*
 * Course: SE 2030 021
 * Spring 2021-2022
 * Lab 5 - Classes and Git
 * Name: AfternoonGroup3
 * Created: 4/7/2022
 * This file is part of GTFS group3.
GTFS group3 is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
GTFS group3 is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with GTFS group3. If not, see <https://www.gnu.org/licenses/>.
 */
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.HashMap;
import java.util.Hashtable;

/**
 * Acts as the interface between the model and view by handling events
 * @author Nathan Kitzman
 * @version 1.0
 */
public class EditorController {

    private GTFS gtfs = GTFS.getInstance();


    TextField stopTextField;


    TextField stopTimeTextField;


    TextField tripTextField;


    TextField routeTextField;


    TextArea stopData;


    TextArea stopTimeData;


    TextArea tripData;


    TextArea routeData;

    private Editor editor = new Editor();

    /**
     * this method Gets all the attributes of a stop to edit
     */
    protected void editStop(){
        if(!stopTextField.getText().isEmpty()) {
            stopData.setText(gtfs.getStop(stopTextField.getText()).toString());
        } else {
            generateAlert("No stop_ID given", "Error", "Not enough data", Alert.AlertType.ERROR);
        }
    }

    protected void saveStop(){
        if(!stopTextField.getText().isEmpty()) {
            String stopID = stopTextField.getText();
            String stopInfo = stopData.getText();
            if (stopInfo.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)").length == 4) {
                editor.updateStop(stopID, stopInfo.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)")[0].split(":")[1].substring(1), stopInfo.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)")[1].split(":")[1].substring(1), Double.parseDouble(stopInfo.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)")[2].split(":")[1].substring(1)), Double.parseDouble(stopInfo.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)")[3].split(":")[1].substring(1)));
            } else {
                generateAlert("there is not enough fields here", "Error", "Not enough data", Alert.AlertType.ERROR);
            }
        } else {
            generateAlert("No stop_ID given", "Error", "Not enough data", Alert.AlertType.ERROR);
        }
    }

    /**
     * this method Gets all the attributes of a StopTime to edit
     */
    protected void editStopTime(){
        if(!stopTimeTextField.getText().isEmpty()) {
            String stopID = stopTimeTextField.getText().split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)")[1];
            String tripID = stopTimeTextField.getText().split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)")[0];
            stopTimeData.setText(gtfs.getStopTime(tripID,stopID).toString());
        } else {
            generateAlert("No stop_ID given", "Error", "Not enough data", Alert.AlertType.ERROR);
        }
    }


    protected void saveStopTime(){
        String stopTimeInfo = stopTimeData.getText();
        if(stopTimeInfo.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)").length == 5 && !stopTimeTextField.getText().isEmpty()) {
            String stopID = stopTimeTextField.getText().split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)")[1];
            String tripID = stopTimeTextField.getText().split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)")[0];
            editor.updateStopTime(tripID, stopID, stopTimeInfo.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)")[0].split(":")[1].substring(1), stopTimeInfo.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)")[1].split(":")[1].substring(1), stopTimeInfo.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)")[2].substring(15), stopTimeInfo.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)")[3].substring(17), Integer.parseInt(stopTimeInfo.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)")[4].split(":")[1].substring(1)));
        } else {
            generateAlert("there is not enough fields here", "Error", "Not enough data", Alert.AlertType.ERROR);
        }
    }

    /**
     * this method Gets all the attributes of a trip to edit
     */
    protected void editTrip(){
        if(!tripTextField.getText().isEmpty()) {
            tripData.setText(gtfs.getTrip(tripTextField.getText()).toString());
        } else {
            generateAlert("No stop_ID given", "Error", "Not enough data", Alert.AlertType.ERROR);
        }
    }

    protected void saveTrip(){
        String tripInfo = tripData.getText();
        if(tripInfo.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)").length == 3 && !tripTextField.getText().isEmpty()) {
            String tripID = tripTextField.getText();
            editor.updateTrip(tripID, tripInfo.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)")[2].split(":")[1].substring(1), tripInfo.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)")[0].split(":")[1].substring(1), tripInfo.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)")[1].split(":")[1].substring(1));
        } else {
            generateAlert("there is not enough fields here", "Error", "Not enough data", Alert.AlertType.ERROR);
        }
    }

    /**
     * this method Gets all the attributes of a Route to edit
     */

    protected void editRoute(){
        if(!routeTextField.getText().isEmpty()) {
            routeData.setText(gtfs.getRoute(routeTextField.getText()).toString());
        } else {
            generateAlert("No stop_ID given", "Error", "Not enough data", Alert.AlertType.ERROR);
        }
    }


    protected void saveRoute(){
        String routeInfo = routeData.getText();
        if(routeInfo.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)").length == 5 && !routeTextField.getText().isEmpty()) {
            String routeID = routeTextField.getText();
            editor.updateRoute(routeID, routeInfo.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)")[0].split(":")[1].substring(1), routeInfo.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)")[1].split(":")[1].substring(1), routeInfo.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)")[2].split(":")[1].substring(1), routeInfo.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)")[3].split(":")[1].substring(1), Integer.parseInt(routeInfo.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)")[4].split(":")[1].substring(1)));
        } else {
            generateAlert("there is not enough fields here", "Error", "Not enough data", Alert.AlertType.ERROR);
        }
    }
    private void generateAlert(String message, String title, String header, Alert.AlertType type) {
        Alert alert = new Alert(type, message);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.showAndWait();
    }
}
