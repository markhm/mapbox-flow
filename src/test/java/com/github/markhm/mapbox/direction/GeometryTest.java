package com.github.markhm.mapbox.direction;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.github.markhm.mapbox.directions.DirectionsResponse;
import com.github.markhm.mapbox.directions.Geometry;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.Test;

public class GeometryTest
{
    private static Log log = LogFactory.getLog(GeometryTest.class);

    public GeometryTest()
    {}

    @Test
    public void compareGeometries()
    {
//        DeprecatedDirectionsResponse deprecatedDirectionsResponse = DeprecatedDirectionsResponse.getInstance();
//        JSONObject jsonObject = deprecatedDirectionsResponse.getGeometry();

        DirectionsResponse directionsResponse = DirectionsResponse.getInstance();
        Geometry geometry = directionsResponse.getRoutes().get(0).getGeometry();

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter writer = objectMapper.writerFor(Geometry.class);

        String stringValue = null;

        try
        {
            stringValue = writer.writeValueAsString(geometry);
        }
        catch (JsonProcessingException jpe)
        {
            log.error(jpe);
        }

        log.warn(stringValue);
        // JSONAssert.assertEquals(jsonObject.toString(), stringValue, JSONCompareMode.LENIENT);
    }

}
