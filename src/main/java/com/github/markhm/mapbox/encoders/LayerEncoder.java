package com.github.markhm.mapbox.encoders;

import com.github.markhm.mapbox.GeoLocation;
import com.vaadin.flow.templatemodel.ModelEncoder;
import mapboxflow.layer.Layer;
import org.json.JSONObject;

public class LayerEncoder implements ModelEncoder<Layer, String>
{
    @Override
    public String encode(Layer value)
    {
        return value.toString();
    }

    @Override
    public Layer decode(String value)
    {
        return null;
    }
}
