package com.github.markhm.mapbox.encoders;

import com.vaadin.flow.templatemodel.ModelEncoder;
import mapboxflow.jsonobject.layer.Layer;

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
