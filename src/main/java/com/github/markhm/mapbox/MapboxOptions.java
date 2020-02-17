package com.github.markhm.mapbox;

import org.json.JSONObject;

// https://docs.mapbox.com/mapbox-gl-js/api/#map

public class MapboxOptions extends JSONObject
{
    public MapboxOptions()
    {
        // defaults go here
        put(OptionType.container.toString(), "map");
        put(OptionType.style.toString(), "mapbox://styles/mapbox/streets-v11");
    }

    public void setContainer(String container)
    {
        put(OptionType.container.toString(), container);
    }

    public String getContainer()
    {
        return getString(OptionType.container.toString());
    }

    public void setCenter(GeoLocation initialView)
    {
        put(OptionType.center.toString(), initialView);
    }

    public GeoLocation getInitialView()
    {
        return GeoLocation.fromLongLat(getString(OptionType.center.toString()));
    }

    public int getInitialZoom()
    {
        return getInt(OptionType.zoom.toString());
    }

    public void setInitialZoom(int initialZoom)
    {
        put(OptionType.zoom.toString(), initialZoom);
    }

    public String toString()
    {
        return super.toString();
    }

    public enum OptionType
    {
        center, // center location
        container,
        style,
        zoom // initial zoom
        ;
    }

}
