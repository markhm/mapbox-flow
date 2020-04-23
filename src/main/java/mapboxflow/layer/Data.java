package mapboxflow.layer;

import org.json.JSONArray;
import org.json.JSONObject;

public class Data extends JSONObject
{
    private JSONArray features = null;

    private Type type;

    public Data(Type type)
    {
        this.type = type;

        features = new JSONArray();
        if (type.equals(Type.single))
        {
            put("type", "Feature");
        }
        else if (type.equals(Type.collection))
        {
            put("type", "FeatureCollection");
            put("features", features);
        }
    }

    public void addFeature(Feature feature)
    {
        if (type.equals(Type.single))
        {
            put("geometry", feature.getGeometry());
        }
        else if (type.equals(Type.collection))
        {
            int currentLength = features.length();
            features.put(currentLength, feature);
        }
    }

    public void addProperties(Properties properties)
    {
        put("properties", properties);
    }

    public enum Type
    {
        single, collection;
    }

}
