package com.github.markhm.mapbox;

import com.github.markhm.mapbox.AnimatedItem;
import com.github.markhm.mapbox.GeoLocation;
import com.github.markhm.mapbox.Geometry;
import com.github.markhm.mapbox.MapboxMap;
import mapboxflow.layer.Data;
import mapboxflow.layer.Feature;
import mapboxflow.layer.Properties;
import mapboxflow.layer.Source;

public class ItemMapboxMap extends MapboxMap
{
    public ItemMapboxMap(String accessToken)
    {
        super(accessToken);
    }

    public ItemMapboxMap(String accessToken, GeoLocation initialLocation)
    {
        super(accessToken, initialLocation);
    }

    public ItemMapboxMap(String accessToken, GeoLocation initialLocation, int initialZoom)
    {
        super(accessToken, initialLocation, initialZoom);
    }

    public void addAnimatedItem(AnimatedItem animatedItem)
    {
        boolean sourceAlreadyExists = true;
        Source source = getSource(animatedItem.getLayerId());

        if (source == null)
        {
            sourceAlreadyExists = false;
        }

        Properties itemProperties = new Properties(animatedItem.getId(), animatedItem.getDescription(), animatedItem.getSprite().toString());
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

    public void moveItemToNewPosition(AnimatedItem car, GeoLocation nextPosition)
    {
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

