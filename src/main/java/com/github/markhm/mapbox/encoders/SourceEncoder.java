package com.github.markhm.mapbox.encoders;

import com.vaadin.flow.templatemodel.ModelEncoder;
import mapboxflow.jsonobject.layer.Source;

public class SourceEncoder implements ModelEncoder<Source, String>
{
    @Override
    public String encode(Source value)
    {
        return value.toString();
    }

    @Override
    public Source decode(String value)
    {
        return null;
    }
}
