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

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Times a vehicle arrives at and departs from a stop for a given trip
 * @author Evan Heinrich, Cody Mikula, and Nathan Kitzman
 * @version 1.1
 */
public final class StopTime {
    private String trip_id;
    private String arrival_time;
    private String departure_time;
    private String stop_id;
    private int stop_sequence;

    /**
     * The constructor to make a stopTime
     * @param trip_id the ID of the trip that the stop time is along
     * @param arrival_time the time that the vehicle arrives at said stop
     * @param departure_time the time that the vehicle leaves at said stop
     * @param stop_id the ID of the stop
     * @param stop_sequence The order which the trip arrives at the stop
     */
    public StopTime(String trip_id, String arrival_time, String departure_time, String stop_id, int stop_sequence) {
        this.arrival_time = arrival_time;
        this.stop_id = stop_id;
        this.departure_time = departure_time;
        this.stop_sequence = stop_sequence;
        this.trip_id = trip_id;
    }

    public String getTrip_id() {
        return trip_id;
    }

    public void setTrip_id(String trip_id) {
        this.trip_id = trip_id;
    }

    public String getArrival_time() {
        return arrival_time;
    }

    public void setArrival_time(String arrival_time) {
        this.arrival_time = arrival_time;
    }

    public String getDeparture_time() {
        return departure_time;
    }

    public void setDeparture_time(String departure_time) {
        this.departure_time = departure_time;
    }

    public String getStop_id() {
        return stop_id;
    }

    public void setStop_id(String stop_id) {
        this.stop_id = stop_id;
    }

    public int getStop_sequence() {
        return stop_sequence;
    }

    public void setStop_sequence(int stop_sequence) {
        this.stop_sequence = stop_sequence;
    }

    /**
     * Constructs a textual representation of the arrival and departure time for a stop, along a
     * given trip
     * @return String representation of the arrival and departure time for a stop, along a given
     * trip
     */
    @Override
    public String toString() {
        return String.format("Trip ID: %s,\nStop ID: %s,\narrival time: %s,\ndeparture time: %s,\nstop sequence: %s", trip_id, stop_id, arrival_time, departure_time, stop_sequence);
    }

    @Override
    public boolean equals(Object that) {
        boolean equality = false;

        if(that instanceof StopTime) {
            if(this.trip_id.equals(((StopTime) that).trip_id) && this.stop_id.equals(((StopTime) that).stop_id) && this.stop_sequence == ((StopTime) that).stop_sequence) {
                equality = true;
            }
        }

        return equality;
    }
}
