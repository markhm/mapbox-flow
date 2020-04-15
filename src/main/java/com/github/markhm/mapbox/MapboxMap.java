package com.github.markhm.mapbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.component.page.PendingJavaScriptResult;
import com.vaadin.flow.shared.ui.LoadMode;
import elemental.json.Json;
import elemental.json.JsonObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;

// @Tag("mapbox-wrapper")

@JavaScript(value = "https://api.tiles.mapbox.com/mapbox-gl-js/v1.9.1/mapbox-gl.js",  loadMode = LoadMode.EAGER)
@StyleSheet(value = "https://api.tiles.mapbox.com/mapbox-gl-js/v1.9.1/mapbox-gl.css",  loadMode = LoadMode.EAGER)
@JavaScript(value = "https://api.tiles.mapbox.com/mapbox.js/plugins/turf/v3.0.11/turf.min.js", loadMode = LoadMode.EAGER)
//@StyleSheet("/com/github/markhm/mapbox-flow/mapbox.css")
//@JavaScript("/com/github/markhm/mapbox-flow/mapbox.js")

public class MapboxMap extends Div
{
    private static Log log = LogFactory.getLog(MapboxMap.class);

    private Page page = null;

    boolean alreadyRendered = false;

    private GeoLocation initialView = null;
    private int initialZoom;
    boolean dkMap = false;

    private MapboxOptions options = null;

    private MapboxMap()
    {
        setId("map");
        getStyle().set("align-self", "center");
        getStyle().set("border", "1px solid black");

        setWidth("1200px");
        setHeight("700px");

        page = UI.getCurrent().getPage();
    }

    public MapboxMap(MapboxOptions options)
    {
        this();

        this.options = options;
    }

    public MapboxMap(GeoLocation initialView, int initialZoom)
    {
        this(initialView, initialZoom, false);
    }


    public MapboxMap(GeoLocation initialView, int initialZoom, boolean dkMap)
    {
        this();

        this.initialView = initialView;
        this.initialZoom = initialZoom;
        this.dkMap = dkMap;

        if (!alreadyRendered)
        {
            render(dkMap);
            alreadyRendered = true;
        }
    }

    private void render(boolean dkMap)
    {
//         page.addStyleSheet("frontend://com/github/markhm/mapbox-flow/mapbox.css");
//         page.addJavaScript("frontend://com/github/markhm/mapbox-flow/mapbox.js");

         page.addStyleSheet("./com/github/markhm/mapbox-flow/mapbox.css");
         page.addJavaScript("./com/github/markhm/mapbox-flow/mapbox.js");

        String accessToken = AccessToken.getToken();

        executeJs("mapboxgl.accessToken = '" + accessToken + "';");

        // render mapbox
        options = new MapboxOptions();
        options.setInitialZoom(initialZoom);
        options.setCenter(initialView);

        // This works to create a map, but should not.
        // executeJs("renderDefaultMap(" + initialView.getLongLat() + ", " + initialZoom + ")");
        if (dkMap)
        {
            executeJs("renderDKMap(" + initialView.getLongLat() + ", " + initialZoom + ")");
        }
        else
        {
            executeJs("renderDefaultMap(" + initialView.getLongLat() + ", " + initialZoom + ")");
        }

        // page.executeJs("renderOptionsMap($0)", options.toString());
        // page.executeJs("renderOptionsMap(" + options.toString() + ");");

        // The correct way should be as follows, but this does not work.
        // executeJs("renderCustomMap($0, $1, $2);", getMapStyle(dkMap), initialView.getLongLat(), initialZoom);
        // executeJs("renderCustomMap( " + getMapStyle(dkMap) + ", " + initialView.getLongLat() + ", " + initialZoom + ")"); //" //, getMapStyle(dkMap), initialView.getLongLat(), initialZoom);

        // This does not work, neither does the call that follows.
        // executeJs("renderOptionsMap($0);", getJsonObject());
        // executeJs("renderOptionsMap(" + options + ");");

        // add full screen control
        executeJs("map.addControl(new mapboxgl.FullscreenControl())");
    }

    private String getMapStyle(boolean dkMap)
    {
        if (dkMap)
        {
            return "mapbox://styles/markhm/ck4b4hiy41bmh1ck5ns089mhh";
        }
        else
        {
            return "mapbox://styles/mapbox/streets-v11";
        }
    }

    public PendingJavaScriptResult addAnimatedItem(AnimatedItem animatedItem)
    {
        Layer itemLayer = new Layer(animatedItem.getLayerId(), Layer.Type.symbol);

        GeoLocation initialPosition = animatedItem.getLocation();
        log.info("Adding " + animatedItem.getDescription() + " to initial position " + initialPosition + " on map.");

        Layer.Properties itemProperties = new Layer.Properties(animatedItem.getDescription(), animatedItem.getSprite().toString());
        Layer.Feature itemFeature = new Layer.Feature("Feature", itemProperties, initialPosition);
        itemLayer.addFeature(itemFeature);

        log.info("Feature to be added: " + itemFeature);

        log.info("CARLAYER: "+itemLayer); // see below

        // This should work, but does not
        // executeJs("addLayer($0);", itemLayer.toString());

        // This should not work, but does. Note that .toString() is nót called on the Layer class (which extends JSONObject)
        return executeJs("addLayer(" + itemLayer + ")");
    }

