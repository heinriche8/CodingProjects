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

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Main class of the GTFS tool application
 * @author Cody Mikula
 * @version 1.3
 */
public class Driver extends Application {

	public GTFS gtfs;

	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * Sets up and displays the JavaFX program
	 * @param stage Frame or window of the program
	 */
	@Override
	public void start(Stage stage) {

		// Get GTFS Instance
		gtfs = GTFS.getInstance();

		// Instantiate primary GUI controller
		Controller controller = new Controller();

		// Configure primary GUI
		Parent root = configureGUI(controller, gtfs);
		stage.setScene(new Scene(root));
		stage.setTitle("GTFS Tool");

		// Instantiate editor GUI controller
		EditorController editorController = new EditorController();

		// Set the reference to the editor GUI in the primary GUI
		controller.secondRoot = editorGUI(editorController);

		// Initialize the primary GUI
		controller.initialize();

		// Show the main GUI
		stage.show();
	}

	/**
	 * Configures the primary GUI
	 * @param controller controller object for the GUI
	 * @return a parent containing the primary GUI
	 */
	Parent configureGUI(Controller controller, GTFS gtfs) {

		// Create root pane
		VBox root = new VBox();

		root.setPrefSize(1280,720);

		// Upper and lower halves of the window
		HBox upper = new HBox();
		HBox lower = new HBox();

		upper.setAlignment(Pos.CENTER);
		upper.setSpacing(10);
		upper.setPadding(new Insets(10, 10, 10 ,10));

		lower.setAlignment(Pos.TOP_CENTER);
		lower.setSpacing(10);
		lower.setPadding(new Insets(10,10,10,10));

		// Import/Export buttons
		Button importButton = new Button();
		Button exportButton = new Button();
		Button editButton = new Button();

		importButton.setText("Import");
		exportButton.setText("Export");
		editButton.setText("Edit Data");

		// Search fields
		TextField searchStops = new TextField();
		searchStops.setPromptText("Search for a Stop");
		TextField searchRoutes = new TextField();
		searchRoutes.setPromptText("Search for a Route");
		TextField searchTripByRoute = new TextField();
		searchTripByRoute.setPromptText("Search for Trips by Route");
		TextField searchTripByStop = new TextField();
		searchTripByStop.setPromptText("Search for Trips by Stop");

		searchRoutes.setOnAction(e -> controller.onSearchRoute());
		searchStops.setOnAction(e -> controller.onSearchStop());
		searchTripByStop.setOnAction(e -> controller.onSearchTripByStop());
		searchTripByRoute.setOnAction(e -> controller.onSearchTripByRoute());


		controller.searchRoutes = searchRoutes;
		controller.searchStops = searchStops;
		controller.searchTripByRoute = searchTripByRoute;
		controller.searchTripByStop = searchTripByStop;


		// Search Buttons
		Button stopsButton = new Button();
		stopsButton.setText("Search");
		stopsButton.setOnAction(e -> controller.onSearchStop());

		Button routesButton = new Button();
		routesButton.setText("Search");
		routesButton.setOnAction(e -> controller.onSearchRoute());

		Button tripsByRouteButton = new Button();
		tripsByRouteButton.setText("Search");
		tripsByRouteButton.setOnAction(e -> controller.onSearchTripByRoute());


		Button tripsByStopButton = new Button();
		tripsByStopButton.setText("Search");
		tripsByStopButton.setOnAction(e -> controller.onSearchTripByStop());

		// Text Areas
		TextArea stopsArea = new TextArea();
		stopsArea.setPrefHeight(3840);
		stopsArea.setEditable(false);
		controller.stopsArea = stopsArea;

		TextArea routesArea = new TextArea();
		routesArea.setPrefHeight(3840);
		routesArea.setEditable(false);
		controller.routesArea = routesArea;

		TextArea tripsByRouteArea = new TextArea();
		tripsByRouteArea.setPrefHeight(3840);
		tripsByRouteArea.setEditable(false);
		controller.tripsByRouteArea = tripsByRouteArea;

		TextArea tripsByStopsArea = new TextArea();
		tripsByStopsArea.setPrefHeight(3840);
		tripsByStopsArea.setEditable(false);
		controller.tripsByStopsArea = tripsByStopsArea;

		Calculate times = new Calculate();
		times.setPrefHeight(3840);
		times.setEditable(false);

		gtfs.attach(times);

		// Vertical quadrants
		VBox stopDiv = new VBox();
		VBox routeDiv = new VBox();
		VBox tripDiv = new VBox();
		VBox stopTimeDiv = new VBox();
		VBox timeDiv = new VBox();

		configDivs(stopDiv);
		configDivs(routeDiv);
		configDivs(tripDiv);
		configDivs(stopTimeDiv);
		configDivs(timeDiv);

		// Bind on click events
		importButton.setOnAction(e -> controller.load());
		exportButton.setOnAction(e -> controller.exportData());
		editButton.setOnAction(e -> controller.openEditor());


		// Populate stops division
		stopDiv.getChildren().addAll(searchStops, stopsButton, stopsArea);

		// Populate routes division
		routeDiv.getChildren().addAll(searchRoutes, routesButton, routesArea);

		// Populate trips division
		tripDiv.getChildren().addAll(searchTripByRoute, tripsByRouteButton, tripsByRouteArea);

		// Populate stop time division
		stopTimeDiv.getChildren().addAll(searchTripByStop, tripsByStopButton, tripsByStopsArea);

		// Populate the time division
		timeDiv.getChildren().addAll(times);

		// Fill the lower half hbox
		lower.getChildren().addAll(stopDiv, routeDiv, tripDiv, stopTimeDiv, timeDiv);

		// Populate the upper half hbox
		upper.getChildren().addAll(importButton, exportButton, editButton);

		root.getChildren().addAll(upper, lower);

		return root;
	}

