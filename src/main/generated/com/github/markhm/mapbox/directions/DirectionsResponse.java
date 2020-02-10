package com.github.markhm.mapbox.directions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.markhm.mapbox.Util;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.util.List;

// https://app.quicktype.io/

public class DirectionsResponse
{
    private static Log log = LogFactory.getLog(DirectionsResponse.class);

    private static String AMS_TO_CPH = "data/amsterdam_to_copenhagen.json";
    private static String UTR_TO_ROS = "data/utrecht_to_roskilde.json";
    private static String VEENENDAAL_TO_NIVAA = "data/veenendaal_to_nivaa.json";

    private static String ACTIVE_PATH = UTR_TO_ROS;

    private List<Route> routes;
    private List<Waypoint> waypoints;
    private String code;
    private String uuid;

    private static DirectionsResponse instance = null;

    public static DirectionsResponse getInstance()
    {
        try
        {
            if (instance == null)
            {
                instance = Converter.fromJsonString(Util.readLineByLine(ACTIVE_PATH));
            }
        }catch (IOException ioe)
        {
            log.error(ioe);
        }

        return instance;

    }

    public static void main(String[] args) throws Exception
    {
        DirectionsResponse directionsResponse = getInstance();

        List<Route> routes = directionsResponse.getRoutes();

        for (Route route : routes)
        {
            long durationHours = route.getDuration() / 3600;
            long remainingMinutesInSeconds = route.getDuration() - (durationHours * 3600);

            long roundedMinutes = remainingMinutesInSeconds / 60;
            long remainingSeconds = remainingMinutesInSeconds - (roundedMinutes * 60);

            log.info("Total duration for this route: ");
            log.info(route.getDuration() + " seconds or");
            log.info(route.getDuration() / 60 + " minutes or");
            log.info( durationHours+ " hours, " + roundedMinutes + " minutes and "+remainingSeconds+" seconds.");
            log.info("");

            int counter = 0;
            long accumulatedDuration = 0;
            List<Leg> legs = route.getLegs();
            for (int i = 0 ; i < legs.size(); i++)
            {
                log.info("Entering leg "+i);
                Leg leg = legs.get(i);

                List<Step> steps = leg.getSteps();
                for (int j = 0; j < steps.size(); j++)
                {
                    log.info("Entering step "+j);
                    Step step = steps.get(j);

                    List<List<Double>> coordinates = step.getGeometry().getCoordinates();

                    long stepDuration = step.getDuration();
                    double coordinateDuration = ((double) stepDuration / coordinates.size());

                    for (int k = 0; k < coordinates.size(); k++)
                    {
                        List<Double> coordinate = coordinates.get(k);

                        log.info(counter + " L"+i + "-S" + j + "-C" + k + " - " + (coordinateDuration * (k+1)) + "-" + accumulatedDuration + " @ [" + coordinate.get(1) + "," + coordinate.get(0) + "]");

                        // accumulatedDuration += coordinateDuration;

                        counter++;
                    }

                    accumulatedDuration += stepDuration;
                    // log.info(stepDuration+" seconds until " + intersection.getLocation());
                }
            }

            log.info("Accumulated duration = "+accumulatedDuration);
            log.info("Leg, Segment, Coordinates");
        }
    }

    @JsonProperty("routes")
    public List<Route> getRoutes()
    {
        return routes;
    }

    @JsonProperty("routes")
    public void setRoutes(List<Route> value)
    {
        this.routes = value;
    }

    @JsonProperty("waypoints")
    public List<Waypoint> getWaypoints()
    {
        return waypoints;
    }

    @JsonProperty("waypoints")
    public void setWaypoints(List<Waypoint> value)
    {
        this.waypoints = value;
    }

    @JsonProperty("code")
    public String getCode()
    {
        return code;
    }

    @JsonProperty("code")
    public void setCode(String value)
    {
        this.code = value;
    }

    @JsonProperty("uuid")
    public String getUUID()
    {
        return uuid;
    }

    @JsonProperty("uuid")
    public void setUUID(String value)
    {
        this.uuid = value;
    }
}
