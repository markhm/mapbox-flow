package com.github.markhm.mapbox;

public interface MapItem {
    String getId();

    String getLayerId();

    /* How the item will be depicted on the map.*/
    MapboxSprite getSprite();

    /* The current GeoLocation for the item. */
    GeoLocation getLocation();

    String getDescription();
}
