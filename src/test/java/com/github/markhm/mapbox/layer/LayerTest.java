package com.github.markhm.mapbox.layer;

import com.github.markhm.mapbox.util.Color;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.util.ArrayList;
import java.util.List;

public class LayerTest
{
    private static Log log = LogFactory.getLog(LayerTest.class);

    public LayerTest()
    {}

    @Test
    public void createLayer() throws Exception
    {
        // creating layer
        Layer layer = new Layer();
        layer.setId("routeLine");
        layer.setType("line");

        Source source = new Source();
        source.setType("geojson");

        Data data = new Data();
        data.setType("Feature");
        data.setProperties(new Properties());

        Geometry geometry = new Geometry();
        geometry.setType(Type.LINE_STRING);

        List<List<Double>> coordinatesList = new ArrayList<>();

        List<Double> coordinate_1 = new ArrayList<>();
        coordinate_1.add(55.755825);
        coordinate_1.add(37.617298);

        List<Double> coordinate_2 = new ArrayList<>();
        coordinate_2.add(55.6761);
        coordinate_2.add(12.5683);

        coordinatesList.add(coordinate_1);
        coordinatesList.add(coordinate_2);
        geometry.setCoordinates(coordinatesList);

        data.setGeometry(geometry);

        source.setData(data);

        Layout layout = new Layout();
        layout.setLineCap("round");
        layout.setLineJoin("round");

        Paint paint = new Paint();
        paint.setLineColor(Color.RED_LINE);
        paint.setLineWidth(3);

        // adding all to layer
        layer.setLayout(layout);
        layer.setPaint(paint);
        layer.setSource(source);

        String jsonString = Converter.toJsonString(layer);

        log.info("created was: \n"+jsonString);

        JSONAssert.assertEquals(EXPECTED_LAYER, jsonString, JSONCompareMode.LENIENT);
    }

    private static final String EXPECTED_LAYER = "{" +
            "        'id': 'routeLine'," +
            "        'type': 'line'," +
            "        'source': {" +
            "            'type': 'geojson'," +
            "            'data': {" +
            "                'type': 'Feature'," +
            "                'properties': {}," +
            "                'geometry': { 'coordinates': [[55.6761, 12.5683], [55.755825, 37.617298]], " +
            "                   'type':'LineString'}" +
            "            }" +
            "        }," +
            "        'layout': {" +
            "            'line-join': 'round'," +
            "            'line-cap': 'round'" +
            "        }," +
            "        'paint': {" +
            "            'line-color': '#d54648'," +
            "            'line-width': 3" +
            "        }\n" +
            "    };";

}
