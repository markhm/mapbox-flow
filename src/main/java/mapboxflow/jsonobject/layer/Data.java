package mapboxflow.jsonobject.layer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;

public class Data extends JSONObject implements Serializable {
    private static Log log = LogFactory.getLog(Data.class);

    private JSONArray features = null;

    private Type type;

    public Data(Type type) {
        this.type = type;

        features = new JSONArray();
        if (type.equals(Type.single)) {
            put("type", "Feature");
        } else if (type.equals(Type.collection)) {
            put("type", "FeatureCollection");
            put("features", features);
        }
    }

    public void addFeature(Feature feature) {
        if (type.equals(Type.single)) {
            put("geometry", feature.getGeometry());
        } else if (type.equals(Type.collection)) {
            int currentLength = features.length();
            features.put(currentLength, feature);
        }
    }

    public Feature getFeatureWith(String id) {
        for (int i = 0; i < features.length(); i++) {
            Feature feature = (Feature) features.get(i);
            String featureId = feature.getId();
            if (id.equals(featureId)) {
                return feature;
            }
        }
        log.error("Could not find feature with id: " + id + ", returning null.");
        return null;
    }

    public void addProperties(Properties properties) {
        put("properties", properties);
    }

    public enum Type {
        single, collection;
    }

}
