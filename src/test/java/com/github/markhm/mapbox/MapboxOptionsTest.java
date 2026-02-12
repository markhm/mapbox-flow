package com.github.markhm.mapbox;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.Test;

public class MapboxOptionsTest {
    private static Log log = LogFactory.getLog(MapboxOptionsTest.class);

    public MapboxOptionsTest() {
    }

    @Test
    public void testMapboxOptions() {
        MapboxOptions options = new MapboxOptions();
        options.setCenter(GeoLocation.InitialView_Denmark);
        options.setInitialZoom(6);

        log.info("JSON String: " + options.toString(2));
    }

}
