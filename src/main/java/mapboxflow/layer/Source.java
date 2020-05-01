package mapboxflow.layer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Source extends JSONObject implements Serializable
{
    public Source()
    {
        put("type", "geojson");
    }

    public void setType(String type)
    {
        put("type", type);
    }

    public void setId(String id)
    {
        put("id", id);
    }

    public String getId()
    {
        return getString("id");
    }


    public void setData(Data data)
    {
        put("data", data);
    }

    public Data getData()
    {
        Data data = null;

        if (! isNull("data"))
        {
            data = (Data) get("data");
        }

        return data;
    }
}