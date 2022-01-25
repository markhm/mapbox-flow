package com.github.markhm.mapbox;

import mapboxflow.jsonobject.layer.Data;
import mapboxflow.jsonobject.layer.Feature;
import mapboxflow.jsonobject.layer.Properties;
import mapboxflow.jsonobject.layer.Source;

/**
 * Extended version of the MapboxMap, which supports easy addition
 * and repositioning of an AnimatedItem, an icon that is positioned
 * on the map and that can be moved.
 */
public class ItemMapboxMap extends MapboxMap {
    public ItemMapboxMap(String accessToken) {
        super(accessToken);
    }

    public ItemMapboxMap(String accessToken, GeoLocation initialLocation) {
        super(accessToken, initialLocation);
    }

    public ItemMapboxMap(String accessToken, GeoLocation initialLocation, int initialZoom) {
        super(accessToken, initialLocation, initialZoom);
    }

    public void addAnimatedItem(AnimatedItem animatedItem) {
        boolean sourceAlreadyExists = true;
        Source source = getSource(animatedItem.getLayerId());

        if (source == null) {
            sourceAlreadyExists = false;
        }

        Properties itemProperties = new Properties(animatedItem.getId(), animatedItem.getDescription(), animatedItem.getSprite().toString());
        Feature itemFeature = new Feature(Feature.FEATURE, itemProperties, animatedItem.getLocation());

        if (sourceAlreadyExists) {
            addToExistingLayer(animatedItem, source, itemFeature);
        } else {
            addToNewLayer(animatedItem, itemFeature);
        }
    }

    public void moveItemToNewPosition(AnimatedItem car, GeoLocation nextPosition) {
        Source source = sourcesInUse.get(car.getLayerId());
        Data data = source.getData();
        Feature feature = data.getFeatureWith(car.getId());
        feature.setLocation(nextPosition);

//        Data newPositionData = new Data(Data.Type.collection);
//        Properties properties = new Properties(car.getId(), car.getSprite().toString());
//        Feature feature = new Feature(Feature.FEATURE, properties, nextPosition);
//        newPositionData.addFeature(feature);

        resetSourceData(car.getLayerId(), data);
    }

}

