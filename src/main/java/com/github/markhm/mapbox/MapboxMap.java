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

    private GeoLocation initialView = null;
    private int initialZoom;

    public MapboxMap(GeoLocation initialView, int initialZoom)
    {
        this.initialView = initialView;
        this.initialZoom = initialZoom;

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

        page.executeJs("renderMapbox(" + initialView.getCoordinates()+ "," + initialZoom + ");");
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
        page.executeJs("fromOriginToDestination(" + origin.getCoordinates() + ", " + destination.getCoordinates() + ");");
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
            // System.out.println("Successfully loaded access token from mapbox.properties file.");

        } catch (IOException ex)
        {
            System.err.println("Something went wrong reading properties file: "+ex.getMessage());
            System.err.println("Did you create an account at Mapbox.com and save your API key in src/main/resources/mapbox.properties...?");
            ex.printStackTrace(System.err);
        }

        return token;
    }
}
