package com.github.markhm.mapbox.encoders;

import com.github.markhm.mapbox.GeoLocation;
import com.vaadin.flow.templatemodel.ModelEncoder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;

public class GeoLocationEncoder implements ModelEncoder<GeoLocation, String>
{
    private static Log log = LogFactory.getLog(GeoLocationEncoder.class);

    @Override
    public String encode(GeoLocation value)
    {
        JSONObject result = new JSONObject();
        result.put("lng", value.getLongitude());
        result.put("lat", value.getLatitude());
        return result.toString();
    }

    @Override
    public GeoLocation decode(String value)
    {
        return GeoLocation.fromLongLat(value);
    }
}
