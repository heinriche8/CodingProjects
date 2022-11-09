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

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.util.*;


/**
 * Acts as the interface between the model and view by handling events
 * @author Cody Mikula, Nathan Kitzman, Evan Heinrich
 * @version 1.6
 */
public class Controller {

	protected TextArea stopsArea, routesArea, tripsByRouteArea, tripsByStopsArea;

	protected TextField searchStops, searchRoutes, searchTripByRoute, searchTripByStop;
	private FileChooser fileChooser;
	private GTFS gtfs;

	protected Parent secondRoot;


	private boolean open = false;

	private final Stage editStage = new Stage();

	public void initialize() {
		fileChooser = new FileChooser();
		File fileDirectory = new File(System.getProperty("user.dir") + "/Data");
		if (!fileDirectory.exists()) {
			fileDirectory.mkdir();
		}
		fileChooser.setInitialDirectory(fileDirectory);

		gtfs = GTFS.getInstance();

		Search searcher = new Search();

		gtfs.attach(searcher);

		//puts the display on to the stage
		editStage.setScene(new Scene(secondRoot, 600, 600));
	}

	public void setGTFS(GTFS gtfs) {
		this.gtfs = gtfs;
	}

	@FXML
	protected void load() {
		final int numGtfsFiles = 4;
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files",
				"*.txt"));
		List<File> files = fileChooser.showOpenMultipleDialog(null);
		try {
			if (files == null) {
				throw new NullPointerException();
			}
			if (files.size() != numGtfsFiles) {
				throw new IllegalArgumentException("A set of four files must be provided.");
			}

			for (File file : files) {
				String path = file.toString();
				int startOfExtension = path.lastIndexOf('.');
				String extension = path.substring(startOfExtension);
				if (!extension.equals(".txt")) {
					throw new IllegalArgumentException("Selected file extension is unsupported.");
				}
				int startOfFileName = path.lastIndexOf('\\') + 1;
				String fileName = path.substring(startOfFileName, startOfExtension);
				switch (fileName) {
					case "stops" -> gtfs.loadStop(file);
					case "trips" -> gtfs.loadTrip(file);
					case "stop_times" -> gtfs.loadStopTime(file);
					case "routes" -> gtfs.loadRoute(file);
					default -> throw new IllegalArgumentException("Invalid GTFS file name received.");
				}
			}
			if(gtfs.isLoaded()){
				gtfs.notifyObservers();
				Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
				alert.setContentText("Data successfully loaded");
				alert.setHeaderText("Import Successful");
				alert.setTitle("Success");
				alert.showAndWait();
			} else {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setContentText("Data Not Loaded");
				alert.setHeaderText("Import Failed");
				alert.setTitle("Error");
				alert.showAndWait();
			}
		} catch (NumberFormatException e) {
			generateAlert("A string could not be converted to a numeric format.",
					"Number Format Exception", "Number Format", Alert.AlertType.ERROR);
		} catch (InvalidPathException e) {
			generateAlert("Path string cannot be converted to a path.",
					"Invalid Path Exception", "Invalid Path", Alert.AlertType.ERROR);
		} catch (IllegalArgumentException e) {
			generateAlert(e.getMessage(), "Illegal Argument Exception", "Illegal Argument", Alert.AlertType.ERROR);
		} catch (IndexOutOfBoundsException e) {
			generateAlert("An invalid index was accessed.",
					"Index Out Of Bounds Exception", "Index Out Of Bounds", Alert.AlertType.ERROR);
		} catch (IOException e) {
			generateAlert("General input or output error occurred.", "I/O Exception",
					"I/O Error", Alert.AlertType.ERROR);
		} catch (NullPointerException e) {
			e.printStackTrace();
			System.err.println("ERROR - NO FILES SELECTED!");
		}
	}

	private void generateAlert(String message, String title, String header, Alert.AlertType type) {
		Alert alert = new Alert(type, message);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.showAndWait();
	}

	/**
	 * @author Evan Heinrich heinriche@msoe.edu
	 * Calls the individual export methods for the
	 * GTFS data structure
	 */
	protected void exportData() {

		// Determine if data is loaded
		if(gtfs.isLoaded()) {
			// Create a new filechooser set to the current users home directory
			DirectoryChooser chooser = new DirectoryChooser();
			chooser.setInitialDirectory(new File(System.getProperty("user.home")));
			chooser.setTitle("Output Directory");
			File output = chooser.showDialog(null);

			// Null check
			if(output != null) {

				// Directory and empty check
				if(output.isDirectory() && output.length() == 0 && gtfs.isLoaded()) {
					try {
						Exporter.populateDir(output.toPath());

						try {
							// Export data
							exportTrips();
							exportStopTimes();
							exportStops();
							exportRoutes();

							// Confirmation alert showing that data exported alright
							Alert alert = new Alert((Alert.AlertType.CONFIRMATION));
							alert.setTitle("Export Successful");
							alert.setHeaderText("Export Successful");
							alert.setContentText("Export of current dataset successful");
							alert.showAndWait();
						} catch (IOException e) {
							Alert alert = new Alert(Alert.AlertType.ERROR);
							alert.setTitle("Error");
							alert.setContentText("Error exporting data");
							alert.setHeaderText("Error");
							alert.showAndWait();
						}

					} catch (IOException e) {
						throw new IllegalStateException("This should not happen, this method includes null checking");
					}

				} else {
					// Configure an error alert to inform the user of bad directory
					// choice
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("Error");
					alert.setContentText("The specified directory is not empty, please select a new directory");
					alert.setHeaderText("Directory not empty");
					alert.showAndWait();
				}

			}
		}


	}

	/**
	 * Attempts to export the routes data
	 * @throws IOException
	 */
	private void exportRoutes() throws IOException{

		// Get an iterator based on all of the keys for the current routes
		Iterator<String> iterator = gtfs.getRoutes().keys().asIterator();

		// While the iterator isn't empty
		while(iterator.hasNext()) {

			// Retrieve the route with the associated iterator key
			Route route = gtfs.getRoutes().get(iterator.next());

			// Export it
			Exporter.exportRoute(route);
		}

		// Close this exporter stream after all data is exported
		Exporter.close();
	}

	/**
	 * Attempts to export the stops data
	 * @throws IOException
	 */
	private void exportStops() throws IOException{

		// Create an iterator based on the keys for the stops hash table
		Iterator<String> iterator = gtfs.getStops().keys().asIterator();

		// While said iterator isn't empty
		while(iterator.hasNext()) {

			// Load the stop associated with the current key
			Stop stop = gtfs.getStops().get(iterator.next());

			// Export it
			Exporter.exportStop(stop);
		}

		// Close this exporter stream after all data is exported
		Exporter.close();
	}

	/**
	 * Attempts to export the trips data
	 * @throws IOException
	 */
	private void exportTrips() throws IOException{

		// Get an iterator of the associated keys for the trips hash table
		Iterator<String> iterator = gtfs.getTrips().keys().asIterator();

		// While the iterator isn't empty
		while(iterator.hasNext()) {

			// Retrieve the next trip in the dataset
			Trip trip = gtfs.getTrips().get(iterator.next());

			// Export it
			Exporter.exportTrip(trip);
		}

		// Close this exporter stream after all data is exported
		Exporter.close();
	}

	/**
	 * Attempts to export the stopTime data
	 * @throws IOException
	 */
	private void exportStopTimes() throws IOException{

		// OK this method gets a bit messy since stop times uses a two-dimensional hash table
		// because not all fields in stop times are unique

		// Get an iterator of the first dimension
		Iterator<String> iterator1 = gtfs.getStopTimes().keys().asIterator();

		// While the iterator isn't empty
		while(iterator1.hasNext()) {

			// Load the next entry which is another hash table
			Hashtable<String, StopTime> curTable = gtfs.getStopTimes().get(iterator1.next());

			// Create an iterator for the second dimension table
			Iterator<String> iterator2 = curTable.keys().asIterator();

			// While the second dimension iterator isnt empty
			while(iterator2.hasNext()) {

				// Load the current stop time
				StopTime stopTime = curTable.get(iterator2.next());

				// Export it
				Exporter.exportStopTime(stopTime);

			}
		}

		// Close this exporter stream after all data is exported
		Exporter.close();
	}

	/**
	 * Attempts to find a route as well as all the stops along said route
	 */
	protected void onSearchRoute() {

		// Determine if data was loaded
		if(!gtfs.isLoaded()) {
			generateAlert("No data currently loaded", "Error", "No data", Alert.AlertType.ERROR);
		} else {

			// Locally store user input
			String input = searchRoutes.getText();

			// Attempt to get the route associated with the user input
			Route route = gtfs.getRoute(input);

			// Null check (null if route not found)
			if(route != null) {

				// Print the found route and concatenate a header
				String display = route.toString().concat("\n\nStops Along This Route:\n");

				// Get all of the stops along that route
				ArrayList<String> foundStops = Search.stopsViaRoutes(input);

				// Iterate through the found stops
				for(String s : foundStops) {
					// Append to the string that will be displayed, along with a newline
					String temp = s.concat("\n");
					display = display.concat(temp);
				}

				// Update the display
				routesArea.setText(display);

			} else {
				routesArea.setText("Route not found");
			}
		}
	}

	/**
	 * Attempts to find a stop and all routes containing said stop
	 */
	protected void onSearchStop() {

		// Determine if data is loaded
		if(!gtfs.isLoaded()) {
			generateAlert("No data currently loaded", "Error", "No data", Alert.AlertType.ERROR);
		} else {

			// Locally store user input
			String input = searchStops.getText();

			// Attempt to get a stop from user input
			Stop stop = gtfs.getStop(input);

			// Null check (null = stop not found)
			if(stop != null) {

				// Print the stop info and header for all found routes
				String display = stop.toString().concat("\n\nRoutes Including This Stop:\n");

				// Get all the routes containing this stop
				ArrayList<String> foundRoutes = Search.routesViaStops(input);

				// Loop through the found routes
				for(String s : foundRoutes) {

					// Concatenate to the string being displayed
					String temp = s.concat("\n");
					display = display.concat(temp);
				}

				// Display the string
				stopsArea.setText(display);

			} else {
				stopsArea.setText("Stop not found");
			}
		}
	}

	/**
	 * Attempts to find trips associated with a specified stop
	 */
	protected void onSearchTripByStop() {

		// Check if data is loaded
		if(!gtfs.isLoaded()) {
			generateAlert("No data currently loaded", "Error", "No data", Alert.AlertType.ERROR);
		} else {

			// Locally store user input
			String input = searchTripByStop.getText();

			// Get the specified stop from user input
			Stop stop = gtfs.getStop(input);

			// Null check (null = stop not found)
			if(stop != null) {

				// Display the stop info
				String display = stop.toString().concat("\n\nTrips Including This Stop:\n");

				// Get the one trip associated with said stop
				String foundTrips = Search.tripsViaStops(input);

				// Concatenate it to the display string
				String temp = foundTrips.concat("\n");
				display = display.concat(temp);

				// Print to the display
				tripsByStopsArea.setText(display);

			} else {
				tripsByStopsArea.setText("Stop not found");
			}
		}
	}

	/**
	 * Attempts to find all trips comprising a single route
	 */
	protected void onSearchTripByRoute() {

		// Check if data is loaded
		if(!gtfs.isLoaded()) {
			generateAlert("No data currently loaded", "Error", "No data", Alert.AlertType.ERROR);
		} else {

			// Locally store user input
			String input = searchTripByRoute.getText();

			// Attempt to get a route from the user input
			Route route = gtfs.getRoute(input);

			// Null check (null = not found)
			if(route != null) {

				// Print the route info and a header
				String display = route.toString().concat("\n\nTrips Along This Route:\n");

				// Get all the trips comprising said route
				ArrayList<String> foundTrips = Search.tripsViaRoutes(input);

				// Loop through all found trips
				for(String s : foundTrips) {

					// Concatenate to display string
					String temp = s.concat("\n");
					display = display.concat(temp);
				}

				// Display
				tripsByRouteArea.setText(display);

			} else {
				tripsByRouteArea.setText("Route not found");
			}
		}
	}

	protected void openEditor() {
		if(!editStage.isShowing()){
			editStage.close();
			open = false;
		}
		if (!open) {
			//shows the display
			editStage.show();
			open = true;
		} else {
			editStage.hide();
			open = false;
		}
	}
}