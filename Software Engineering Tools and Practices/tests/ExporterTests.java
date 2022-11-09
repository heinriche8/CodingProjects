/*
 * Course: SE 2030 021
 * Spring 2021-2022
 * Lab 8 - Defect Tracking and Technical Debt
 * Name: AfternoonGroup3
 * Created: 4/20/2022
 */

import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * Test suite for the Exporter class
 * @author Cody Mikula
 * @version 1.2
 */
public class ExporterTests {
    GTFS gtfs;

    /**
     * Populates the Desktop, Downloads, and a New folder directory with empty GTFS files and
     * confirms the existence of said files at each location
     * @throws IOException if a supplied path does not exist
     */
    @Test
    @Order(1)
    void populateDirFileCreation() throws IOException {
        deleteFilesHelper("C:/Users/" + System.getProperty("user.name") + "/Desktop");
        deleteFilesHelper("C:/Users/" + System.getProperty("user.name") + "/Downloads");
        deleteFilesHelper("C:/Users/" + System.getProperty("user.name") + "/New Folder");

        String partialDirectory = "C:/Users/" + System.getProperty("user.name");
        populateDirFileCreationHelper(partialDirectory + "/Desktop");
        populateDirFileCreationHelper(partialDirectory + "/Downloads");

        File newFolderDirectory = new File(partialDirectory + "/New folder");
        if (!newFolderDirectory.exists()) {
            newFolderDirectory.mkdir();
        }
        populateDirFileCreationHelper(newFolderDirectory.getPath());
    }

    /**
     * Confirms that a directory containing any one or all of the GTFS files will throw a
     * FileAlreadyExistsException when attempting to repopulate said directory with new
     * GTFS files
     * @throws IOException if a supplied path does not exist
     */
    @Test
    @Order(2)
    void populateDirFileCreationConflicts() throws IOException {
        deleteFilesHelper("C:/Users/" + System.getProperty("user.name") + "/New Folder");

        String directory = "C:/Users/" + System.getProperty("user.name") + "/New folder";
        File stops = new File(directory + "/stops.txt");
        File routes = new File(directory + "/routes.txt");
        File trips = new File(directory + "/trips.txt");
        File stopTimes = new File(directory + "/stop_times.txt");

        Path path = Paths.get(directory);
        Exporter.populateDir(path);
        Assertions.assertThrows(FileAlreadyExistsException.class, () ->
                Exporter.populateDir(path));

        routes.delete();
        trips.delete();
        stopTimes.delete();
        Assertions.assertThrows(FileAlreadyExistsException.class, () ->
                Exporter.populateDir(path));

        deleteFilesHelper(directory);
        Exporter.populateDir(path);
        stops.delete();
        trips.delete();
        stopTimes.delete();
        Assertions.assertThrows(FileAlreadyExistsException.class, () ->
                Exporter.populateDir(path));

        deleteFilesHelper(directory);
        Exporter.populateDir(path);
        stops.delete();
        routes.delete();
        stopTimes.delete();
        Assertions.assertThrows(FileAlreadyExistsException.class, () ->
                Exporter.populateDir(path));

        deleteFilesHelper(directory);
        Exporter.populateDir(path);
        stops.delete();
        routes.delete();
        trips.delete();
        Assertions.assertThrows(FileAlreadyExistsException.class, () ->
                Exporter.populateDir(path));
    }

    /**
     * Asserts that an IOException will be thrown when attempting to populate a nonexistent
     * directory with GTFS files
     */
    @Test
    @Order(3)
    void populateDirNonexistentPaths() {
        deleteFilesHelper("C:/Users/" + System.getProperty("user.name") + "/New Folder");

        Assertions.assertThrows(IOException.class, () ->
                Exporter.populateDir(Paths.get("C/Users/" + System.getProperty("user.name") +
                        "/New folder")));
        Assertions.assertThrows(IOException.class, () ->
                Exporter.populateDir(Paths.get("C:/User/" + System.getProperty("user.name") +
                        "/New folder")));
        Assertions.assertThrows(IOException.class, () ->
                Exporter.populateDir(Paths.get("C:/Users/" + System.getProperty("user.name") +
                        "/Newfolder")));
        Assertions.assertThrows(IOException.class, () ->
                Exporter.populateDir(Paths.get("C:/Users/" + System.getProperty("user.name") +
                        "/New folder1")));
    }

