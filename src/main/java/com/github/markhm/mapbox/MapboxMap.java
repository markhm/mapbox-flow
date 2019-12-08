package com.github.markhm.mapbox;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.page.Page;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MapboxMap extends Div
{
    private static Log log = LogFactory.getLog(MapboxMap.class);

    private Page page = null;

    boolean alreadyRendered = false;

    public MapboxMap()
    {
        setId("map");
        getStyle().set("align-self", "center");
        getStyle().set("border", "1px solid black");

        setWidth("1200px");
        setHeight("700px");

        page = UI.getCurrent().getPage();

        if (!alreadyRendered)
        {
            render();
            alreadyRendered = true;
        }
    }

    private void render()
    {
        page.addStyleSheet("https://api.tiles.mapbox.com/mapbox-gl-js/v1.6.0/mapbox-gl.css");
        page.addJavaScript("https://api.tiles.mapbox.com/mapbox-gl-js/v1.6.0/mapbox-gl.js");
        page.addJavaScript("https://api.tiles.mapbox.com/mapbox.js/plugins/turf/v2.0.0/turf.min.js");

        page.addJavaScript("/js/mapbox/mapbox.js");

        String accessToken = loadAccessToken();
        page.executeJs("mapboxgl.accessToken = '" + accessToken + "';");

        page.executeJs("renderMapbox(" + GeoLocation.Turku.getCoordinates()+");");
    }

    public void flyTo(GeoLocation geoLocation)
    {
        page.executeJs("map.flyTo({center: " + geoLocation.getCoordinates() + ", zoom: 9});");
    }

    public void zoomTo(int zoomLevel)
    {
        page.executeJs("zoomTo(" + zoomLevel + ");");
    }

    public void startAnimation()
    {
        page.executeJs("startAnimation();");
    }

    public void executeJS(String javaScript)
    {
        page.executeJs(javaScript);
    }

    public void drawOriginDestinationFlight(GeoLocation origin, GeoLocation destination)
    {
//        Route route = new Route(origin, destination);
//        Point point = new Point(origin);

        page.executeJs("fromOriginToDestination(" + origin.getCoordinates() + ", " + destination.getCoordinates() + ");");
    }

    public static void main(String[] args)
    {
        Route route = new Route(GeoLocation.Madrid, GeoLocation.Moscow);
        Point point = new Point(GeoLocation.Madrid);

        log.info((route.toString(2)));
        log.info("");
        log.info((point.toString(2)));
    }

    private static class Route extends JSONObject
    {
        public Route(GeoLocation origin, GeoLocation destination)
        {
            this.put("type", "FeatureCollection");

            JSONArray coordinates = new JSONArray();
            coordinates.put(0, origin.getCoordinates());
            coordinates.put(1, destination.getCoordinates());

            JSONObject geometry = new JSONObject();
            geometry.put("type", "LineString");
            geometry.put("coordinates", coordinates);

            JSONObject featureObject = new JSONObject();
            featureObject.put("geometry", geometry);
            featureObject.put("type", "Feature");

            JSONArray features = new JSONArray();
            this.put("features", featureObject);

        }
    }

    private static class Point extends JSONObject
    {
        public Point(GeoLocation origin)
        {
            this.put("type", "FeatureCollection");

            JSONObject geometry = new JSONObject();
            geometry.put("type", "Point");
            geometry.put("coordinates", origin.getCoordinates());

            JSONObject featureObject = new JSONObject();
            featureObject.put("geometry", geometry);
            featureObject.put("type", "Feature");

            JSONArray features = new JSONArray();
            this.put("features", featureObject);
        }

    }

    private String loadAccessToken()
    {
        String token = "";

        // https://www.mkyong.com/java/java-properties-file-examples/
        try (InputStream input = MapboxMap.class.getClassLoader().getResourceAsStream("mapbox.properties"))
        {

            Properties prop = new Properties();

            // load a properties file
            prop.load(input);

            token = prop.getProperty("mapboxgl.accessToken");

            // get the property value and print it out
            System.out.println("Successfully loaded access token from mapbox.properties file.");

        } catch (IOException ex)
        {
            System.err.println("Something went wrong reading properties file: "+ex.getMessage());
            ex.printStackTrace(System.err);
        }

        return token;
    }

}
