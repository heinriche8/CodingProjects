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
 * routes that vehicles go along
 * @author Evan Heinrich, Cody Mikula, and Nathan Kitzman
 * @version 1.1
 */
public final class Route {

    private String route_id;
    private String agency_id;
    private String route_name_short;
    private String route_name_long;
    //private String route_desc; Pending Deletion
    private int route_type;


    public Route(String route_id, String route_name_long, int route_type) {
        this(route_id, null, null, route_name_long, null, route_type);
    }

    /**
     * The constructor for a Route
     * @param route_id the ID of this route
     * @param agency_id the ID of the agency for this route
     * @param route_name_short the abstract identifier for this route
     * @param route_name_long the long descriptive name for this route
     * @param route_type the type of route that this is ex bus,ferry,... ect
     */
    public Route(String route_id, String agency_id, String route_name_short,
                 String route_name_long, int route_type) {
        this(route_id, agency_id, route_name_short, route_name_long, null, route_type);
    }

    public Route(String route_id, String agency_id, String route_name_short, String route_name_long, String route_desc, int route_type) {
        this.route_id = route_id;
        this.agency_id = agency_id;
        this.route_name_short = route_name_short;
        this.route_name_long = route_name_long;
        //this.route_desc = route_desc; Pending Deletion
        this.route_type = route_type;
    }


    public String getRoute_id() {
        return route_id;
    }

    public String getAgency_id() {
        return agency_id;
    }

    public String getRoute_name_short() {
        return route_name_short;
    }

    public String getRoute_name_long() {
        return route_name_long;
    }

    /*public String getRoute_desc() {
        return route_desc;
    } Pending Deletion */

    public int getRoute_type() {
        return route_type;
    }

    public void setRoute_id(String route_id) {
        this.route_id = route_id;
    }

    public void setAgency_id(String agency_id) {
        this.agency_id = agency_id;
    }

    public void setRoute_name_short(String route_name_short) {
        this.route_name_short = route_name_short;
    }

    public void setRoute_name_long(String route_name_long) {
        this.route_name_long = route_name_long;
    }

    /*public void setRoute_desc(String route_desc) {
        this.route_desc = route_desc;
    }Pending Deletion*/

    public void setRoute_type(int route_type) {
        this.route_type = route_type;
    }

    @Override
    public String toString() {
        return String.format("Route ID: %s,\nAgency: %s,\nShort Name: %s,\nLong Name: %s,\nRoute Type: %d", route_id, agency_id, route_name_short, route_name_long, route_type);
    }

    @Override
    public boolean equals(Object that) {
        boolean result = false;

        // First, make sure that is a Route
        if(that instanceof Route) {

            // Route ID and type are the only two required field
            if(this.route_id.equals(((Route) that).route_id) && this.route_type == ((Route) that).route_type) {
                result = true;
            }
        }

        return result;
    }
}
