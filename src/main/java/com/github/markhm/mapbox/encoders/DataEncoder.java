package com.github.markhm.mapbox.encoders;

import com.vaadin.flow.templatemodel.ModelEncoder;
import mapboxflow.layer.Data;
import mapboxflow.layer.Layer;

public class DataEncoder implements ModelEncoder<Data, String>
{
    @Override
    public String encode(Data value)
    {
        return value.toString();
    }

    @Override
    public Data decode(String value)
    {
        return null;
    }
}
