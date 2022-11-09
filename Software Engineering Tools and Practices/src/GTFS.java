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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * list of observers
 * @author Aidan Holcombe, Nathan Kitzman, Cody Mikula, Ryan Beatty, and Evan Heinrich
 * @version 1.1
 */
public class GTFS implements Subject {
	private static GTFS gtfs = null;
	//Key for each route is its route_id
	private Hashtable<String, Route> routes;

	// Key for each stop is its stop_id
	private Hashtable<String, Stop> stops;

	// First key is the tripID, and then the key in the second hashtable is the stopID
	// Example: stopTimes.get("some tripID").get("some stopID");
	private Hashtable<String, Hashtable<String, StopTime>> stopTimes;

	//Key for each trip is its trip_id
	private Hashtable<String, Trip> trips;

	private List<Observer> observers;

	ArrayList<String> lines;

    /**
	 * Default constructor
	 */
	private GTFS(){
		routes = new Hashtable<>();
		stops = new Hashtable<>();
		stopTimes = new Hashtable<>();
		trips = new Hashtable<>();
		observers = new ArrayList<>();
	}


	/**
	 * Attaches the observer
	 * @param observer the observer to attach
	 */
	public void attach(Observer observer){
		observers.add(observer);
	}

	/**
	 * detaches a specific observer
	 * @param observer the observer to be detached
	 */
	public void detach(Observer observer){
		observers.remove(observer);
	}

	public void notifyObservers(){
		for(Observer o: observers) {
			o.update();
		}
	}

	public void addRoute(String routeID, Route route)  {
		routes.put(routeID, route);
	}

	public void addStop(String stopID, Stop stop) {
		stops.put(stopID, stop);
	}

	public void addStopTime(String tripID, String stopID, StopTime stopTime) {
		if(stopTimes.get(tripID) == null) {
			stopTimes.put(tripID, new Hashtable<>());
			stopTimes.get(tripID).put(stopID, stopTime);
		} else {
			stopTimes.get(tripID).put(stopID, stopTime);
		}
	}

	public void addTrip(String tripID, Trip trip) {
		trips.put(tripID, trip);
	}

	public Trip getTrip(String trip_id) {
		return trips.get(trip_id);
	}

	/*
	Added for testing purposes, I dont know if this should be permanent
	*/
	public Stop getStop(String stop_ID) {
		return stops.get(stop_ID);
	}

	/*
    Added for testing purposes, I dont know if this should be permanent
    */
	public Route getRoute(String route_ID) {
		return routes.get(route_ID);
	}

	/*
	Added for testing purposes, I dont know if this should be permanent
	 */
	public StopTime getStopTime(String tripID, String stopID) {
		if(stopTimes.get(tripID) != null) {
			return stopTimes.get(tripID).get(stopID);
		}
		return null;
	}

	public Hashtable<String, Trip> getTrips(){
		return trips;
	}

	public Hashtable<String, Route> getRoutes(){
		return routes;
	}

	public Hashtable<String, Hashtable<String, StopTime>> getStopTimes(){
		return stopTimes;
	}

	public Hashtable<String, Stop> getStops(){
		return stops;
	}

	public void clearTrips() {
		trips.clear();
	}

	public void clearRoutes() {
		routes.clear();
	}

	public void clearStops() {
		stops.clear();
	}

	public void clearStopTimes() {
		stopTimes.clear();
	}

	public boolean isLoaded() {
		return trips.size() != 0 && stopTimes.size() != 0 && stops.size() != 0 && routes.size() != 0;
	}

	public static GTFS getInstance(){
		if(gtfs == null){
			gtfs = new GTFS();
		}
		return gtfs;
	}