	/**
	 * Configures some of the panes of the primary GUI
	 * @param target VBox being formatted
	 */
	private void configDivs(VBox target) {
		target.setAlignment(Pos.TOP_CENTER);
		target.setSpacing(5);
		target.setPadding(new Insets(10, 10 ,10 ,10));
	}

	/**
	 * Configures the GUI for the editor screen
	 * @param controller controller for the editor screen
	 * @return a parent containing the controller GUI
	 */
	Parent editorGUI(EditorController controller) {

		// Root Pane
		HBox root = new HBox();

		// Left half VBox, contains edit stop and trip
		VBox leftBox = new VBox();

		// Right half vbox, contains edit stoptime and route
		VBox rightBox = new VBox();

		leftBoxConfig(leftBox, controller);
		rightBoxConfig(rightBox, controller);

		root.getChildren().addAll(leftBox, rightBox);

		return root;
	}

	/**
	 * Helper method to configure some of the VBoxes for the
	 * editor GUI
	 * @param leftBox VBox being formatted
	 * @param controller The editor GUI controller
	 */
	private void leftBoxConfig(VBox leftBox, EditorController controller) {

		leftBox.setAlignment(Pos.CENTER_LEFT);
		leftBox.setPadding(new Insets(5, 20, 5, 5));
		leftBox.setSpacing(5);

		Button editStop = new Button();
		editStop.setText("Edit Stop");
		editStop.setOnAction(e -> controller.editStop());

		TextField stopTextField = new TextField();
		stopTextField.setPromptText("Enter a Stop ID");
		controller.stopTextField = stopTextField;

		TextArea stopData = new TextArea();
		controller.stopData = stopData;

		Button save1 = new Button();
		save1.setText("Save");
		save1.setOnAction(e -> controller.saveStop());

		Button save2 = new Button();
		save2.setText("Save");
		save2.setOnAction(e -> controller.saveTrip());

		Button editTrip = new Button();
		editTrip.setText("Edit Trip");
		editTrip.setOnAction(e -> controller.editTrip());

		TextField tripTextField = new TextField();
		tripTextField.setPromptText("Enter a Trip ID");
		controller.tripTextField = tripTextField;

		TextArea tripData = new TextArea();
		controller.tripData = tripData;

		leftBox.getChildren().addAll(editStop, stopTextField, stopData, save1, editTrip, tripTextField, tripData, save2);
	}

	/**
	 * Helper method to configure come of the VBoxes for
	 * the editor GUI
	 * @param rightBox VBox being formatted
	 * @param controller the editor GUI controller
	 */
	private void rightBoxConfig(VBox rightBox, EditorController controller) {

		rightBox.setAlignment(Pos.CENTER_LEFT);
		rightBox.setPadding(new Insets(5, 5, 5, 5));
		rightBox.setSpacing(5);

		Button editStopTime = new Button();
		editStopTime.setText("Edit StopTime");
		editStopTime.setOnAction(e -> controller.editStopTime());

		TextField stopTimeField = new TextField();
		stopTimeField.setPromptText("Enter a Trip ID, Stop ID");
		controller.stopTimeTextField = stopTimeField;

		TextArea stopTimeArea = new TextArea();
		controller.stopTimeData = stopTimeArea;

		Button save1 = new Button();
		save1.setText("Save");
		save1.setOnAction(e -> controller.saveStopTime());

		Button save2 = new Button();
		save2.setText("Save");
		save2.setOnAction(e -> controller.saveRoute());

		Button editRoute = new Button();
		editRoute.setText("Edit Route");
		editRoute.setOnAction(e -> controller.editRoute());

		TextField routeIDField = new TextField();
		routeIDField.setPromptText("Enter a Route ID");
		controller.routeTextField = routeIDField;

		TextArea editRouteID = new TextArea();
		controller.routeData = editRouteID;

		rightBox.getChildren().addAll(editStopTime, stopTimeField, stopTimeArea, save1, editRoute, routeIDField, editRouteID, save2);
	}

}
