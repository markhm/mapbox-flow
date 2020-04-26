package com.github.markhm.mapbox;

import com.github.markhm.mapbox.util.PolymerMapModel;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.dependency.*;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import mapboxflow.layer.*;
import mapboxflow.layer.Data;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.Map;

@JavaScript(value = "https://api.tiles.mapbox.com/mapbox-gl-js/v1.9.1/mapbox-gl.js")
@StyleSheet(value = "https://api.tiles.mapbox.com/mapbox-gl-js/v1.9.1/mapbox-gl.css")
@JavaScript(value = "https://api.tiles.mapbox.com/mapbox.js/plugins/turf/v3.0.11/turf.min.js")
@Tag("mapbox-wrapper")
@JsModule("./mapbox/mapbox-wrapper.js")
public class MapboxMap extends PolymerTemplate<PolymerMapModel> implements HasSize, HasStyle, HasTheme
{
    private static Log log = LogFactory.getLog(MapboxMap.class);

    private Map<String, Source> sourcesInUse = new HashMap<>();

    public MapboxMap(String accessToken)
    {
        setId("map");
        getStyle().set("align-self", "center");
        getStyle().set("border", "1px solid black");

        setSizeFull();

        getModel().setAccessToken(accessToken);
    }

    public MapboxMap(String accessToken, GeoLocation initialLocation)
    {
        this(accessToken);
        getModel().setInitialLocation(initialLocation);
    }

    public MapboxMap(String accessToken, GeoLocation initialLocation, int initialZoom)
    {
        this(accessToken);
        getModel().setInitialLocation(initialLocation);
        getModel().setZoomLevel(initialZoom);
    }

    public void executeJs(String command)
    {
        getElement().executeJs(command);
    }

    public void addLayer(Layer layer)
    {
        getModel().setLayer(layer);
        getElement().callJsFunction("addLayer");
    }

    public void addSource(String sourceId, Source source)
    {
        if (!sourcesInUse.keySet().contains(sourceId))
        {
            getModel().setSourceId(sourceId);
            getModel().setSource(source);
            getElement().callJsFunction("addSource");

            sourcesInUse.put(sourceId, source);
        }
        else
        {
            log.warn("Source "+sourceId+" already exists, ignoring request");
        }
    }

    public void hideLayer(String layerId)
    {
        getModel().setLayerId(layerId);
        getElement().callJsFunction("hideLayer");
    }

    public void unhideLayer(String layerId)
    {
        getModel().setLayerId(layerId);
        getElement().callJsFunction("unhideLayer");
    }

    public void resetSourceData(String sourceId, Data data)
    {
        getModel().setSourceId(sourceId);
        getModel().setData(data);
        getElement().callJsFunction("resetData");
    }

    public void zoomTo(int zoomLevel)
    {
        getModel().setZoomLevel(zoomLevel);
        getElement().callJsFunction("zoomTo");
    }

    public void flyTo(GeoLocation geoLocation, int zoomLevel)
    {
        getModel().setZoomLevel(zoomLevel);
        getModel().setZoomCenter(geoLocation);
        getElement().callJsFunction("flyTo");
    }

    public void flyTo(GeoLocation geoLocation)
    {
        getModel().setZoomCenter(geoLocation);
        getElement().callJsFunction("flyTo");
    }

    public void deactivateListeners()
    {
        getElement().callJsFunction("removeListeners");
    }

    public Source getSource(String layerId)
    {
        return sourcesInUse.get(layerId);
    }

    public void addAnimatedItem(AnimatedItem animatedItem)
    {
        boolean sourceAlreadyExists = true;
        Source source = getSource(animatedItem.getLayerId());

        if (source == null)
        {
            sourceAlreadyExists = false;
        }

        Properties itemProperties = new Properties(animatedItem.getDescription(), animatedItem.getSprite().toString());
        Feature itemFeature = new Feature(Feature.FEATURE, itemProperties, animatedItem.getLocation());

        if (sourceAlreadyExists)
        {
            addToExistingLayer(animatedItem, source, itemFeature);
        }
        else
        {
            addToNewLayer(animatedItem, itemFeature);
        }
    }

    public void activatePointerLocation()
    {
        getElement().callJsFunction("activatePointerLocation");
    }

    private void addToExistingLayer(AnimatedItem animatedItem, Source source, Feature itemFeature)
    {
        mapboxflow.layer.Data data = source.getData();
        data.addFeature(itemFeature);
        resetSourceData(animatedItem.getLayerId(), data);
    }

    private void addToNewLayer(AnimatedItem animatedItem, Feature itemFeature)
    {
        mapboxflow.layer.Data data = new mapboxflow.layer.Data(Data.Type.collection);
        data.addFeature(itemFeature);
        Source source = new Source();
        source.setData(data);

        addSource(animatedItem.getLayerId(), source);

        Layer itemLayer = new Layer(animatedItem.getLayerId(), Layer.Type.symbol);
        itemLayer.setSourceId(animatedItem.getLayerId());

        addLayer(itemLayer);
    }

    // --- // --- // --- // --- // --- // --- // --- // --- // --- // --- // --- // --- // --- // --- // ---
    // EXAMPLES here

    public void drawOriginDestinationFlight(GeoLocation origin, GeoLocation destination)
    {
        // Expected to work but does not.
        // executeJs("fromOriginToDestination($0, $1);", origin.getLongLat() ,destination.getLongLat());

        getModel().setOrigin(origin);
        getModel().setDestination(destination);
        getElement().callJsFunction("fromOriginToDestination");

        // Should not work, but does
        // return page.executeJs("fromOriginToDestination(" + origin.getLongLat() + ", " + destination.getLongLat() + ");");

        // Even more strange, the following does not work, which is really identical to the previous method.
        // executeJs("fromOriginToDestination(" + origin.getLongLat() + ", " + origin.getLongLat() + ")");
    }
}
