package com.github.markhm.mapbox.directions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.markhm.mapbox.Util;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

// https://app.quicktype.io/

public class DirectionsResponse
{
    private static Log log = LogFactory.getLog(DirectionsResponse.class);

    private static String filePath = "/Users/mark/git/markhm/mapbox-flow/src/main/resources/data/amsterdam_to_copenhagen.json";

    private List<Route> routes;
    private List<Waypoint> waypoints;
    private String code;
    private String uuid;

    public static void main(String[] args) throws Exception
    {
        DirectionsResponse directionsResponse = Converter.fromJsonString(Util.readLineByLine(filePath));

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

            long accumulatedDuration = 0;
            List<Leg> legs = route.getLegs();
            for (Leg leg : legs)
            {
                List<Step> steps = leg.getSteps();
                for (int i = 0; i < steps.size(); i++)
                {
                    Step step = steps.get(i);
                    // log.info(step.getDestinations());
//                    List<Intersection> intersections = step.getIntersections();
//                    for (Intersection intersection : intersections)
//                    {
//                    }
                    List<List<Double>> coordinates = step.getGeometry().getCoordinates();
                    for (List<Double> coordinate : coordinates)
                    {
                        log.info(i + " - [" + coordinate.get(0) + "," + coordinate.get(1) + "]");
                    }
                    long duration = step.getDuration();
                    accumulatedDuration += duration;
                    // log.info(duration+" seconds until " + intersection.getLocation());

                }
            }

            log.info("Accumulated duration = "+accumulatedDuration);
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
