package com.github.markhm.mapbox.util;

import com.github.markhm.mapbox.GeoLocation;
import com.github.markhm.mapbox.encoders.DataEncoder;
import com.github.markhm.mapbox.encoders.GeoLocationEncoder;
import com.github.markhm.mapbox.encoders.LayerEncoder;
import com.github.markhm.mapbox.encoders.SourceEncoder;
import com.vaadin.flow.templatemodel.Encode;
import com.vaadin.flow.templatemodel.TemplateModel;
import mapboxflow.jsonobject.layer.Data;
import mapboxflow.jsonobject.layer.Layer;
import mapboxflow.jsonobject.layer.Source;

public interface PolymerMapModel extends TemplateModel
{
    void setAccessToken(String accessToken);

    @Encode(GeoLocationEncoder.class)
    void setInitialLocation(GeoLocation initialLocation);

    @Encode(LayerEncoder.class)
    void setLayer(Layer layer);

    @Encode(SourceEncoder.class)
    void setSource(Source source);

    @Encode(GeoLocationEncoder.class)
    void setZoomCenter(GeoLocation zoomCenter);
    void setZoomLevel(int zoomLevel);

    // For use in hiding and unhiding
    void setLayerId(String layerId);

    // For (re)setting data
    void setSourceId(String sourceId);

    @Encode(DataEncoder.class)
    void setData(Data data);

    // For the flight example
    @Encode(GeoLocationEncoder.class)
    void setOrigin(GeoLocation origin);
    @Encode(GeoLocationEncoder.class)
    void setDestination(GeoLocation destination);

    void setUrl(String url);
    void setIconName(String iconName);

}
