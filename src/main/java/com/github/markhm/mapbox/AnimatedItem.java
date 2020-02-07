package com.github.markhm.mapbox;

public interface AnimatedItem
{
    /* How the item will be depicted on the map.*/
    Sprite getSprite();

    /* The current GeoLocation for the item. */
    GeoLocation getLocation();
}
