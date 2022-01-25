package com.github.markhm.mapbox;

public interface AnimatedItem {
    String getId();

    String getLayerId();

    /* How the item will be depicted on the map.*/
    Sprite getSprite();

    /* The current GeoLocation for the item. */
    GeoLocation getLocation();

    String getDescription();
}
