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

import java.util.Hashtable;

/**
 * Class for editing files. Enter null as a value if you want it to be ignored. If
 * the parameter is a primitive, enter a special value defined by the method to have it be ignored.
 * @author Aidan Holcombe
 */
public class Editor implements Observer {

    GTFS subject = GTFS.getInstance();

    public Editor() {
        this.subject.attach(this);
    }

    /**
     * Updates a specific stopTime. enter null in the string parameters if you do not want that parameter to be updated.
     * Enter -1 for newStopSequence if you do not want it updated. the combination of oldTripID and oldStopID must be a
     * combination that is already in the database. Whatever set of oldTripID, oldStopID, newTripID, or newStopID is
     * inputted must not already be in the database
     * @param oldTripID the tripID that describes the stopTime you wish to edit
     * @param oldStopID the stopID that describes the stopTime you wish to edit
     * @param newTripID the new tripID of the stopTime you wish to edit
     * @param newStopID the new stopID of the stopTime you wish to edit
     * @param newArrivalTime the new arrival time of the stopTime you wish to edit
     * @param newDepartureTime the new departure time you wish to edit
     * @param newStopSequence the new stop sequence of the stop time (-1 is the value to ignore having it be updated).
     */
    public void updateStopTime(String oldTripID, String oldStopID, String newTripID, String newStopID,
                               String newArrivalTime, String newDepartureTime, int newStopSequence) throws IllegalArgumentException{
        // checks if the set of tripId and stopID is a valid stopTime
        if(subject.getStopTime(oldTripID, oldStopID) == null) {
            throw new IllegalArgumentException("The tripID and stopID does not map to a unique stop time");
        }

        if(newTripID == null && newStopID != null) {
            if(subject.getStopTime(oldTripID, newStopID) != null) {
                throw new IllegalArgumentException("oldTripID and newStopID already have a corresponding stop time");
            }
        } else if(newTripID != null && newStopID == null) {
            if(subject.getStopTime(newTripID, oldTripID) != null) {
                throw new IllegalArgumentException("newTripID and oldStopID already have a corresponding stop time");
            }
        } else if(newTripID != null && newStopID != null) {
            if(subject.getStopTime(newTripID, newStopID) != null) {
                throw new IllegalArgumentException("newTripID and newStopID already have a corresponding stop time");
            }
        }

        // updating based off each parameter
        if(newArrivalTime != null) {
            subject.getStopTime(oldTripID, oldStopID).setArrival_time(newArrivalTime);
        }

        if(newDepartureTime != null) {
            subject.getStopTime(oldTripID, oldStopID).setDeparture_time(newDepartureTime);
        }

        if(newStopSequence != -1) {
            subject.getStopTime(oldTripID, oldStopID).setStop_sequence(newStopSequence);
        }

        // updating the keys and trip & stop ids
        if(newTripID != null && newStopID == null) {
            Hashtable<String, StopTime> stopTimesWithOldTripID = subject.getStopTimes().remove(oldTripID);
            stopTimesWithOldTripID.forEach((stopID, stopTime) -> stopTime.setTrip_id(newTripID));
            subject.getStopTimes().put(newTripID, stopTimesWithOldTripID);
        } else if(newTripID != null && newStopID != null) {
            StopTime stopTime = subject.getStopTimes().get(oldTripID).remove(oldStopID);
            stopTime.setTrip_id(newTripID);
            stopTime.setStop_id(newStopID);
            subject.addStopTime(newTripID, newStopID, stopTime);
        } else if(newTripID == null && newStopID != null) {
            StopTime stopTime = subject.getStopTimes().get(oldTripID).remove(oldStopID);
            stopTime.setStop_id(newStopID);
            subject.addStopTime(oldTripID, newStopID, stopTime);
        }


    }

    /**
     * Updates a specific stop. enter null in the string parameters if you do not want that parameter to be updated.
     * Enter 999 in the double parameters if you do not want them to be updated. oldStopID must be a stopID currently
     * in the database, or else it will throw an exception.
     * @param oldStopID the stop ID of the stop to be edited
     * @param newStopID the potential new stop ID of the stop, cannot be a stopID that is already in the database
     * @param newStopName the potential new stop name of the stop
     * @param newStopLat the potential new stop latitude of the stop
     * @param newStopLon the potential new stop longitude of the stop
     * @throws IllegalArgumentException throws an IllegalArgumentException if the oldStopID was not in the database,
     *  or if the newStopID is already in the database
     */
    public void updateStop(String oldStopID, String newStopID, String newStopName, double newStopLat, double newStopLon) throws IllegalArgumentException{
        // checks if valid stopID
        if(oldStopID == null) {
            throw new IllegalArgumentException("the stopID cannot be null");
        }

        if(subject.getStop(oldStopID) == null) {
            throw new IllegalArgumentException("There is no stop matching that stopID");
        }
        if(newStopID != null) {
            if (subject.getStop(newStopID) != null) {
                throw new IllegalArgumentException("the newStopID already has a corresponding stop");
            }
        }

        // updating based off each parameter
        if(newStopLat != 999) {
            subject.getStop(oldStopID).setStop_lat(newStopLat);
        }

        if(newStopLon != 999) {
            subject.getStop(oldStopID).setStop_lon(newStopLon);
        }

        if(newStopName != null) {
            subject.getStop(oldStopID).setStop_name(newStopName);
        }

        // changes the key that this object corresponds to
        if(newStopID != null) {
            // There's no good way to change the key without removing the object and adding a new one in since
            // keys in java are final, so I have to remove the whole object and replace it with an updated key
            Stop stop = subject.getStops().remove(oldStopID);
            stop.setStop_id(newStopID);
            subject.addStop(newStopID, stop);

            subject.getStopTimes().forEach((tripID, stopIDTable) -> {
                if(stopIDTable.get(oldStopID) != null) {
                    // Same issue here as earlier
                    StopTime stopTime = stopIDTable.remove(oldStopID);
                    stopTime.setStop_id(newStopID);
                    stopIDTable.put(newStopID, stopTime);
                }
            });
        }
    }

