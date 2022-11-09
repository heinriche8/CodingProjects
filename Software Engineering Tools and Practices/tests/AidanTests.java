import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AidanTests {
    GTFS gtfs;
    Calculate calculate = new Calculate();

    @BeforeEach
    public void setup() {
        gtfs = GTFS.getInstance();
        gtfs.addTrip("21736564_2535", new Trip("64","17-SEP_SUN","21736564_2535"));
        gtfs.addStopTime("21736564_2535","9113",new StopTime("21736564_2535","08:51:00","08:51:00","9113",1));
        gtfs.addStopTime("21736564_2535","4759", new StopTime("21736564_2535","08:54:00","08:54:00","4759",6));
        gtfs.addStopTime("21736564_2535", "6294", new StopTime("21736564_2535","12:49:00","12:49:00","6294",33));
        gtfs.addStop("9113", new Stop("9113","SOUTHRIDGE & GRANGE #9113",42.9451148,-88.0042294));
        gtfs.addStop("4759", new Stop("4759","NORTHWAY & BROAD #4759",42.942342,-87.9961012));
        gtfs.addStop("6294", new Stop("6294","S60 & LINCOLN #6294",43.002736,-87.9873962));
    }

    @Test
    public void distanceTest() {
        Assertions.assertEquals(4, Math.round(calculate.calculateDistance(gtfs.getTrip("21736564_2535"))));
    }

    @Test
    public void timeTest() {
        Assertions.assertEquals(9, Math.round(calculate.calculateAverageSpeed(gtfs.getTrip("21736564_2535"))));
    }
}
