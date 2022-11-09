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
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Ryan Beatty, Nathan Kitzman
 * @version 1
 */

/**
 * This class holds static methods for the user to search for a certain element via a certain ID
 */
public class Search implements Observer {
    //variable set up
    private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static LocalDateTime now;
    private static Hashtable<String, Trip> trips;
    private static Hashtable<String, Hashtable<String, StopTime>> stopTimes;
    private GTFS data = GTFS.getInstance();

    /**
     * Searches for all routes along a stop via the StopID
     * @param stop_id the ID of the stop to search
     * @return the list of routes
     */
    public static ArrayList<String> routesViaStops(String stop_id){
        ArrayList<String> route_ids = new ArrayList<>();
        Iterator<String> tripIterator = trips.keys().asIterator();
        while (tripIterator.hasNext()){
            String tripID = tripIterator.next();
            Iterator<String> stopIDIterator = stopTimes.get(tripID).keys().asIterator();
            while(stopIDIterator.hasNext()){
                String stopID = stopIDIterator.next();
                if(stopID.equals(stop_id)){
                    if(!route_ids.contains(trips.get(tripID).getRoute_id())){
                        route_ids.add(trips.get(tripID).getRoute_id());
                    }
                }
            }
        }
        return route_ids;
    }

    /**
     * Gets all the stops along a route given the route ID
     * @param route_id the ID of the route to search
     * @return the list of stops
     */
    public static ArrayList<String> stopsViaRoutes(String route_id){
        ArrayList<String> stop_ids = new ArrayList<>();
        Iterator<String> iterator = trips.keys().asIterator();

        while (iterator.hasNext()){
            String tripID = iterator.next();
            String stop_id;
            if(trips.get(tripID).getRoute_id().equals(route_id)){
                Iterator<String> stopIDIterator = stopTimes.get(tripID).keys().asIterator();
                while(stopIDIterator.hasNext()) {
                    stop_id = stopIDIterator.next();
                    if(!stop_ids.contains(stop_id)) {
                        stop_ids.add(stop_id);
                    }
                }
            }
        }

        return stop_ids;
    }

    /**
     * Gets all the future trips along a route given the route ID
     * @param route_id the ID of the route to search
     * @return the list of trips
     */
    public static ArrayList<String> tripsViaRoutes(String route_id){
        now = LocalDateTime.now();
        String time = dtf.format(now);
        Iterator<String> iterator = trips.keys().asIterator();
        ArrayList<String> trip_ids = new ArrayList<>();
        while(iterator.hasNext()){
            String tripID = iterator.next();
            if(trips.get(tripID).getRoute_id().equals(route_id)){
                Iterator<String> stopIDIterator = stopTimes.get(tripID).keys().asIterator();
                while(stopIDIterator.hasNext()){
                    String stopID = stopIDIterator.next();
                    if(Calculate.timeToSeconds(stopTimes.get(tripID).get(stopID).getArrival_time()) > Calculate.timeToSeconds("24:00:00")){
                        if(Calculate.timeToSeconds(stopTimes.get(tripID).get(stopID).getArrival_time()) > (Calculate.timeToSeconds(time)+Calculate.timeToSeconds("24:00:00"))){
                            trip_ids.add(tripID);
                        }
                    }
                    if(Calculate.timeToSeconds(stopTimes.get(tripID).get(stopID).getArrival_time()) > Calculate.timeToSeconds(time)){
                        if(!trip_ids.contains(tripID)) {
                            trip_ids.add(tripID);
                        }
                    }
                }
            }
        }

        return trip_ids;
    }

    /**
     * Gets the next trip with the systems time via a stop ID
     * @param stop_id the ID of the stop to search
     * @return the trip ID
     */
    public static String tripsViaStops(String stop_id) {
        now = LocalDateTime.now();
        String currentTime;
        double oldTime = Double.MAX_VALUE;
        String trip = "not found";
        Iterator<String> tripIterator = stopTimes.keys().asIterator();


        while (tripIterator.hasNext()){
            String tripID = tripIterator.next();
            Iterator<String> stopIDIterator = stopTimes.get(tripID).keys().asIterator();
            while(stopIDIterator.hasNext()){
                String stopID = stopIDIterator.next();
                if(stopID.equals(stop_id)){
                    currentTime = dtf.format(now);
                    if(Calculate.timeToSeconds(stopTimes.get(tripID).get(stopID).getArrival_time()) > Calculate.timeToSeconds(currentTime) && oldTime > Calculate.timeToSeconds(stopTimes.get(tripID).get(stopID).getArrival_time())) {
                        oldTime = Calculate.timeToSeconds(stopTimes.get(tripID).get(stopID).getArrival_time());
                        trip = tripID;
                    }
                }
            }
        }
        return trip;
    }

    @Override
    public void update() {
        trips = data.getTrips();
        stopTimes = data.getStopTimes();
    }
}
