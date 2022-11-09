import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;


public class RyansTests {


    private static GTFS loadData = GTFS.getInstance();
    static File fileOut = new File("tests\\RyansTestData");
    static File stopTimesFile = new File("tests\\RyansTestData\\init-stop-times.txt");
    static File stopsFile = new File("tests\\RyansTestData\\init-stops.txt");
    static File routesFile = new File("tests\\RyansTestData\\init-routes.txt");
    static File tripsFile = new File("tests\\RyansTestData\\init-trips.txt");
    ArrayList<String> lines;
    @BeforeAll
    static void init() {
        try {
            loadData.loadStopTime(stopTimesFile);
            loadData.loadStop(stopsFile);
            loadData.loadStopTime(routesFile);
            loadData.loadStop(tripsFile);
            Exporter.populateDir(fileOut.toPath());
        } catch (IOException exception) {
            if(exception instanceof FileAlreadyExistsException){
                System.out.println("File or Files already exist");
            }else{
                System.out.println("Bad File");
            }
        }
    }

    @Test
    void TestExportStop() throws IOException {
            GTFS data = GTFS.getInstance();
            Stop stop = data.getStop("6712");
            Exporter.exportStop(stop);
            Exporter.close();
            lines = new ArrayList<>(Files.lines(Path.of("tests\\RyansTestData\\stops.txt")).toList());
            String[] order = lines.get(0).split(",");
                int stop_name_index = 0;
                int stop_id_index = 0;
                int stop_lon_index = 0;
                int stop_lat_index = 0;
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
            array = lines.get(1).split(",");
            assertEquals(array[stop_name_index],                    data.getStop("6712").getStop_name());
            assertEquals(Integer.parseInt(array[stop_id_index]),    data.getStop("6712").getStop_id());
            assertTrue(Double.parseDouble(array[stop_lon_index]) < data.getStop("6712").getStop_lon() + 0.00001);
            assertTrue(Double.parseDouble(array[stop_lon_index]) > data.getStop("6712").getStop_lon() - 0.00001);
            assertTrue(Double.parseDouble(array[stop_lat_index]) < data.getStop("6712").getStop_lat() + 0.00001);
            assertTrue(Double.parseDouble(array[stop_lat_index]) > data.getStop("6712").getStop_lat() - 0.00001);

    }
    @Test
    void TestExportStopTimes() throws IOException {
        GTFS data = GTFS.getInstance();
        StopTime stoptime = data.getStopTime("955","9113");
        Exporter.exportStopTime(stoptime);
        Exporter.close();
        lines = new ArrayList<>(Files.lines(Path.of("tests\\RyansTestData\\stop_times.txt")).toList());
        String[] order = lines.get(0).split(",");
        int trip_id_index = 0;
        int arrival_time_index = 0;
        int departure_time_index = 0;
        int stop_id_index = 0;
        int stop_sequence_index = 0;
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
        array = lines.get(1).split(",");
        assertEquals(array[trip_id_index],                          data.getStopTime("955","9113").getTrip_id());
        assertEquals(array[arrival_time_index],                     data.getStopTime("955","9113").getArrival_time());
        assertEquals(array[departure_time_index],                   data.getStopTime("955","9113").getDeparture_time());
        assertEquals(Integer.parseInt(array[stop_id_index]),        data.getStopTime("955","9113").getStop_id());
        assertEquals(Integer.parseInt(array[stop_sequence_index]),  data.getStopTime("955","9113").getStop_sequence());

    }

    @Test
    void TestGetTrips() throws IOException {



    }
    //work in progress
   /* @Test
    void TestExportTrips() throws IOException {
        GTFS data = GTFS.getInstance();
        Trip trip = data.getTrip("21736564_2535");
        Exporter.exportTrip(trip);
        Exporter.close();
        lines = new ArrayList<>(Files.lines(Path.of("tests\\RyansTestData\\trips.txt")).toList());
        String[] order = lines.get(0).split(",");
        int route_id_index = 0;
        int service_id_index = 0;
        int trip_id_index = 0;
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
        array = lines.get(1).split(",");
        assertEquals(array[trip_id_index],      data.getTrip("60TH-VLIET").getTrip_id());
        assertEquals(array[route_id_index],     data.getTrip("60TH-VLIET").getRoute_id());
        assertEquals(array[service_id_index],   data.getTrip("60TH-VLIET").getService_id());

    }
    @Test
    void TestExportRoutes() throws IOException {
        GTFS data = GTFS.getInstance();
        Route route = data.getRoute("12");
        Exporter.exportRoute(route);
        Exporter.close();
        lines = new ArrayList<>(Files.lines(Path.of("tests\\RyansTestData\\routes.txt")).toList());
        String[] order = lines.get(0).split(",");
        int route_name_index = 0;
        int route_id_index = 0;
        int route_type_index = 0;
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
        }
        String[] array;
        array = lines.get(1).split(",");
        assertEquals(array[route_name_index],                   data.getRoute("12").getRoute_name_long());
        assertEquals(array[route_id_index],                     data.getRoute("12").getRoute_id());
        assertEquals(Integer.parseInt(array[route_type_index]), data.getRoute("12").getRoute_type());

    }*/

    @AfterEach
    void deleteFiles(){
        Path.of("tests\\RyansTestData\\stop_times.txt").toFile().delete();
        Path.of("tests\\RyansTestData\\stops.txt").toFile().delete();
        Path.of("tests\\RyansTestData\\routes.txt").toFile().delete();
        Path.of("tests\\RyansTestData\\trips.txt").toFile().delete();
    }




}



