package com.github.markhm.mapbox;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.dependency.*;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.page.PendingJavaScriptResult;
import elemental.json.JsonObject;
import mapboxflow.jsonobject.layer.Feature;
import mapboxflow.jsonobject.layer.Layer;
import mapboxflow.jsonobject.layer.Source;
import mapboxflow.jsonobject.layer.Data;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.Map;

@JavaScript(value = "https://api.mapbox.com/mapbox-gl-js/v2.6.1/mapbox-gl.js")
@StyleSheet(value = "https://api.mapbox.com/mapbox-gl-js/v2.6.1/mapbox-gl.css")
@JavaScript(value = "https://unpkg.com/@turf/turf@6/turf.min.js")
@Tag("mapbox-wrapper")
@JsModule("./mapbox/mapbox-wrapper.js")
public class MapboxMap extends LitTemplate implements HasSize, HasStyle, HasTheme {
    private static Log log = LogFactory.getLog(MapboxMap.class);

    protected Map<String, Source> sourcesInUse = new HashMap<>();

    private String accessToken = null;
    private GeoLocation initialLocation = null;
    private int initialZoom = 0;

    private GeoLocation origin = null;
    private GeoLocation destination = null;

    public MapboxMap(String accessToken) {
        this.accessToken = accessToken;
        getElement().setProperty("accessToken", accessToken);

        setId("map");
        getStyle().set("align-self", "center");
        getStyle().set("border", "1px solid black");

        setSizeFull();
    }

    public MapboxMap(String accessToken, GeoLocation initialLocation) {
        this(accessToken);
        this.initialLocation = initialLocation;

        getElement().setProperty("initialLocation", initialLocation.toJSON());
        getElement().setProperty("lng", initialLocation.getLongitude());
        getElement().setProperty("lat", initialLocation.getLatitude());
    }

    public MapboxMap(String accessToken, GeoLocation initialLocation, int initialZoom) {
        this(accessToken, initialLocation);
        this.initialZoom = initialZoom;

        getElement().setProperty("zoomLevel", initialZoom);
    }

    public void addFullScreenControl() {
        getElement().callJsFunction("addFullScreenControl");
    }

    public PendingJavaScriptResult executeJs(String command) {
        return getElement().executeJs(command);
    }

    public PendingJavaScriptResult addLayer(JsonObject layer) {
        return getElement().executeJs("this.map.addLayer($0);", layer);
    }

    public PendingJavaScriptResult addLayer(Layer layer) {
        // This works, but when called rapidly in series, it does not:
        // getModel().setLayer(layer);
        // getElement().callJsFunction("addLayer");

        // This does not work:
        // getElement().executeJs("addLayerArgumented($0);", layer.toString());

        // This does not work either:
        // getElement().executeJs("this.map.addLayer($0);", layer.toString());

        // This works:
        return getElement().executeJs("this.map.addLayer(" + layer.toString() + ");");
    }

    public PendingJavaScriptResult addSource(String sourceId, JsonObject source) {
        PendingJavaScriptResult result = null;

        if (!sourcesInUse.keySet().contains(sourceId)) {
            result = getElement().executeJs("this.map.addSource($0, $1);", sourceId, source);
            // sourcesInUse.put(sourceId, source);
        } else {
            log.warn("Source " + sourceId + " already exists, ignoring request");
        }
        return result;
    }

    public PendingJavaScriptResult addSource(String sourceId, Source source) {
        PendingJavaScriptResult result = null;
        if (!sourcesInUse.keySet().contains(sourceId)) {
//            getModel().setSourceId(sourceId);
//            getModel().setSource(source);
//            getElement().callJsFunction("addSource");

            result = getElement().executeJs("this.map.addSource('" + sourceId + "', " + source + ")");

            sourcesInUse.put(sourceId, source);
        } else {
            log.warn("Source " + sourceId + " already exists, ignoring request");
        }

        return result;
    }

    public PendingJavaScriptResult hideLayer(String layerId) {
        // getModel().setLayerId(layerId);
        // getElement().callJsFunction("hideLayer");

        return getElement().executeJs("this.map.setFilter('" + layerId + "', ['==', 'type', 'Feature']);");
    }

    public PendingJavaScriptResult unhideLayer(String layerId) {
//        getModel().setLayerId(layerId);
//        getElement().callJsFunction("unhideLayer");

        return getElement().executeJs("this.map.setFilter('" + layerId + "', null);");
    }

    public PendingJavaScriptResult resetSourceData(String sourceId, Data data) {
//        getModel().setSourceId(sourceId);
//        getModel().setData(data);
//        getElement().callJsFunction("resetData");

        return getElement().executeJs("this.map.getSource('" + sourceId + "').setData(" + data + ");");
    }

    public PendingJavaScriptResult zoomTo(int zoomLevel) {
//        getModel().setZoomLevel(zoomLevel);
//        getElement().callJsFunction("zoomTo");

        return getElement().executeJs("this.map.zoomTo(" + zoomLevel + ", { 'duration': 1500 } );");
    }

    public PendingJavaScriptResult flyTo(GeoLocation geoLocation, int zoomLevel) {
//        getModel().setZoomLevel(zoomLevel);
//        getModel().setZoomCenter(geoLocation);
//        getElement().callJsFunction("flyTo");

        return getElement().executeJs("this.map.flyTo({center: " + geoLocation + ", zoom: " +
                zoomLevel + ", duration: 1500});");
    }

    public PendingJavaScriptResult flyTo(GeoLocation geoLocation) {
//        getModel().setZoomCenter(geoLocation);
//        getElement().callJsFunction("flyTo");

        // this.map.flyTo({center: JSON.parse(this.zoomCenter), duration: 1500});

        return getElement().executeJs("this.map.flyTo({center: " + geoLocation + ", duration: 1500});");
    }

    public PendingJavaScriptResult deactivateListeners() {
        return getElement().callJsFunction("removeListeners");
    }

    public Source getSource(String layerId) {
        return sourcesInUse.get(layerId);
    }

    public void activatePointerLocation() {
        getElement().callJsFunction("activatePointerLocation");
    }

    protected void addToExistingLayer(AnimatedItem animatedItem, Source source, Feature itemFeature) {
        Data data = source.getData();
        data.addFeature(itemFeature);
        resetSourceData(animatedItem.getLayerId(), data);
    }

    protected void addToNewLayer(AnimatedItem animatedItem, Feature itemFeature) {
        Data data = new Data(Data.Type.collection);
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

    public void drawOriginDestinationFlight(GeoLocation origin, GeoLocation destination) {
        // Expected to work but does not.
        // executeJs("fromOriginToDestination($0, $1);", origin.getLongLat() ,destination.getLongLat());

        this.origin = origin;
        this.destination = destination;
        getElement().callJsFunction("fromOriginToDestination");

        // Should not work, but does
        // return page.executeJs("fromOriginToDestination(" + origin.getLongLat() + ", " + destination.getLongLat() + ");");

        // Even more strange, the following does not work, which is really identical to the previous method.
        // executeJs("fromOriginToDestination(" + origin.getLongLat() + ", " + origin.getLongLat() + ")");
    }

    public void addImage(String url, String iconName) {
        // getModel().setUrl(url);
        // getModel().setIconName(iconName);

        getElement().callJsFunction("loadIcon");
    }

}