	/**
	 *
	 * @param file the file from the gui
	 * @throws IllegalArgumentException if the file is null
	 * @throws IOException
	 * @throws IndexOutOfBoundsException if an invalid index is accessed
	 * @throws NumberFormatException if the route type cannot be parsed as an integer
	 */
	public boolean loadRoute(File file) throws IOException {
		if (file == null) {
			throw new IllegalArgumentException("Invalid file received.");
		}
		gtfs.clearRoutes();
		lines = new ArrayList<>(Files.lines(file.toPath()).toList());


		/**
		 *Iterate all the readers by one in order to start on the data portion
		 */

		addRoute(lines);

		return true;
	}

	/**
	 *
	 * @param file
	 * @return
	 * @throws IOException
	 * @throws IllegalArgumentException if the file is null
	 * @throws IndexOutOfBoundsException if an invalid index is accessed
	 * @throws NumberFormatException if the stop id or stop sequence cannot be parsed as an
	 */
	public boolean loadStopTime(File file) throws IOException {
		if (file == null) {
			throw new IllegalArgumentException("Invalid file received.");
		}
		gtfs.clearStopTimes();
		lines = new ArrayList<>(Files.lines(file.toPath()).toList());
		addStopTime(lines);


		return true;


	}

	/**
	 *
	 * @param file
	 * @return
	 * @throws IOException
	 * @throws IllegalArgumentException if the file is null
	 * @throws IndexOutOfBoundsException if an invalid index is accessed
	 * @throws NumberFormatException if the stop id cannot be parsed as an integer, or if the
	 * stop longitude or stop latitude cannot be parsed as a double
	 */
	public boolean loadStop(File file) throws IOException {
		if (file == null) {
			throw new IllegalArgumentException("Invalid file received.");
		}
		gtfs.clearStops();
		lines = new ArrayList<>(Files.lines(file.toPath()).toList());
		addStop(lines);


		return true;
	}

	/**
	 *
	 * @param file
	 * @return
	 * @throws IOException
	 * @throws IllegalArgumentException if the file is null
	 * @throws IndexOutOfBoundsException if an invalid index is accessed
	 */
	public boolean loadTrip(File file) throws IOException {
		if (file == null) {
			throw new IllegalArgumentException("Invalid file received.");
		}
		gtfs.clearTrips();
		lines = new ArrayList<>(Files.lines(file.toPath()).toList());
		addTrip(lines);

		return true;
	}

	/*
	Evan will test
	 */
	/**
	 *
	 * @param lines
	 * @throws IndexOutOfBoundsException if an invalid index is accessed
	 * @throws NumberFormatException if the route type cannot be parsed as an integer
	 */
	private void addRoute(ArrayList<String> lines) throws IndexOutOfBoundsException,
			NumberFormatException {
		int route_name_index = 0;
		int route_id_index = 0;
		int route_type_index = 0;
		int agency_id_index = 0;
		int route_short_name_index = 0;
		String[] order = lines.get(0).split(",");
		for(int i = 0; i < order.length; i++){
			if(order[i].equals("route_id")){
				route_id_index = i;
			}
			if(order[i].equals("route_long_name")){
				route_name_index = i;
			}
			if(order[i].equals("route_type")){
				route_type_index = i;
			}
			if(order[i].equals("agency_id")) {
				agency_id_index = i;
			}
			if(order[i].equals("route_short_name")) {
				route_short_name_index = i;
			}
		}
		String[] array;
		String route_name;
		String route_id;
		int route_type;
		String agency_id;
		String route_short_name;
		for(int i = 1; i < lines.size(); i++) {
			array = lines.get(i).split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
			route_id = array[route_id_index];
			route_name = array[route_name_index];
			route_type = Integer.parseInt(array[route_type_index]);
			agency_id = array[agency_id_index];
			route_short_name = array[route_short_name_index];
			gtfs.addRoute(route_id, new Route(route_id, agency_id, route_short_name, route_name,
					route_type));
		}
	}

