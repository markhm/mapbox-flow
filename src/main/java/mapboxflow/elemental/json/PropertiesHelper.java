package mapboxflow.elemental.json;

import elemental.json.Json;
import elemental.json.JsonObject;

public class PropertiesHelper {
    public static JsonObject createProperties(String id, String title, String icon) {
        JsonObject properties = Json.createObject();

        properties.put("id", id);
        properties.put("title", title);
        properties.put("icon", icon);

        return properties;
    }

    public static JsonObject createProperties() {
        JsonObject properties = Json.createObject();
        return properties;
    }
}
