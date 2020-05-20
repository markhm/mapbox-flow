package mapboxflow.elemental.json;

import elemental.json.Json;
import elemental.json.JsonArray;
import elemental.json.JsonObject;

public class DataHelper
{
    public static JsonObject createData(Type type)
    {
        JsonArray features = Json.createArray();

        JsonObject data = Json.createObject();

        if (type.equals(Type.single))
        {
            data.put("type", "Feature");
        }
        else if (type.equals(Type.collection))
        {
            data.put("type", "FeatureCollection");
            data.put("features", features);
        }

        return data;
    }

    public static void addGeometry(JsonObject data, JsonObject geometry)
    {
        String type = data.getString("type");
        if (type.equals("Feature"))
        {
            data.put("geometry", geometry);
        }
        else if (type.equals("FeatureCollection"))
        {
            throw new RuntimeException("Not supported");
        }
    }

    public static void addFeature(JsonObject data, JsonObject feature)
    {
        String type = data.getString("type");
        if (type.equals("Feature"))
        {
            data.put("geometry", FeatureHelper.getGeometry(feature));
            // data.put("geometry", feature);
        }
        else if (type.equals("FeatureCollection"))
        {
            JsonArray features = data.getArray("features");
            int currentLength = features.length();
            features.set(currentLength, feature);
        }
    }


    public enum Type
    {
        single, collection;
    }

}
