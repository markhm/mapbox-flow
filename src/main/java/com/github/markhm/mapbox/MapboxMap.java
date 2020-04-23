package com.github.markhm.mapbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.github.markhm.mapbox.util.Color;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.component.page.PendingJavaScriptResult;
import elemental.json.Json;
import elemental.json.JsonObject;
import mapboxflow.layer.Data;
import mapboxflow.layer.Feature;
import mapboxflow.layer.Layer;
import mapboxflow.layer.Properties;
import mapboxflow.layer.Source;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JavaScript(value = "https://api.tiles.mapbox.com/mapbox-gl-js/v1.9.1/mapbox-gl.js")
@StyleSheet(value = "https://api.tiles.mapbox.com/mapbox-gl-js/v1.9.1/mapbox-gl.css")
@JavaScript(value = "https://api.tiles.mapbox.com/mapbox.js/plugins/turf/v3.0.11/turf.min.js")
// @JsModule("./com/github/markhm/mapbox-flow/mapbox.js")
public class MapboxMap extends Div
{
    private static Log log = LogFactory.getLog(MapboxMap.class);

    private Page page = null;

    boolean alreadyRendered = false;

    private GeoLocation initialView = null;
    private int initialZoom;
    boolean dkMap = false;

    private MapboxOptions options = null;

    private Map<String, Source> sourcesInUse = new HashMap<>();

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

    public Source getSource(String layerId)
    {
        return sourcesInUse.get(layerId);
    }

    public String addAnimatedItem(AnimatedItem animatedItem)
    {
        boolean sourceAlreadyExists = true;
        Source source = getSource(animatedItem.getLayerId());

        if (source == null)
        {
            sourceAlreadyExists = false;
        }

        Properties itemProperties = new Properties(animatedItem.getDescription(), animatedItem.getSprite().toString());
        Feature itemFeature = new Feature(Feature.FEATURE, itemProperties, animatedItem.getLocation());

        String command;
        if (sourceAlreadyExists)
        {
            command = addToExistingLayer(animatedItem, source, itemFeature);
        }
        else
        {
            command = addToNewLayer(animatedItem, itemFeature);
        }

        return command;
    }

    public void unhideLayer(String layerId)
    {
        executeJs("unhideLayer('" + layerId + "')");
    }

    public void hideLayer(String layerId)
    {
        executeJs("hideLayer('" + layerId + "')");
    }

    public String resetSourceData(String sourceId, Data newData)
    {
        String command = "setData('"+sourceId+"', " + newData + ")";
        executeJs(command);

        return command;
    }

    private String addToExistingLayer(AnimatedItem animatedItem, Source source, Feature itemFeature)
    {
        mapboxflow.layer.Data data = source.getData();
        data.addFeature(itemFeature);
        String command = "setData('"+animatedItem.getLayerId()+"'," + data + ")";
        executeJs(command);

        return command;
    }

    private String addToNewLayer(AnimatedItem animatedItem, Feature itemFeature)
    {
        mapboxflow.layer.Data data = new mapboxflow.layer.Data(Data.Type.collection);
        data.addFeature(itemFeature);
        Source source = new Source();
        source.setData(data);

        addSource(animatedItem.getLayerId(), source);

        Layer itemLayer = new Layer(animatedItem.getLayerId(), Layer.Type.symbol);
        itemLayer.setSourceId(animatedItem.getLayerId());

        addLayer(itemLayer);

        return "no comment";
    }

    public void addSource(String id, Source source)
    {
        if (!sourcesInUse.keySet().contains(id))
        {
            executeJs("addSource('" + id + "'," + source + ")");

            sourcesInUse.put(id, source);
        }
        else
        {
            log.warn("Source "+id+" already exists, ignoring request");
        }
    }

    public PendingJavaScriptResult addLayerAlt(JsonObject layer)
    {
        return executeJs("addLayer(" + layer.toJson() + ")");
        // return executeJs("addLayer($0)", layer);
    }

    public PendingJavaScriptResult addLayer(Layer layer)
    {
        String command = "addLayer(" + layer + ")";
        return executeJs(command);
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

        // return executeJs("addLine("+geometry +", "+ color.toStringForJS()+")");
        return executeJs("addLine("+geometryAsString +", "+ color.toStringForJS()+")");

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

//    public String addAnimatedItem(AnimatedItem animatedItem)
//    {
//        boolean layerAlreadyExists = true;
//        Layer itemLayer = getLayer(animatedItem.getLayerId());
//
//        if (itemLayer == null)
//        {
//            layerAlreadyExists = false;
//            itemLayer = new Layer(animatedItem.getLayerId(), Layer.Type.symbol);
//        }
//
//        GeoLocation initialPosition = animatedItem.getLocation();
//        log.info("Adding " + animatedItem.getDescription() + " to initial position " + initialPosition + " on map.");
//        Layer.Properties itemProperties = new Layer.Properties(animatedItem.getDescription(), animatedItem.getSprite().toString());
//        Layer.Feature itemFeature = new Layer.Feature("Feature", itemProperties, initialPosition);
//
//        itemLayer.addFeature(itemFeature);
//
//        String command = null;
//        if (layerAlreadyExists)
//        {
//            Layer.Data data = itemLayer.getSource().getData();
//            command = "setData('"+animatedItem.getLayerId()+"_source'," + data + ")";
//        }
//        else
//        {
//            command = "addLayer(" + itemLayer + ")";
//            layersInUse.put(animatedItem.getLayerId(), itemLayer);
//        }
//
//        executeJs(command);
//        return command;
//
//        // This should work, but does not
//        // executeJs("addLayer($0);", itemLayer.toString());
//
//        // This should not work, but does. Note that .toString() is nót called on the Layer class (which extends JSONObject)
//        // return executeJs("addLayer(" + itemLayer + ")");
//    }
//
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