    /**
     * Asserts that a NullPointerException is thrown when attempting to export a route prior to
     * establishing the directory to export to
     * @throws IOException if the destination to write to cannot be accessed
     */
    @Test
    @Order(4)
    void exportRouteFailedPrecondition() throws IOException {
        importFiles();
        Exporter.reset();

        Route route = gtfs.getRoute("12");
        Assertions.assertThrows(NullPointerException.class, () ->
                Exporter.exportRoute(route));
    }

    /**
     * Verifies that an exported routes file properly receives its header and route data without
     * overwriting previous file information
     * @throws IOException if a supplied path does not exist or if the destination to write to
     * cannot be accessed
     */
    @Test
    @Order(5)
    void exportRouteFileContents() throws IOException {
        String directory = "C:/Users/" + System.getProperty("user.name") + "/New folder";
        deleteFilesHelper(directory);
        importFiles();

        Exporter.populateDir(Paths.get(directory));
        Scanner routes = new Scanner(Paths.get(directory + "/routes.txt"));
        Assertions.assertFalse(routes.hasNext());
        routes.close();

        Route route1 = gtfs.getRoute("12");
        Exporter.exportRoute(route1);
        Route route2 = gtfs.getRoute("RR3");
        Exporter.exportRoute(route2);
        Exporter.close();
        routes = new Scanner(Paths.get(directory + "/routes.txt"));
        Assertions.assertEquals("route_id,agency_id,route_short_name,route_type",
                routes.nextLine());
        Assertions.assertEquals("12,MCTS,12,3", routes.nextLine());
        Assertions.assertEquals("RR3,MCTS,RR3,3", routes.nextLine());
        Assertions.assertFalse(routes.hasNext());
        routes.close();
    }

    /**
     * Asserts that a NullPointerException is thrown when attempting to export a stop prior to
     * establishing the directory to export to
     * @throws IOException if the destination to write to cannot be accessed
     */
    @Test
    @Order(6)
    void exportStopFailedPrecondition() throws IOException {
        importFiles();
        Exporter.reset();

        final String stopID = "6712";
        Stop stop = gtfs.getStop(stopID);
        Assertions.assertThrows(NullPointerException.class, () ->
                Exporter.exportStop(stop));
    }

    /**
     * Verifies that an exported stops file properly receives its header and stop data without
     * overwriting previous file information
     * @throws IOException if a supplied path does not exist or if the destination to write to
     * cannot be accessed
     */
    @Test
    @Order(7)
    void exportStopFileContents() throws IOException {
        String directory = "C:/Users/" + System.getProperty("user.name") + "/New folder";
        deleteFilesHelper(directory);
        importFiles();

        Exporter.populateDir(Paths.get(directory));
        Scanner stops = new Scanner(Paths.get(directory + "/stops.txt"));
        Assertions.assertFalse(stops.hasNext());
        stops.close();

        final String stopID1 = "6712";
        final String stopID2 = "5505";
        Stop stop1 = gtfs.getStop(stopID1);
        Exporter.exportStop(stop1);
        Stop stop2 = gtfs.getStop(stopID2);
        Exporter.exportStop(stop2);
        Exporter.close();
        stops = new Scanner(Paths.get(directory + "/stops.txt"));
        Assertions.assertEquals("stop_id,stop_name,stop_lat,stop_lon", stops.nextLine());
        Assertions.assertEquals("6712,STATE & 5101 #6712,43.044448,-87.977937",
                stops.nextLine());
        Assertions.assertEquals("5505,LAYTON & KINGAN #5505,42.959050,-87.862894",
                stops.nextLine());
        Assertions.assertFalse(stops.hasNext());
        stops.close();
    }

    /**
     * Asserts that a NullPointerException is thrown when attempting to export a trip prior to
     * establishing the directory to export to
     * @throws IOException if the destination to write to cannot be accessed
     */
    @Test
    @Order(8)
    void exportTripFailedPrecondition() throws IOException {
        importFiles();
        Exporter.reset();

        Trip trip = gtfs.getTrip("21736564_2535");
        Assertions.assertThrows(NullPointerException.class, () ->
                Exporter.exportTrip(trip));
    }

