package com.github.markhm.mapbox.directions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class DeprecatedDirectionsResponse extends JSONObject
{
    private static Log log = LogFactory.getLog(DeprecatedDirectionsResponse.class);

    public static DeprecatedDirectionsResponse singleton = null;

    static
    {
        // String filePath = "/Users/mark/git/markhm/mapbox-flow/src/main/resources/data/oisterwijk_kijkduin_int2.json";
        // String filePath = "/Users/mark/git/markhm/mapbox-flow/src/main/resources/data/from_paris_to_copenhagen.json";
        String filePath = "/Users/mark/git/markhm/mapbox-flow/src/main/resources/data/amsterdam_to_copenhagen.json";
        // String filePath = "/Users/mark/git/markhm/mapbox-flow/src/main/resources/data/paris_to_copenhagen.json";
        // String filePath = "/Users/mark/git/markhm/mapbox-flow/src/main/resources/data/oisterwijk_stokske_int2.json";

        singleton = new DeprecatedDirectionsResponse(readLineByLine(filePath));

//        JSONArray waypoints = singleton.getJSONArray(DirectionsResponse.waypoints);
//        for (int i = 0; i < waypoints.length(); i++)
//        {
//            JSONObject object = (JSONObject) waypoints.get(i);
//            GeoLocation location = GeoLocation.fromJSONWaypoint(object);
//            log.info(location);
//        }

        JSONArray routes = singleton.getJSONArray(DeprecatedDirectionsResponse.routes);
        for (int i = 0; i < routes.length(); i++)
        {
            JSONObject object = (JSONObject) routes.get(i);
            Route route = Route.fromJSON(object);
            // log.info(route.getGeometry().toString(2));
        }
    }

    public static final String code = "code";
    public static final String routes = "routes";
    public static final String waypoints = "waypoints";
    public static final String uuid = "uuid";

    public DeprecatedDirectionsResponse(String string)
    {
        super(string);
    }

    public static DeprecatedDirectionsResponse getInstance()
    {
        return singleton;
    }

    public JSONObject getGeometry()
    {
        JSONArray routes = getJSONArray(DeprecatedDirectionsResponse.routes);
        JSONObject object = (JSONObject) routes.get(0);

        Route route = Route.fromJSON(object);
        return route.getGeometry();
    }

    public static void main(String[] args)
    {
//        log.info("distance = " + directionsResponse.getString(DirectionsResponse.distance) + " meters");
//        log.info("duration = " + directionsResponse.getString(DirectionsResponse.duration) + " seconds");
    }

    private static String readLineByLine(String filePath)
    {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8))
        {
            stream.forEach(s -> contentBuilder.append(s));
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        return contentBuilder.toString();
    }

    private class Routes extends JSONArray
    {
        public Routes()
        {
        }
    }

    private static class Route extends JSONObject
    {
        public static final String duration_key = "duration";
        public static final String distance_key = "distance";
        public static final String weight_key = "weight";
        public static final String weigh_name_key = "weight_name";
        public static final String geometry_key = "geometry";
        public static final String legs_key = "legs";

        private int duration;
        private int distance;
        private int weight;
        private String weightName;

        private JSONObject geometry;
        private JSONArray legs;

        public Route(JSONObject jsonObject)
        {
            super(jsonObject);
        }

        public static Route fromJSON(JSONObject jsonObject)
        {
            Route result = new Route(jsonObject);

            result.duration = jsonObject.getInt(duration_key);
            result.distance = jsonObject.getInt(distance_key);
            result.weight = jsonObject.getInt(weight_key);
            result.weightName = jsonObject.getString(weigh_name_key);
            result.geometry = jsonObject.getJSONObject(geometry_key);
            result.legs = jsonObject.getJSONArray(legs_key);

            return result;
        }

        public JSONObject getGeometry()
        {
            return geometry;
        }

        public JSONArray getLegs()
        {
            return legs;
        }
    }

    private static class Leg extends JSONObject
    {
        public static final String summary_key = "summary";
        public static final String duration_key = "duration";
        public static final String distance_key = "distance";
        public static final String steps_key = "steps";

        private JSONArray steps = null;

        public static Leg fromJSON(JSONObject jsonObject)
        {
            Leg leg = new Leg();
            leg.steps = jsonObject.getJSONArray("steps");
            // JSONObject json2 = jsonObject.getJSONObject(steps_key);

            return leg;
        }
    }

    private static class Step extends JSONObject
    {
        public static final String intersections_key = "intersections";
        public static final String duration_key = "duration";
        public static final String distance_key = "distance";
        public static final String steps_key = "steps";
        public static final String geometry_key = "geometry";

        private JSONObject geometry = null;
        private int duration;
        private int distance;
        private String name = null;

        public static Step fromJSON(JSONObject jsonObject)
        {
            Step step = new Step();
            step.geometry = jsonObject.getJSONObject(geometry_key);
            step.duration = jsonObject.getInt(duration_key);
            step.distance = jsonObject.getInt(distance_key);

            return step;
        }

        public String toString()
        {
            return "";
        }

    }


}
