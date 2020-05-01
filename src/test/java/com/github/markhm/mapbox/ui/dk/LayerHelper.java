package com.github.markhm.mapbox.ui.dk;

// import com.github.markhm.mapbox.util.Color;
import mapboxflow.layer.Layer;
import mapboxflow.layer.Paint;
import org.json.JSONArray;

import java.util.List;
import java.util.Set;

public class LayerHelper
{
    public static Layer createBinLayer(String layerId, JSONArray kommuner, String colorString)
    {
        Layer layer = new Layer(layerId, Layer.Type.fill);
        layer.put("source", "danske-kommuner");
        layer.put("source-layer", "Danske_Kommuner");
        Paint paint = new Paint(Paint.Type.fill);

        JSONArray paintArray = new JSONArray();

        JSONArray paintNameArray = new JSONArray();
        paintNameArray.put(0, "get");
        paintNameArray.put(1, "name");

        paintArray.put(0, "match");
        paintArray.put(1, paintNameArray);
        paintArray.put(2, kommuner);
        paintArray.put(3, colorString);
        paintArray.put(4, "hsla(0, 0%, 0%, 0)");

        paint.setFillColor(paintArray);
        layer.setPaint(paint);

        return layer;
    }

    public static JSONArray mapToJSONArray(Set<String> values)
    {
        return mapToJSONArray(values.toArray(new String[values.size()]));
    }

    public static JSONArray mapToJSONArray(String[] values)
    {
        JSONArray resultArray = new JSONArray();

        for (int i=0; i < values.length; i++) {
            resultArray.put(i, values[i]);
        }

        return resultArray;
    }

}
