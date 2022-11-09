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

import java.util.Objects;

/**
 * Stops for vehicles to stop at
 * @author Evan Heinrich, Cody Mikula, and Nathan Kitzman
 * @version 1.1
 */
public final class Stop {
    private String stop_id;
    private String stop_name;
    private double stop_lat, stop_lon;

    /**
     * The constructor to make a Stop
     * @param stop_id the ID of the stop
     * @param stop_name the name of the stop
     * @param stop_lat the latitude value of the stop
     * @param stop_lon the longitude value of the stop
     */
    public Stop(String stop_id, String stop_name, double stop_lat, double stop_lon) {
        this.stop_lat = stop_lat;
        this.stop_lon = stop_lon;
        this.stop_id = stop_id;
        this.stop_name = stop_name;
    }

    public double getStop_lat() {
        return stop_lat;
    }

    public double getStop_lon() {
        return stop_lon;
    }

    public String getStop_id() {
        return stop_id;
    }

    public String getStop_name() {
        return stop_name;
    }

    public void setStop_id(String stop_id) {
        this.stop_id = stop_id;
    }

    public void setStop_lat(double stop_lat) {
        this.stop_lat = stop_lat;
    }

    public void setStop_lon(double stop_lon) {
        this.stop_lon = stop_lon;
    }

    public void setStop_name(String stop_name) {
        this.stop_name = stop_name;
    }


    @Override
    public String toString() {

        return String.format("Stop ID: %s,\nStop Name: %s,\nLat: %.4f,\nLon: %.4f", stop_id, stop_name,  stop_lat, stop_lon);
    }

    @Override
    public boolean equals(Object that) {
        boolean equality = false;

        if(that instanceof Stop) {
            if(this.stop_id.equals(((Stop) that).stop_id) && this.stop_name.equals(((Stop) that).stop_name)) {
                equality = true;
            }
        }

        return equality;
    }

}
