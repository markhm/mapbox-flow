package com.github.markhm.mapbox.layer;

import com.github.markhm.mapbox.Color;
import com.github.markhm.mapbox.Data;
import com.github.markhm.mapbox.Geometry;
import org.junit.jupiter.api.Test;

public class LayerTest
{
    public LayerTest()
    {}

    @Test
    public void createLayer()
    {
        // creating layer
        Layer layer = new Layer();
        layer.setID("routeLine");
        layer.setType("line");

        Source source = new Source();
        source.setType("geojson");

        Data data = new Data();
        data.setType("Feature");
        data.setProperties(new Properties());

        source.setData(data);

        Geometry geometry = new Geometry();

        data.setGeometry(geometry);

        Layout layout = new Layout();
        layout.setLineCap("round");
        layout.setLineJoin("round");

        Paint paint = new Paint();
        paint.setLineColor(Color.GREEN);
        paint.setLineWidth(3);

        // adding all to layer
        layer.setLayout(layout);
        layer.setPaint(paint);
        layer.setSource(source);
    }


    private static final String EXPECTED_LAYER = "{\n" +
            "        'id': 'routeLine',\n" +
            "        'type': 'line',\n" +
            "        'source': {\n" +
            "            'type': 'geojson',\n" +
            "            'data': {\n" +
            "                'type': 'Feature',\n" +
            "                'properties': {},\n" +
            "                'geometry': geometry\n" +
            "            }\n" +
            "        },\n" +
            "        'layout': {\n" +
            "            'line-join': 'round',\n" +
            "            'line-cap': 'round'\n" +
            "        },\n" +
            "        'paint': {\n" +
            "            'line-color': '#d54648',\n" +
            "            'line-width': 3\n" +
            "        }\n" +
            "    };";

}