    public PendingJavaScriptResult addLine(Geometry geometry, Color color)
    {
//        JSONObject geometryObject = new JSONObject();
//        geometryObject.put("type", "Point");
//        geometryObject.put("coordinates", geometry.getCoordinates());

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter writer = objectMapper.writerFor(Geometry.class);

        String geometryAsString = null;

        try
        {
            geometryAsString = writer.writeValueAsString(geometry);
        }
        catch (JsonProcessingException jpe)
        {
            log.error(jpe);
        }

        // log.info("geometry as String: "+geometryAsString);

        // This should work, but it doesn't.
        // page.executeJs("addLine($0, $1);", geometryAsString + "", color.getHexValue());

        // This shouldn't work, but it does.
        return page.executeJs("addLine("+geometryAsString +", "+ color.toStringForJS()+")");

        // This should be identical to the previous call, and yes, here it works (NB: in drawOriginDestinationFlight(..) below, it does not).
        // executeJs("addLine("+geometryAsString +", "+ color.toStringForJS()+")");
    }

    public PendingJavaScriptResult removeAnimatedItem(AnimatedItem animatedItem)
    {
        return executeJs("removeLayer($0);", animatedItem.getLayerId());
    }

    public PendingJavaScriptResult zoomTo(GeoLocation geoLocation, int zoomLevel)
    {
        // This should work, but does not
        // executeJs("map.flyTo({center: $0, zoomLevel: $1});", geoLocation.getLongLat(), zoomLevel);

        // This should not work, but does.
        return executeJs("map.flyTo({center: " + geoLocation.getLongLat() + ", zoom: " + zoomLevel + "})");
    }

    public PendingJavaScriptResult flyTo(GeoLocation geoLocation)
    {
        // This should work, but does not
        // executeJs("map.flyTo({center: $0 });", geoLocation.getLongLat());

        // This works
        return executeJs("map.flyTo({center: "+geoLocation.getLongLat()+"})");
    }

    public PendingJavaScriptResult zoomTo(int zoomLevel)
    {
        // This works as expected (wow..!)
        return page.executeJs("zoomTo($0);", zoomLevel);
    }

    public PendingJavaScriptResult startAnimation()
    {
        // This works as expected
        return executeJs("startAnimation();");
    }

    public PendingJavaScriptResult drawOriginDestinationFlight(GeoLocation origin, GeoLocation destination)
    {
        // Expected to work but does not.
        // executeJs("fromOriginToDestination($0, $1);", origin.getLongLat() ,destination.getLongLat());

        // Should not work, but does
        return page.executeJs("fromOriginToDestination(" + origin.getLongLat() + ", " + destination.getLongLat() + ");");

        // Even more strange, the following does not work, which is really identical to the previous method.
        // executeJs("fromOriginToDestination(" + origin.getLongLat() + ", " + origin.getLongLat() + ")");
    }

    private JsonObject getJsonObject()
    {
        JsonObject jsonObject = Json.createObject();

        jsonObject.put(MapboxOptions.OptionType.container.toString(), "map");
        jsonObject.put(MapboxOptions.OptionType.style.toString(), "mapbox://styles/mapbox/streets-v11");


        jsonObject.put(MapboxOptions.OptionType.center.toString(), GeoLocation.InitialView_Denmark.getLongLat());
        jsonObject.put(MapboxOptions.OptionType.zoom.toString(), 6);

        return jsonObject;
    }

    public PendingJavaScriptResult executeJs(String javaScript, Serializable... parameters)
    {
        PendingJavaScriptResult result = page.executeJs(javaScript, parameters);

        return result;
    }

//    public PendingJavaScriptResult executeJs(String javaScript)
//    {
//        return page.executeJs(javaScript);
//    }

    // Documents regarding importing
    // https://github.com/vaadin/flow/issues/6582
    // https://vaadin.com/forum/thread/14045163/how-to-pack-server-side-java-script-in-executable-jar-file
    // https://vaadin.com/docs/v14/flow/importing-dependencies/tutorial-ways-of-importing.html
    // https://vaadin.com/forum/thread/18059914
}

// --------------------------------------------------------------------------------------------------------------------

// DRAGONS here, please ignore

//TODO The problem seems to be that the String returned from initialView.getLongLat() arrives at the other side as a String,
// where it needs to be an object.
// Strangely enough, the toJSON() method does not help sufficiently.
// WE SHOULD PROBABLY USE VAADIN's OWN JSON FRAMEWORK

// --------------------------------------------------------------------------------------------------------------------
//        {
//            "id":"routeLine",
//                "type":"line",
//                "source":
//            {
//                "type":"geojson",
//                    "data":
//                {
//                    "type":"Feature",
//                        "properties":{},
//                    "geometry":
//                    {
//                        "coordinates":[
//                    [55.755825,37.617298],
//                    [55.6761,12.5683]
//                    ],
//                        "type":"LineString"
//                    }
//                }
//            },
//            "layout":{"line-join":"round","line-cap":"round"},
//            "paint":{"line-color":{"line-color":"#377E21"},"line-width":3}
//        }

//        UI ui = getUI().get();
//        ui.access(() ->
//        {
//
//        });

// --------------------------------------------------------------------------------------------------------------------

//    {
//        "layout":
//        {
//            "text-field":["get","title"],
//            "text-offset":[0,0.7],
//            "text-anchor":"top",
//                "icon-image":["concat",["get","icon"],"-15"],
//            "text-font":["Open Sans  Semibold"]
//        },
//        "id":"cars",
//            "source":
//        {
//            "type":"geojson"
//            "data":
//            {
//                "features":
//                        [{"geometry":
//                {
//                    "coordinates":[5.55361,52.026443],
//                    "type":"Point"
//                },
//                "type":"Feature",
//                        "properties": {"icon":"car","title":""}}
//                        ],
//                "type":"FeatureCollection"
//            },
//        },
//        "type":"symbol"
//    }