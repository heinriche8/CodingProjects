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



/**
 * trips that vehicles can take
 * @author Evan Heinrich, Cody Mikula, and Nathan Kitzman
 * @version 1.1
 */
public final class Trip {
    private String route_id;
    private String service_id;
    private String trip_id;

    /**
     * The constructor to make a Trip
     * @param route_id the ID of the route that the trip follows
     * @param service_id when the trip is available
     * @param trip_id the ID of this trip
     */
    public Trip(String route_id, String service_id, String trip_id) {
        this.route_id = route_id;
        this.service_id = service_id;
        this.trip_id = trip_id;
    }

    public String getRoute_id() {
        return route_id;
    }

    public String getService_id() {
        return service_id;
    }

    public String getTrip_id() {
        return trip_id;
    }

    public void setRoute_id(String route_id) {
        this.route_id = route_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    public void setTrip_id(String trip_id) {
        this.trip_id = trip_id;
    }


    @Override
    public String toString() {
        return String.format("Route ID: %s,\nservice ID: %s,\nTrip ID: %s", route_id, service_id, trip_id);
    }

    @Override
    public boolean equals(Object that) {
        boolean equality = false;

        if(that instanceof Trip) {
            if(this.route_id.equals(((Trip) that).route_id) && this.trip_id.equals(((Trip) that).trip_id) && this.service_id.equals(((Trip) that).service_id)) {
                equality = true;
            }
        }

        return equality;
    }
}
