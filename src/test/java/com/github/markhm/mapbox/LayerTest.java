package com.github.markhm.mapbox;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

public class LayerTest
{
    @Test
    public void testLayerMaker()
    {
        Layer layer = DemoView.getLayer();

        String jsonString = "{" +
                "        \"id\": \"points\"," +
                "        \"type\": \"symbol\"," +
                "        \"source\": {" +
                "            \"type\": \"geojson\"," +
                "            \"data\": {" +
                "                \"type\": \"FeatureCollection\"," +
                "                \"features\": [{" +
                "// feature for Mapbox DC" +
                "                    \"type\": \"Feature\"," +
                "                    \"geometry\": {" +
                "                        \"type\": \"Point\"," +
                "                        \"coordinates\": [-77.03238901390978, 38.913188059745586]" +
                "                    }," +
                "                    \"properties\": {" +
                "                        \"title\": \"Mapbox DC\"," +
                "                        \"icon\": \"monument\"" +
                "                    }" +
                "                }, {" +
                "// feature for Mapbox SF" +
                "                    \"type\": \"Feature\"," +
                "                    \"geometry\": {" +
                "                        \"type\": \"Point\"," +
                "                        \"coordinates\": [-122.414, 37.776]" +
                "                    }," +
                "                    \"properties\": {" +
                "                        \"title\": \"Mapbox SF\"," +
                "                        \"icon\": \"harbor\"" +
                "                    }" +
                "                }]" +
                "            }" +
                "        }," +
                "        \"layout\": {" +
                "            \"icon-image\": [\"concat\", [\"get\", \"icon\"], \"-15\"]," +
                "            \"text-field\": [\"get\", \"title\"]," +
                "            \"text-font\": [\"Open Sans Semibold\", \"Arial Unicode MS Bold\"]," +
                "            \"text-offset\": [0, 0.6]," +
                "            \"text-anchor\": \"top\"" +
                "        }" +
                "    }";

        JSONObject expectedLayer = new JSONObject(jsonString);

        JSONAssert.assertEquals(expectedLayer.toString(), layer.toString(), false);

    }


}
