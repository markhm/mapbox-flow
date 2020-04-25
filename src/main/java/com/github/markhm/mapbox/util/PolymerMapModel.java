package com.github.markhm.mapbox.util;

import com.github.markhm.mapbox.GeoLocation;
import com.github.markhm.mapbox.encoders.DataEncoder;
import com.github.markhm.mapbox.encoders.GeoLocationEncoder;
import com.github.markhm.mapbox.encoders.LayerEncoder;
import com.github.markhm.mapbox.encoders.SourceEncoder;
import com.vaadin.flow.templatemodel.Encode;
import com.vaadin.flow.templatemodel.TemplateModel;
import mapboxflow.layer.Data;
import mapboxflow.layer.Layer;
import mapboxflow.layer.Source;

public interface PolymerMapModel extends TemplateModel
{
    void setAccessToken(String accessToken);

    @Encode(GeoLocationEncoder.class)
    void setInitialLocation(GeoLocation initialLocation);

    @Encode(LayerEncoder.class)
    void setLayer(Layer layer);

    @Encode(SourceEncoder.class)
    void setSource(Source source);

    // For use in hiding and unhiding
    void setLayerId(String layerId);

    // For (re)setting data
    void setSourceId(String sourceId);

    @Encode(DataEncoder.class)
    void setData(Data data);

    void setZoomLevel(int zoomLevel);
}
