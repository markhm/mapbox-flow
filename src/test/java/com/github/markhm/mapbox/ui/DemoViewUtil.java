package com.github.markhm.mapbox.ui;

import com.github.markhm.mapbox.GeoLocation;
import com.github.markhm.mapbox.Sprite;
import com.github.markhm.mapbox.layer.Layer;
import com.github.markhm.mapbox.layer.Properties;
import com.github.markhm.mapbox.layer.Source;

public class DemoViewUtil
{
    public static Layer getExampleLayerAlt()
    {
        Layer layer = new Layer();
        layer.setId("points");
        layer.setType("symbol");

        Properties properties = new Properties();
        properties.setTitle("National Bank");
        properties.setIcon(Sprite.Bank.toString());

        GeoLocation mapboxDCLocation = new GeoLocation(38.913188059745586, -77.03238901390978);

        Source source = new Source();
        source.setType("geojson");

        return layer;
    }

}
