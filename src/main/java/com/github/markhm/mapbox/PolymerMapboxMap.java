package com.github.markhm.mapbox;

import com.github.markhm.mapbox.util.PolymerMapModel;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.dependency.*;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import mapboxflow.layer.Data;
import mapboxflow.layer.Layer;
import mapboxflow.layer.Source;

@JavaScript(value = "https://api.tiles.mapbox.com/mapbox-gl-js/v1.9.1/mapbox-gl.js")
@StyleSheet(value = "https://api.tiles.mapbox.com/mapbox-gl-js/v1.9.1/mapbox-gl.css")
@JavaScript(value = "https://api.tiles.mapbox.com/mapbox.js/plugins/turf/v3.0.11/turf.min.js")
@Tag("mapbox-wrapper")
@JsModule("./mapbox/mapbox-wrapper.js")
public class PolymerMapboxMap extends PolymerTemplate<PolymerMapModel> implements HasSize, HasStyle, HasTheme
{
    public PolymerMapboxMap(String accessToken)
    {
        getStyle().set("align-self", "center");
        getStyle().set("border", "1px solid black");

        getModel().setAccessToken(accessToken);
        setId("map");
    }

    public PolymerMapboxMap(String accessToken, GeoLocation initialLocation)
    {
        this(accessToken);
        getModel().setInitialLocation(initialLocation);
    }

    public void addLayer(Layer layer)
    {
        getModel().setLayer(layer);
        getElement().callJsFunction("addLayer");
    }

    public void addSource(Source source)
    {
        getModel().setSource(source);
        getElement().callJsFunction("addSource");
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

    public void resetData(String sourceId, Data data)
    {
        getModel().setSourceId(sourceId);
        getModel().setData(data);
        getElement().callJsFunction("setData");
    }

    public void zoomTo(int zoomLevel)
    {
        getModel().setZoomLevel(zoomLevel);
        getElement().callJsFunction("zoomTo");
    }

    public void deactivateListeners()
    {
        getElement().callJsFunction("removeListeners");
    }


}