    /**
     * Updates a specific route. enter null in the string parameters if you do not want that parameter to be updated.
     * Enter 999 in the int parameter if you do not want them to be updated. oldRouteID must be a routeID currently
     * in the database, or else it will throw an exception.
     * @param oldRouteID the route id of the route that you want edited
     * @param newRouteID the potential new route id, also updates the route portion of trips.txt if the old route
     *                   was part of a trip.
     * @param newAgencyID the potential new agency id
     * @param newRoute_name_short the potential new route short name
     * @param newRoute_name_long the potential new route long name
     * @param newRoute_type the potential new route type
     * @throws IllegalArgumentException throws an IllegalArgumentException if the oldStopID was not in the database,
     *  or if the newStopID is already in the dataBase
     */
    public void updateRoute(String oldRouteID, String newRouteID, String newAgencyID, String newRoute_name_short,
                            String newRoute_name_long, int newRoute_type) throws IllegalArgumentException{
        //check if valid routeID
        if(oldRouteID == null) {
            throw new IllegalArgumentException("the RouteID cannot be null");
        }

        if(subject.getRoute(oldRouteID) == null) {
            throw new IllegalArgumentException("No matching route for the routeID");
        }

        if(newRouteID != null) {
            if (subject.getRoute(newRouteID) != null) {
                throw new IllegalArgumentException("the newRouteID already has a corresponding stop");
            }
        }

        // check if valid route type, has to be between 0 and 12
        if(newRoute_type != 999) {
            if(newRoute_type < 0 || newRoute_type > 12) {
                throw new IllegalArgumentException("the route type is an invalid number, must be between 0-12");
            }
        }

        // updating based off of each parameter
        if(newAgencyID != null) {
            subject.getRoute(oldRouteID).setAgency_id(newAgencyID);
        }

        if(newRoute_name_short != null) {
            subject.getRoute(oldRouteID).setRoute_name_short(newRoute_name_short);
        }

        if(newRoute_name_long != null) {
            subject.getRoute(oldRouteID).setRoute_name_long(newRoute_name_long);
        }

        if(newRoute_type != 999) {
            subject.getRoute(oldRouteID).setRoute_type(newRoute_type);
        }

        // updates the routeID in the route object, and in any trip that contained the previous routeID
        if(newRouteID != null) {
            // changes the routeID in routes
            Route route = subject.getRoutes().remove(oldRouteID);
            route.setRoute_id(newRouteID);
            subject.addRoute(newRouteID, route);

            // changes the routeID in trips
            subject.getTrips().forEach((tripID, trip) -> {
                if(trip.getRoute_id().equals(oldRouteID)) {
                    trip.setRoute_id(newRouteID);
                }
            });
        }
    }

    /**
     * Updates a specific route. enter null in the parameters if you do not want that parameter to be updated.
     * oldTripID must be a tripID currently in the database, or else it will throw an exception.
     *
     * @param oldTripID the route id of the route that you want edited
     * @param newTripID the potential new route id
     * @param newRouteID the potential new agency id
     * @param newServiceID the potential new route short name
     * @throws IllegalArgumentException throws an IllegalArgumentException if the oldTripID was not in the database,
     *  or if the newTripID is already in the dataBase
     */
    public void updateTrip(String oldTripID, String newTripID, String newRouteID, String newServiceID)
            throws IllegalArgumentException{

        // check to make sure all the data passed in is valid
        if(oldTripID == null) {
            throw new IllegalArgumentException("the tripID cannot be null");
        }

        if(subject.getTrip(oldTripID) == null) {
            throw new IllegalArgumentException("No matching trip for the tripID");
        }

        if(newRouteID != null) {
            if (subject.getTrip(newTripID) != null) {
                throw new IllegalArgumentException("the newTripID already has a corresponding stop");
            }
        }

        // updating based off of each parameter
        if(newRouteID != null) {
            subject.getTrip(oldTripID).setRoute_id(newRouteID);
        }

        if(newServiceID != null) {
            subject.getTrip(oldTripID).setService_id(newServiceID);
        }

        // updates the tripID in the trip object, in the key for the hashtable, and in stopTimes
        if(newTripID != null) {
            // This has the same issues as it did in Stops, so look in there to make sense of it
            Trip trip = subject.getTrips().remove(oldTripID);
            trip.setTrip_id(newTripID);
            subject.addTrip(newTripID, trip);

            if(subject.getStopTimes().get(oldTripID) != null) {
                Hashtable<String, StopTime> stopTimes = subject.getStopTimes().remove(oldTripID);
                stopTimes.forEach((stopID, stopTime) -> stopTime.setTrip_id(newTripID));
                subject.getStopTimes().put(newTripID, stopTimes);
            }
        }

    }

    @Override
    public void update() {

    }
}