    /**
     * Verifies that an exported trips file properly receives its header and trip data without
     * overwriting previous file information
     * @throws IOException if a supplied path does not exist or if the destination to write to
     * cannot be accessed
     */
    @Test
    @Order(9)
    void exportTripFileContents() throws IOException {
        String directory = "C:/Users/" + System.getProperty("user.name") + "/New folder";
        deleteFilesHelper(directory);
        importFiles();

        Exporter.populateDir(Paths.get(directory));
        Scanner trips = new Scanner(Paths.get(directory + "/trips.txt"));
        Assertions.assertFalse(trips.hasNext());
        trips.close();

        Trip trip1 = gtfs.getTrip("21736564_2535");
        Exporter.exportTrip(trip1);
        Trip trip2 = gtfs.getTrip("22528180_1395");
        Exporter.exportTrip(trip2);
        Exporter.close();
        trips = new Scanner(Paths.get(directory + "/trips.txt"));
        Assertions.assertEquals("route_id,service_id,trip_id", trips.nextLine());
        Assertions.assertEquals("64,17-SEP_SUN,21736564_2535", trips.nextLine());
        Assertions.assertEquals("53,17-SEP_NH-PON_0,22528180_1395", trips.nextLine());
        Assertions.assertFalse(trips.hasNext());
        trips.close();
    }

    /**
     * Asserts that a NullPointerException is thrown when attempting to export a stopTimes prior
     * to establishing the directory to export to
     * @throws IOException if the destination to write to cannot be accessed
     */
    @Test
    @Order(10)
    void exportStopTimeFailedPrecondition() throws IOException {
        importFiles();
        Exporter.reset();

        final String stopID = "9113";
        final String tripID = "4332";
        StopTime stopTime = gtfs.getStopTime(tripID,stopID);
        Assertions.assertThrows(NullPointerException.class, () ->
                Exporter.exportStopTime(stopTime));
    }

    /**
     * Verifies that an exported stop_times file properly receives its header and stopTimes data
     * without overwriting previous file information
     * @throws IOException if a supplied path does not exist or if the destination to write to
     * cannot be accessed
     */
    @Test
    @Order(11)
    void exportStopTimeFileContents() throws IOException {
        String directory = "C:/Users/" + System.getProperty("user.name") + "/New folder";
        deleteFilesHelper(directory);
        importFiles();

        Exporter.populateDir(Paths.get(directory));
        Scanner stopTimes = new Scanner(Paths.get(directory + "/stop_times.txt"));
        Assertions.assertFalse(stopTimes.hasNext());
        stopTimes.close();

        final String stopID1 = "9113";
        final String stopID2 = "433";
        final String tripID1 = "5849";
        final String tripID2 = "5687";
        StopTime stopTime1 = gtfs.getStopTime(tripID1,stopID1);
        Exporter.exportStopTime(stopTime1);
        StopTime stopTime2 = gtfs.getStopTime(tripID2,stopID2);
        Exporter.exportStopTime(stopTime2);
        Exporter.close();
        stopTimes = new Scanner(Paths.get(directory + "/stop_times.txt"));
        Assertions.assertEquals("trip_id,arrival_time,departure_time,stop_id,stop_sequence",
                stopTimes.nextLine());
        Assertions.assertEquals("21860425_930,21:32:00,21:32:00,9113,55", stopTimes.nextLine());
        Assertions.assertEquals("22528180_1395,15:43:00,15:43:00,433,54", stopTimes.nextLine());
        Assertions.assertFalse(stopTimes.hasNext());
        stopTimes.close();
    }

    /**
     * Confirms that data is only written to the GTFS file after the stream has been flushed
     * (for Exporter's close method)
     * @throws IOException if a supplied path does not exist or if the destination to write to
     * cannot be accessed
     */
    @Test
    @Order(12)
    void closeVerifyFlush() throws IOException {
        testBeforeFlush();
        Exporter.close();
        Scanner stops = new Scanner(Paths.get("C:/Users/" + System.getProperty("user.name") +
                "/New folder/stops.txt"));
        Assertions.assertTrue(stops.hasNext());
        stops.close();
    }

