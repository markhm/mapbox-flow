package mapboxflow.jsonobject.layer;

import org.json.JSONObject;

import java.io.Serializable;

public class Properties extends JSONObject implements Serializable {
    public Properties() {
    }

    public Properties(String id, String title, String icon) {
        put("id", id);
        put("title", title);
        put("icon", icon);
    }
}