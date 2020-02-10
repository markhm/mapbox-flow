package com.github.markhm.mapbox;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.Test;

public class MapboxOptionsTest
{
    private static Log log = LogFactory.getLog(MapboxMap.class);

    public MapboxOptionsTest()
    {
    }

    @Test
    public void testMapboxOptions()
    {
        MapboxOptions options = new MapboxOptions();
        options.setInitialView(GeoLocation.InitialView_Denmark);
        options.setInitialZoom(6);

        log.info("JSON String: " + options.toString(2));
    }

}
