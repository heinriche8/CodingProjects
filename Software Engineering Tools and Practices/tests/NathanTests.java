/*
 * Course: SE 2030 021
 * Spring 2021-2022
 * Lab 8 - Defect Tracking and Technical Debt
 * Name: AfternoonGroup3
 * Created: 4/7/2022
 */

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

/**
 * @author kitzmann
 * @version 1.2
 */
public class NathanTests {
    //stop time and trip
    private static GTFS data = GTFS.getInstance();
    @BeforeAll
    static void setup(){
        try {
            data.loadStopTime(new File("tests\\NathanTestData\\stop_times.txt"));
            data.loadTrip(new File("tests\\NathanTestData\\trips.txt"));
        } catch (IOException e) {
            System.out.println("Bad Files");
        }
    }

    @Test
    void testStopTime(){
        Assertions.assertEquals(data.getStopTime("21736564_2535", "9113").toString(),new StopTime("21736564_2535","08:51:00" ,"08:51:00","9113",1).toString());
        Assertions.assertEquals(data.getStopTime("21752862_470", "4720").toString(),new StopTime("21752862_470","05:57:00","05:57:00","4720",35).toString());
        Assertions.assertEquals(data.getStopTime("21736570_2547", "4763").toString(),new StopTime("21736570_2547","16:13:00","16:13:00","4763",10).toString());
        Assertions.assertEquals(data.getStopTime("21736596_578", "6211").toString(),new StopTime("21736596_578","16:19:00","16:19:00","6211",11).toString());
        Assertions.assertEquals(data.getStopTime("21858793_713", "6930").toString(),new StopTime("21858793_713","24:24:00","24:24:00","6930",64).toString());
    }

    @Test
    void testTrip(){
        Assertions.assertEquals(data.getTrip("21736594_570").toString(),new Trip("64","17-SEP_SUN","21736594_570").toString());
        Assertions.assertEquals(data.getTrip("21794637_1575").toString(),new Trip("23D","17-SEP_SUN","21794637_1575").toString());
        Assertions.assertEquals(data.getTrip("21794657_2278").toString(),new Trip("BLU","17-SEP_SUN","21794657_2278").toString());
        Assertions.assertEquals(data.getTrip("22528173_2090").toString(),new Trip("53","17-SEP_WK","22528173_2090").toString());
        Assertions.assertEquals(data.getTrip("22528180_1395").toString(),new Trip("53","17-SEP_NH-PON_0","22528180_1395").toString());

    }



}
