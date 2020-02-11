package elemental.json.test;

import com.github.markhm.mapbox.GeoLocation;
import com.github.markhm.mapbox.MapboxMap;
import com.github.markhm.mapbox.MapboxOptions;
import elemental.json.Json;
import elemental.json.JsonObject;
import elemental.json.JsonValue;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.Test;

public class CreateJsonTest
{
    private static Log log = LogFactory.getLog(CreateJsonTest.class);

    public CreateJsonTest()
    {}

    @Test
    public void testCreateElementsal()
    {
        JsonObject jsonObject = Json.createObject();

        jsonObject.put(MapboxOptions.OptionType.container.toString(), "map");
        jsonObject.put(MapboxOptions.OptionType.style.toString(), "mapbox://styles/mapbox/streets-v11");

        jsonObject.put(MapboxOptions.OptionType.center.toString(), GeoLocation.InitialView_Denmark.getLongLat());
        jsonObject.put(MapboxOptions.OptionType.zoom.toString(), 6);

        log.info(jsonObject);
    }

}
