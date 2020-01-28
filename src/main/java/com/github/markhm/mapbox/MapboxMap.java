package com.github.markhm.mapbox;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.shared.ui.LoadMode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

//@Tag("map")
@JsModule("mapbox.js")
@CssImport("mapbox.css")
// @JavaScript("https://raw.githubusercontent.com/markhm/mapbox-flow/master/src/main/resources/META-INF/resources/frontend/mapbox.js")
// @CssImport("https://raw.githubusercontent.com/markhm/mapbox-flow/master/src/main/resources/META-INF/resources/frontend/mapbox.css")
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
//        String jsFileLocation = AccessToken.getJSFileLocation();
//        String cssLocation = AccessToken.getCSSFileLocation();

        page.addStyleSheet("https://api.tiles.mapbox.com/mapbox-gl-js/v1.6.0/mapbox-gl.css");


        // TODO Here it is
        // https://github.com/vaadin/flow/issues/6582
        // https://vaadin.com/forum/thread/14045163/how-to-pack-server-side-java-script-in-executable-jar-file
        // https://vaadin.com/docs/v14/flow/importing-dependencies/tutorial-ways-of-importing.html
        // https://vaadin.com/forum/thread/18059914

        page.addJavaScript("https://api.tiles.mapbox.com/mapbox-gl-js/v1.6.0/mapbox-gl.js");
        page.addJavaScript("https://api.tiles.mapbox.com/mapbox.js/plugins/turf/v2.0.0/turf.min.js");

        String accessToken = AccessToken.getToken();
        // page.addJavaScript(jsFileLocation);

//        page.addStyleSheet("https://raw.githubusercontent.com/markhm/mapbox-flow/master/src/main/resources/META-INF/resources/frontend/mapbox.css");
//        page.addJavaScript("https://raw.githubusercontent.com/markhm/mapbox-flow/master/src/main/resources/META-INF/resources/frontend/mapbox.js");

        page.executeJs("mapboxgl.accessToken = '" + accessToken + "';");
        page.executeJs("renderMapbox(" + initialView.getLongLat()+ "," + initialZoom + ");");

        // add full screen control
        page.executeJs("map.addControl(new mapboxgl.FullscreenControl());");
    }

    public void flyTo(GeoLocation geoLocation)
    {
        page.executeJs("map.flyTo({center: " + geoLocation.getLongLat() + ", zoom: 9});");
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

}
