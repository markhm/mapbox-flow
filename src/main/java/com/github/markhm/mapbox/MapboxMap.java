package com.github.markhm.mapbox;

import com.github.markhm.mapbox.methods.FlyToMethod;
import com.github.markhm.mapbox.methods.ZoomToMethod;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.dependency.*;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.page.PendingJavaScriptResult;
import elemental.json.JsonObject;
import mapboxflow.jsonobject.layer.*;
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

    protected Map<String, MapItem> items = new HashMap<>();
    protected Map<String, Source> sourcesInUse = new HashMap<>();
    private MapboxProperties properties;

    public MapboxMap(MapboxProperties properties) {
        this.properties = properties;
        initialize();
    }

    private void initialize() {
        getElement().setProperty("accessToken", properties.getAccessToken());

        if (properties.getInitialLocation() != null) {
            GeoLocation initialLocation = properties.getInitialLocation();
            getElement().setProperty("initialLocation", initialLocation.toJSON());
            getElement().setProperty("lng", initialLocation.getLongitude());
            getElement().setProperty("lat", initialLocation.getLatitude());
        }

        if (properties.getInitialZoom() != 0) {
            getElement().setProperty("zoomLevel", properties.getInitialZoom());
        }

        setId("map");
        getStyle().set("align-self", "center");
        getStyle().set("border", "1px solid black");

        setSizeFull();

        if (properties.isAddFullScreenControl()) {
            getElement().setProperty("isAddFullScreenControl", properties.isAddFullScreenControl());
        }
    }

    public PendingJavaScriptResult executeJs(String command) {
        return getElement().executeJs(command);
    }

    public PendingJavaScriptResult addLayer(JsonObject layer) {
        return getElement().executeJs("this.map.addLayer($0);", layer);
    }

    public PendingJavaScriptResult addLayer(Layer layer) {
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

    public PendingJavaScriptResult deactivateListeners() {
        return getElement().callJsFunction("removeListeners");
    }

    public Source getSource(String layerId) {
        return sourcesInUse.get(layerId);
    }

    public void activatePointerLocation() {
        getElement().callJsFunction("activatePointerLocation");
    }

    protected void addToExistingLayer(MapItem item, Source source, Feature itemFeature) {
        Data data = source.getData();
        data.addFeature(itemFeature);
        resetSourceData(item.getLayerId(), data);
    }

    protected void addToNewLayer(MapItem animatedItem, Feature itemFeature) {
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

    public void addMapItem(MapItem mapItem) {
        assert ! items.containsKey(mapItem.getId());

        boolean sourceAlreadyExists = true;
        Source source = getSource(mapItem.getLayerId());
        if (source == null) {
            sourceAlreadyExists = false;
        }

        Properties itemProperties = new Properties(mapItem.getId(), mapItem.getDescription(), mapItem.getSprite().toString());
        Feature itemFeature = new Feature(Feature.FEATURE, itemProperties, mapItem.getLocation());

        if (sourceAlreadyExists) {
            addToExistingLayer(mapItem, source, itemFeature);
        } else {
            addToNewLayer(mapItem, itemFeature);
        }

        items.put(mapItem.getId(), mapItem);
    }

    public MapItem getMapItem(String id) {
        if (items.containsKey(id)) {
            return items.get(id);
        }
        else {
            return null;
        }
    }

    public void moveItemTo(MapItem mapItem, GeoLocation nextPosition) {
        if (items.get(mapItem.getId()) == null) {
            // item not on map, returning
            return;

        }
        Source source = sourcesInUse.get(mapItem.getLayerId());
        Data data = source.getData();
        Feature feature = data.getFeatureWith(mapItem.getId());
        feature.setLocation(nextPosition);

//        Data newPositionData = new Data(Data.Type.collection);
//        Properties properties = new Properties(mapItem.getId(), mapItem.getSprite().toString());
//        Feature feature = new Feature(Feature.FEATURE, properties, nextPosition);
//        newPositionData.addFeature(feature);

        resetSourceData(mapItem.getLayerId(), data);
    }


    /* ------ ------ ------ ------ ------ ------ ------ ------ ------ ------ ------ ------ ------ ------ ------*/

    public PendingJavaScriptResult zoomTo(int zoomLevel) {
        ZoomToMethod zoomToMethod = new ZoomToMethod(zoomLevel, properties.getZoomToDelay());
        // return getElement().executeJs("this.map.zoomTo(" + zoomLevel + ", { 'duration': " + properties.getZoomToDelay() + " } );");
        return getElement().executeJs(zoomToMethod.toString());
    }

    public PendingJavaScriptResult flyTo(GeoLocation geoLocation, int zoomLevel) {
        FlyToMethod flyToMethod = new FlyToMethod(geoLocation, zoomLevel, properties.getFlyToDelay());
        return getElement().executeJs(flyToMethod.toString());
    }

    public PendingJavaScriptResult flyTo(GeoLocation geoLocation) {
        FlyToMethod flyToMethod = new FlyToMethod(geoLocation,properties.getFlyToDelay());
        return getElement().executeJs(flyToMethod.toString());
    }

}
