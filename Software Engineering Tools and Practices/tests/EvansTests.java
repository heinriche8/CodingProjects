import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;

public class EvansTests {

    private static GTFS testData = GTFS.getInstance();

    @BeforeAll
    static void init() {
        try{
            testData.loadRoute(new File("tests\\EvansTestData\\routes.txt"));
            testData.loadStop(new File("tests\\EvansTestData\\stops.txt"));
        } catch (IOException exception) {
            System.out.println("Bad File");
        }

    }

    @Test
    void TestAddStop() {
        GTFS localData = GTFS.getInstance();

        Stop target = new Stop("6712", "STATE & 5101 #6712",  43.0444475, -87.9779369);
        assertEquals(localData.getStop("6712").toString(), target.toString());

        target = new Stop("3919","N27 & CONCORDIA #3919",43.0793464,-87.9473946);
        assertEquals(localData.getStop("3919").toString(), target.toString());

        target = new Stop("728", "S1 & VIRGINIA #728",  43.0261353, -87.9110042);
        assertEquals(localData.getStop("728").toString(), target.toString());

        target = new Stop("1244", "HOLTON & MEINECKE #1244",  43.0619186, -87.9052747);
        assertEquals(localData.getStop("1244").toString(), target.toString());

        target = new Stop("1638", "GREENFIELD & S106 #1638",  43.0165957,-88.0442668);
        assertEquals(localData.getStop("1638").toString(), target.toString());
    }

    @Test
    void TestAddRoute() {
        GTFS data = GTFS.getInstance();

        Route target = new Route("30X", "Sherman - WisconsinEXpress", 3);
        assertEquals(data.getRoute("30X"), target);

        target = new Route("44U","Fair Park - Hales CornersUBUS", 3);
        assertEquals(data.getRoute("44U"), target);

        target = new Route("53", "Lincoln Avenue", 3);
        assertEquals(data.getRoute("53"), target);

        target = new Route("54", "Mitchell - Burnham", 3);
        assertEquals(data.getRoute("54"), target);

        target = new Route("GRE", "GreenLine", 3);
        assertEquals(data.getRoute("GRE"), target);
    }

    @Test
    void BadAddRoute() {
        // Clear all data


    }


}
