package com.github.markhm.mapbox.service;

import com.github.markhm.mapbox.util.Color;
import mapboxflow.ConversionUtil;
import mapboxflow.jsonobject.layer.*;

public class LineBuilder {

    public static Layer getLineLayer(String layerId, com.github.markhm.mapbox.style.Geometry geometry) {
        Layer layer = new Layer(layerId, Layer.Type.line);
        Layout lineLayout = new Layout(Layer.Type.line);
        layer.setLayout(lineLayout);
        layer.setPaint(new Paint(Paint.Type.line, Color.GREEN, 3));

        Geometry convertedGeometry = mapboxflow.ConversionUtil.convert(geometry);

        Source source = new Source();
        Data data = new Data(Data.Type.single);
        Feature feature = new Feature(Feature.FEATURE, null, convertedGeometry);
        data.addFeature(feature);

        source.setData(data);
        layer.setSource(source);

        return layer;
    }

    public static Layer buildLineLayer(String layerId, com.github.markhm.mapbox.style.Geometry geometry, Color color) {
        Layer layer = new Layer(layerId, Layer.Type.line);
        Layout lineLayout = new Layout(Layer.Type.line);
        layer.setLayout(lineLayout);
        layer.setPaint(new Paint(Paint.Type.line, color, 3));

        Geometry convertedGeometry = ConversionUtil.convert(geometry);

        Source source = new Source();
        Data data = new Data(Data.Type.single);
        Feature feature = new Feature(Feature.FEATURE, null, convertedGeometry);

        data.addFeature(feature);
        source.setData(data);
        layer.setSource(source);

        return layer;
    }

}
