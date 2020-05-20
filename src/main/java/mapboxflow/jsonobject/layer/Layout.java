package mapboxflow.jsonobject.layer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;

public class Layout extends JSONObject implements Serializable
{
    public Layout(Layer.Type type)
    {
        if (type.equals(Layer.Type.line))
        {
            initiateLine();
        }
        else if (type.equals(Layer.Type.symbol))
        {
            initiateSymbol();
        }
        else if (type.equals(Layer.Type.fill))
        {
            initiateFill();
        }
        else
        {
            throw new RuntimeException("Please Layout for layer type "+type);
        }
    }

    private void initiateFill()
    {
        // nothing prescribed
    }


    private void initiateLine()
    {
        put("line-join", "round");
        put("line-cap", "round");
    }

    private void initiateSymbol()
    {
        JSONArray iconImageArray = new JSONArray();
        iconImageArray.put(0, "concat");
        JSONArray innerIconImageArray = new JSONArray();
        innerIconImageArray.put(0, "get");
        innerIconImageArray.put(1, "icon");
        iconImageArray.put(1, innerIconImageArray);
        iconImageArray.put(2, "-15");
        put("icon-image", iconImageArray);

        JSONArray textFieldArray = new JSONArray();
        textFieldArray.put(0, "get");
        textFieldArray.put(1, "title");
        put("text-field", textFieldArray);

        JSONArray textFontArray = new JSONArray();
        textFontArray.put(0, "Open Sans Semibold");
//            textFontArray.put(1, "Open Sans Semibold");
        // textFontArray.put(1, "Arial Unicode MS Bold");
        put("text-font", textFontArray);
        // put("text-font", "Open Sans Semibold");

        JSONArray textOffsetArray = new JSONArray();
        textOffsetArray.put(0, 0);
        textOffsetArray.put(1, 0.7);
        put("text-offset", textOffsetArray);

        put("text-anchor", "top");
    }

}