    /**
     * Asserts that an IllegalStateException is thrown when attempting to close Exporter's
     * already closed stream
     */
    @Test
    @Order(13)
    void closeAlreadyClosed() {
        Assertions.assertThrows(IllegalStateException.class, () ->
                Exporter.close());
    }

    /**
     * Confirms that data is only written to the GTFS file after the stream has been flushed
     * (for Exporter's reset method)
     * @throws IOException if a supplied path does not exist or if the destination to write to
     * cannot be accessed
     */
    @Test
    @Order(14)
    void resetVerifyFlush() throws IOException {
        testBeforeFlush();
        Exporter.reset();
        Scanner stops = new Scanner(Paths.get("C:/Users/" + System.getProperty("user.name") +
                "/New folder/stops.txt"));
        Assertions.assertTrue(stops.hasNext());
        stops.close();
    }

    /**
     * Asserts that a NullPointerException is thrown when attempting to export GTFS data
     * after resetting Exporter, and without establishing the output directory
     * @throws IOException if a supplied path does not exist or if the destination to write to
     * cannot be accessed
     */
    @Test
    @Order(15)
    void resetOutputFilesRemoved() throws IOException {
        String directory = "C:/Users/" + System.getProperty("user.name") + "/New folder";
        deleteFilesHelper(directory);
        Exporter.populateDir(Paths.get(directory));
        importFiles();

        final String stopID = "433";
        final String tripID = "5433";
        Route route = gtfs.getRoute("12");
        Stop stop = gtfs.getStop(stopID);
        Trip trip = gtfs.getTrip("21736564_2535");
        StopTime stopTime = gtfs.getStopTime(tripID, stopID);

        Exporter.reset();
        Assertions.assertThrows(NullPointerException.class, () ->
                Exporter.exportRoute(route));
        Assertions.assertThrows(NullPointerException.class, () ->
                Exporter.exportStop(stop));
        Assertions.assertThrows(NullPointerException.class, () ->
                Exporter.exportTrip(trip));
        Assertions.assertThrows(NullPointerException.class, () ->
                Exporter.exportStopTime(stopTime));
    }

    private void importFiles() throws IOException {
        gtfs = GTFS.getInstance();
        File stops = new File(System.getProperty("user.dir") + "/Data/GTFS_MCTS/stops.txt");
        File routes = new File(System.getProperty("user.dir") + "/Data/GTFS_MCTS/routes.txt");
        File trips = new File(System.getProperty("user.dir") + "/Data/GTFS_MCTS/trips.txt");
        File stopTimes = new File(System.getProperty("user.dir") +
                "/Data/GTFS_MCTS/stop_times.txt");
        gtfs.loadStop(stops);
        gtfs.loadRoute(routes);
        gtfs.loadTrip(trips);
        gtfs.loadStopTime(stopTimes);
    }

    private void populateDirFileCreationHelper(String directory) throws IOException {
        Exporter.populateDir(Paths.get(directory));
        File stops = new File(directory + "/stops.txt");
        File routes = new File(directory + "/routes.txt");
        File trips = new File(directory + "/trips.txt");
        File stopTimes = new File(directory + "/stop_times.txt");
        Assertions.assertTrue(stops.exists());
        Assertions.assertTrue(routes.exists());
        Assertions.assertTrue(trips.exists());
        Assertions.assertTrue(stopTimes.exists());
    }

    private void deleteFilesHelper(String directory) {
        File stops = new File(directory + "/stops.txt");
        File routes = new File(directory + "/routes.txt");
        File trips = new File(directory + "/trips.txt");
        File stopTimes = new File(directory + "/stop_times.txt");
        stops.delete();
        routes.delete();
        trips.delete();
        stopTimes.delete();
    }

    private void testBeforeFlush() throws IOException {
        String directory = "C:/Users/" + System.getProperty("user.name") + "/New folder";
        importFiles();
        deleteFilesHelper(directory);
        Exporter.populateDir(Paths.get(directory));

        final String stopID = "6712";
        Stop stop = gtfs.getStop(stopID);
        Exporter.exportStop(stop);
        Scanner stops = new Scanner(Paths.get(directory + "/stops.txt"));
        Assertions.assertFalse(stops.hasNext());
        stops.close();
    }
}
