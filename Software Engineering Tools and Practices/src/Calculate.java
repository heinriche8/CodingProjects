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

import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Class to calculate speed and distance
 * @author Nathan kitzman, Aidan Holcombe
 * @version 1.1
 */
public class Calculate extends TextArea implements Observer {

    private GTFS gtfs = GTFS.getInstance();
    private Hashtable<String, Hashtable<String, StopTime>> stopTimes = gtfs.getStopTimes();
    private AtomicReference<String> firstStopID = new AtomicReference<>(new String());
    private AtomicReference<String> secondStopID = new AtomicReference<>(new String());
    StringBuilder firstTime = new StringBuilder();
    StringBuilder secondTime = new StringBuilder();

    /**
     * calculates the distance between two stops
     * @param trip the trip to use
     * @return the distance between the two in miles
     */
    public double calculateDistance(Trip trip){
        Stop stop1 = gtfs.getStop(firstStopID.get());
        Stop stop2 = gtfs.getStop(secondStopID.get());

        //have to convert to radians
        double lat1 = (stop1.getStop_lat()*Math.PI)/180;
        double lat2 = (stop2.getStop_lat()*Math.PI)/180;
        double lon1 = (stop1.getStop_lon()*Math.PI)/180;
        double lon2 = (stop2.getStop_lon()*Math.PI)/180;
        return 3963.0 * Math.acos((Math.sin(lat1) * Math.sin(lat2)) + Math.cos(lat1) * Math.cos(lat2) * Math.cos((lon2-lon1)));
    }

    /**
     * Calculates the average speed of a trip in miles per hour
     * @param trip the trip to calculate the speed of
     * @return the average speed
     */
    public double calculateAverageSpeed(Trip trip){
        //sets up variables to use


        //calculates distance with those stops
        double distance = calculateDistance(trip);
        //converts the time to seconds
        double time = (Double.parseDouble(secondTime.toString().split(":")[0])*3600+(Double.parseDouble(secondTime.toString().split(":")[1])*60+(Double.parseDouble(secondTime.toString().split(":")[2])))-(Double.parseDouble(firstTime.toString().split(":")[0])*3600+(Double.parseDouble(firstTime.toString().split(":")[1])*60)+(Double.parseDouble(firstTime.toString().split(":")[2]))));
        //converts again to miles per hour
        return (distance/(time/3600));
    }

    public static double timeToSeconds(String time) {
        return (Double.parseDouble(time.split(":")[0])*3600+(Double.parseDouble(time.split(":")[1])*60+(Double.parseDouble(time.split(":")[2]))));
    }

    @Override
    public void update() {
        Hashtable<String, Trip> trips = gtfs.getTrips();
        Iterator<String> iterator = trips.keys().asIterator();

        String display = "";

        while (iterator.hasNext()) {

            Trip currentTrip = trips.get(iterator.next());

            AtomicReference<Double> max = new AtomicReference<>(Double.MIN_VALUE);
            AtomicReference<Double> min = new AtomicReference<>(Double.MAX_VALUE);
            //loops through each stop time to get the first stop and the last stop on a trip
            if (stopTimes.get(currentTrip.getTrip_id()) != null) {
                stopTimes.get(currentTrip.getTrip_id()).forEach((stop_id, stopTime) -> {
                    if (stopTime.getTrip_id().equals(currentTrip.getTrip_id())) {
                        if (timeToSeconds(stopTime.getArrival_time()) < min.get()) {
                            firstTime.delete(0, firstTime.length());
                            firstStopID.set(stopTime.getStop_id());
                            min.set(timeToSeconds(stopTime.getArrival_time()));
                            firstTime.append(stopTime.getArrival_time());
                        } else if (timeToSeconds(stopTime.getDeparture_time()) > max.get()) {
                            secondTime.delete(0, secondTime.length());
                            max.set(timeToSeconds(stopTime.getDeparture_time()));
                            secondTime.append(stopTime.getDeparture_time());
                            secondStopID.set(stopTime.getStop_id());
                        }
                    }
                });
                double currentDist = calculateDistance(currentTrip);
                double currentSpeed = calculateAverageSpeed(currentTrip);

                display = display.concat(String.format("Trip ID: %s\nDistance: %.4f \n Avg. Speed: %.4f \n\n", currentTrip.getTrip_id(), currentDist, currentSpeed));
            }
        }
        super.setText(display);
    }
}
