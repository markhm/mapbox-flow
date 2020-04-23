package mapboxflow.layer;

import org.json.JSONObject;

public class Properties extends JSONObject
{
    public Properties()
    {}

    public Properties(String title, String icon)
    {
        put("title", title);
        put("icon", icon);
    }
}