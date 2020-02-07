package com.github.markhm.mapbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.github.markhm.mapbox.directions.Geometry;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.page.Page;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


//@Tag("map")
//@JavaScript("./mapbox.js")
//@CssImport("./mapbox.css")
public class MapboxMap extends Div
{
    private static Log log = LogFactory.getLog(MapboxMap.class);

    private Page page = null;

    boolean alreadyRendered = false;

    private GeoLocation initialView = null;
    private int initialZoom;

    public MapboxMap(GeoLocation initialView, int initialZoom)
    {
        // log.info("Entering Mapbox(..) constructor");
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
        // mapbox://styles/markhm/ck63rxdzn0zgq1iomoub9gk91

        // log.info("About to render()");
//        String jsFileLocation = AccessToken.getJSFileLocation();
//        String cssLocation = AccessToken.getCSSFileLocation();

        page.addStyleSheet("https://api.tiles.mapbox.com/mapbox-gl-js/v1.6.0/mapbox-gl.css");
        page.addJavaScript("https://api.tiles.mapbox.com/mapbox-gl-js/v1.6.0/mapbox-gl.js");
        page.addJavaScript("https://api.tiles.mapbox.com/mapbox.js/plugins/turf/v2.0.0/turf.min.js");

        page.addStyleSheet("./mapbox.css");
        page.addJavaScript("./mapbox.js");

        String accessToken = AccessToken.getToken();

        page.executeJs("mapboxgl.accessToken = '" + accessToken + "';");

        // render mapbox
        page.executeJs("renderMapbox(" + initialView.getLongLat()+ "," + initialZoom + ");");

        // add full screen control
        page.executeJs("map.addControl(new mapboxgl.FullscreenControl());");
    }

    public void addAnimatedItem(AnimatedItem animatedItem)
    {
        Layer carLayer = new Layer(animatedItem.getLayerId(), "symbol");

        GeoLocation initialPosition = animatedItem.getLocation();
        log.info("Adding " + animatedItem.getDescription() + " to initial position " + initialPosition + " on map.");

        Layer.Properties carProperties = new Layer.Properties("", animatedItem.getSprite().toString());
        Layer.Feature carFeature = new Layer.Feature("Feature", carProperties, initialPosition);
        carLayer.addFeature(carFeature);

        executeJS("addLayer(" + carLayer + ");");

//        UI ui = getUI().get();
//        ui.access(() ->
//        {
//
//        });
    }

    public void addLine(Geometry geometry, Color color)
    {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter writer = objectMapper.writerFor(Geometry.class);

        String stringValue = null;

        try
        {
            stringValue = writer.writeValueAsString(geometry);
        }
        catch (JsonProcessingException jpe)
        {
            log.error(jpe);
        }

        executeJS("addLine(" + stringValue + ", " + color.toStringForJS() + ");");
    }

    public void removeAnimatedItem(AnimatedItem animatedItem)
    {
        executeJS("removeLayer(" + animatedItem.getLayerId() + ");");
    }

    public void zoomTo(GeoLocation geoLocation, int zoomLevel)
    {
        page.executeJs("map.flyTo({center: " + geoLocation.getLongLat() + ", zoom: " + zoomLevel + "});");
    }

    public void zoomTo(GeoLocation geoLocation)
    {
        page.executeJs("map.flyTo({center: " + geoLocation.getLongLat() + "});");
        // zoomTo(geoLocation, 9);
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
        page.executeJs("fromOriginToDestination(" + origin.getLongLat() + ", " + destination.getLongLat() + ");");
    }

    // Documents regarding importing
    // https://github.com/vaadin/flow/issues/6582
    // https://vaadin.com/forum/thread/14045163/how-to-pack-server-side-java-script-in-executable-jar-file
    // https://vaadin.com/docs/v14/flow/importing-dependencies/tutorial-ways-of-importing.html
    // https://vaadin.com/forum/thread/18059914

}