	/**
	 *
	 * @param lines
	 * @throws IndexOutOfBoundsException if an invalid index is accessed
	 * @throws NumberFormatException if the stop id or stop sequence cannot be parsed as an
	 * integer
	 */
	private void addStopTime(ArrayList<String> lines) throws IndexOutOfBoundsException,
			NumberFormatException {
		int trip_id_index = 0;
		int arrival_time_index = 0;
		int departure_time_index = 0;
		int stop_id_index = 0;
		int stop_sequence_index = 0;
		String[] order = lines.get(0).split(",");
		for(int i = 0; i < order.length; i++){
			if(order[i].equals("trip_id")){
				trip_id_index = i;
			}
			if(order[i].equals("arrival_time")){
				arrival_time_index = i;
			}
			if(order[i].equals("departure_time")){
				departure_time_index = i;
			}
			if(order[i].equals("stop_id")){
				stop_id_index = i;
			}
			if(order[i].equals("stop_sequence")){
				stop_sequence_index = i;
			}
		}
		String[] array;
		String trip_id;
		String arrival_time;
		String departure_time;
		String stop_id;
		int stop_sequence;
		for(int i = 1; i < lines.size(); i++) {
			array = lines.get(i).split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
			trip_id = array[trip_id_index];
			arrival_time = array[arrival_time_index];
			departure_time = array[departure_time_index];
			stop_id = array[stop_id_index];
			stop_sequence = Integer.parseInt(array[stop_sequence_index]);
			gtfs.addStopTime(trip_id,stop_id, new StopTime(trip_id,arrival_time,departure_time,stop_id,stop_sequence));
		}
	}


	/*
	Evan will test
	 */
	/**
	 *
	 * @param lines
	 * @throws IndexOutOfBoundsException if an invalid index is accessed
	 * @throws NumberFormatException if the stop id cannot be parsed as an integer, or if the
	 * stop longitude or stop latitude cannot be parsed as a double
	 */
	private void addStop(ArrayList<String> lines) throws IndexOutOfBoundsException,
			NumberFormatException {
		int stop_name_index = 0;
		int stop_id_index = 0;
		int stop_lon_index = 0;
		int stop_lat_index = 0;
		String[] order = lines.get(0).split(",");
		for(int i = 0; i < order.length; i++){
			if(order[i].equals("stop_name")){
				stop_name_index = i;
			}
			if(order[i].equals("stop_id")){
				stop_id_index = i;
			}
			if(order[i].equals("stop_lon")){
				stop_lon_index = i;
			}
			if(order[i].equals("stop_lat")){
				stop_lat_index = i;
			}
		}
		String[] array;
		String stop_name;
		String stop_id;
		double stop_lon;
		double stop_lat;
		for(int i = 1; i < lines.size(); i++) {
			array = lines.get(i).split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
			stop_name = array[stop_name_index];
			stop_id = array[stop_id_index];
			stop_lon = Double.parseDouble(array[stop_lon_index]);
			stop_lat = 	Double.parseDouble(array[stop_lat_index]);
			gtfs.addStop(stop_id, new Stop(stop_id, stop_name,  stop_lat, stop_lon));
		}
	}

	/**
	 *
	 * @param lines
	 * @throws IndexOutOfBoundsException if an invalid index is accessed
	 */
	private void addTrip(ArrayList<String> lines) throws IndexOutOfBoundsException {
		int route_id_index = 0;
		int service_id_index = 0;
		int trip_id_index = 0;
		String[] order = lines.get(0).split(",");
		for(int i = 0; i < order.length; i++){
			if(order[i].equals("route_id")){
				route_id_index = i;
			}
			if(order[i].equals("service_id")){
				service_id_index = i;
			}
			if(order[i].equals("trip_id")){
				trip_id_index = i;
			}
		}
		String[] array;
		String route_id;
		String service_id;
		String trip_id;
		for(int i = 1; i < lines.size(); i++) {
			array = lines.get(i).split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
			route_id = array[route_id_index];
			service_id = array[service_id_index];
			trip_id = array[trip_id_index];
			gtfs.addTrip(trip_id, new Trip(route_id, service_id, trip_id));
		}
	}
}