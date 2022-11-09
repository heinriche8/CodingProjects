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
import java.io.PrintWriter;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Path;
import java.nio.file.Files;

public class Exporter {
    private static PrintWriter out;
    private static File Routes;
    private static File StopTimes;
    private static File Stops;
    private static File Trips;

    private static final String ROUTE_HEADER = "route_id,agency_id,route_short_name,route_type";
    private static final String STOP_TIME_HEADER = "trip_id,arrival_time,departure_time,stop_id,stop_sequence";
    private static final String STOP_HEADER = "stop_id,stop_name,stop_lat,stop_lon";
    private static final String TRIP_HEADER = "route_id,service_id,trip_id";

    /**
     * Populates a specified directory with all the
     * required GTFS text files.
     * @param path Path to the empty output folder
     * @throws FileAlreadyExistsException if one or more of the GTFS files already exists in the specified path
     * @throws IOException if the specified path does not exist
     * @throws IllegalArgumentException if the path is null
     */
    public static void populateDir(Path path) throws IOException {
        if (path == null) {
            throw new IllegalArgumentException("Invalid path received.");
        }

        // For all of these calls, creates
        Routes = Files.createFile(new File(path.toAbsolutePath().toString().concat("\\routes.txt")).toPath()).toFile();
        StopTimes = Files.createFile(new File(path.toAbsolutePath().toString().concat("\\stop_times.txt")).toPath()).toFile();
        Stops = Files.createFile(new File(path.toAbsolutePath().toString().concat("\\stops.txt")).toPath()).toFile();
        Trips = Files.createFile(new File(path.toAbsolutePath().toString().concat("\\trips.txt")).toPath()).toFile();
    }

    /**
     * Appends the specified stop information to the currently
     * selected file. If no file is selected by the output stream,
     * it will then bind to the routes.txt file
     * @param route the route to be appended
     * @throws IOException if the currently bound file cannot be accessed
     * @throws IllegalArgumentException if the route is null
     */
    public static void exportRoute(Route route) throws IOException {
        if (route == null) {
            throw new IllegalArgumentException("Invalid route selected.");
        }

        // If the output stream isn't bound to anything, bind it to the Routes file
        // and append the header.
        if(out == null) {
            out = new PrintWriter(Routes);
            out.println(ROUTE_HEADER);
        }

        out.println(String.format("%s,%s,%s,%d",route.getRoute_id(),route.getAgency_id(),route.getRoute_name_short(),route.getRoute_type()));
    }

    /**
     * Appends the specified stop information to the currently
     * selected file. If no file is selected by the output stream,
     * it will then bind to the stops.txt file
     * @param stop the stop to be appended
     * @throws IOException if the currently bound file cannot be accessed
     * @throws IllegalArgumentException if the stop is null
     */
    public static void exportStop(Stop stop) throws IOException {
        if (stop == null) {
            throw new IllegalArgumentException("Invalid stop selected.");
        }

        // If the output stream isn't bound to anything, bind it to the Stops file
        // and append the header.
       if(out == null) {
           out = new PrintWriter(Stops);
           out.println(STOP_HEADER);
       }

       out.println(String.format("%s,%s,%f,%f",stop.getStop_id(),stop.getStop_name(),stop.getStop_lat(),stop.getStop_lon()));
    }

    /**
     * Appends the specified trip information to the currently
     * selected file. If no file is selected by the output stream,
     * it will then bind to the trips.txt file.
     * @param trip the trip to the appended
     * @throws IOException if the currently bound file cannot be accessed
     * @throws IllegalArgumentException if the trip is null
     */
    public static void exportTrip(Trip trip) throws IOException {
        if (trip == null) {
            throw new IllegalArgumentException("Invalid trip selected.");
        }

        // If the output stream isn't bound to anything, bind it to the Trips file
        // and append the header.
        if(out == null) {
            out = new PrintWriter(Trips);
            out.println(TRIP_HEADER);
        }

        out.println(String.format("%s,%s,%s",trip.getRoute_id(), trip.getService_id(), trip.getTrip_id()));
    }

    /**
     * Appends the specified stop_time information to the
     * currently selected file. If no file is selected by the
     * output stream, it will then bind to the stop_times.txt file
     * @param stopTime the stop time to be appended
     * @throws IOException if the currently bound file cannot be accessed
     * @throws IllegalArgumentException if the stopTime is null
     */
    public static void exportStopTime(StopTime stopTime) throws IOException {
        if (stopTime == null) {
            throw new IllegalArgumentException("Invalid stop time selected.");
        }

        // If the output stream isn't bound to anything, bind it to the StopTimes file
        // and append the header.
        if(out == null) {
            out = new PrintWriter(StopTimes);
            out.println(STOP_TIME_HEADER);
        }

        out.println(String.format("%s,%s,%s,%s,%d",stopTime.getTrip_id(),stopTime.getArrival_time(),stopTime.getDeparture_time(),stopTime.getStop_id(),stopTime.getStop_sequence()));
    }

    /**
     * Flushes and closes the current output stream, then sets
     * it to null.
     * @throws IllegalStateException if the output stream was already closed
     */
    public static void close() throws IllegalStateException{
        if(out == null) {
            throw new IllegalStateException("Output stream already closed");
        }

        out.flush();
        out.close();
        out = null;
    }

    /**
     * Resets all aspects of this library, setting
     * the output files to null and closing the
     * output stream if it wasn't already
     */
    public static void reset() {
        // Close the output stream if it wasn't closed already
        if(out != null) {
            close();
        }

        // Clear the output files
        Routes = null;
        Stops = null;
        StopTimes = null;
        Trips = null;
    }


}
