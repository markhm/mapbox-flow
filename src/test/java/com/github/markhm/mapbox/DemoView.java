package com.github.markhm.mapbox;

import com.github.markhm.mapbox.directions.DeprecatedDirectionsResponse;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;

@Route("")
@JsModule("mapbox.js")
@CssImport("mapbox.css")
public class DemoView extends VerticalLayout
{
    private static Log log = LogFactory.getLog(DemoView.class);

    private MapboxMap mapboxMap = null;

    boolean alreadyRendered = false;

    public DemoView()
    {
        if (!alreadyRendered)
        {
            render();
        }
    }

    private void render()
    {
        H3 title = new H3("Mapbox Demo");
        add(title);

        addTopButtons();

        add(new InfoBox());
        mapboxMap = new MapboxMap(GeoLocation.InitialView_Turku_NY, 2);
        add(mapboxMap);

        addBottomButtons();
    }

    private void addTopButtons()
    {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setAlignItems(Alignment.CENTER);

        Button zoomTurku = new Button("Turku", e -> mapboxMap.flyTo(GeoLocation.Turku));
        Button zoomCopenhagen = new Button("Copenhagen", e -> mapboxMap.flyTo(GeoLocation.Copenhagen));
        Button zoomAmsterdam = new Button("Amsterdam", e -> mapboxMap.flyTo(GeoLocation.Amsterdam));
        Button zoomParis = new Button("Paris", e -> mapboxMap.flyTo(GeoLocation.Paris));
        Button zoomNewYork = new Button("New York JFK", e -> mapboxMap.flyTo(GeoLocation.NewYork_JFK));

        Button zoomWorld = new Button("World", e ->
        {
            mapboxMap.flyTo(GeoLocation.Center);
            mapboxMap.zoomTo(1);
        });

        horizontalLayout.add(new Label("Zoom to:"), zoomTurku, zoomCopenhagen, zoomAmsterdam, zoomParis, zoomNewYork, zoomWorld);

        add(horizontalLayout);
    }

    private void addBottomButtons()
    {
        HorizontalLayout layerButtons = new HorizontalLayout();
        layerButtons.setAlignItems(Alignment.CENTER);

        HorizontalLayout animationsButtons = new HorizontalLayout();
        animationsButtons.setAlignItems(Alignment.CENTER);

        Button startAnimation = new Button("Circle animation", e -> mapboxMap.startAnimation());
        Button addLayer = new Button("Add layer", e -> mapboxMap.executeJS("addLayer(" + getExampleLayer() + ");"));
        // Button removeLayer = new Button("Remove layer", e -> mapboxMap.executeJS("removeLayer('" + getLayer().getId()+ "');"));
        Button hideLayer = new Button("Hide layer", e -> mapboxMap.executeJS("hideLayer('" + getExampleLayer().getId()+ "');"));
        Button unhideLayer = new Button("Unhide layer", e -> mapboxMap.executeJS("unhideLayer('" + getExampleLayer().getId()+ "');"));

        JSONObject jsonObject = TestData.loadFile();

        // log.info("jsonObject = "+ jsonObject.toString(2));

        Button addPolygon = new Button("Add DK Polygon", e -> mapboxMap.executeJS("addPolygon("+jsonObject+");"));
        Button turkuNewYork = new Button("From Turku to New York", e ->
        {
            mapboxMap.drawOriginDestinationFlight(GeoLocation.Turku, GeoLocation.NewYork_JFK);
        });
        turkuNewYork.setId("replay");


//        Button addLineButton = new Button("New York -> Boston",
//                e -> mapboxMap.executeJS("addLine(" + DirectionsResponse.getInstance().getGeometry() + ");"));

//        Button fromParisToCopenhagen = new Button("Paris -> Copenhagen",
//                e -> mapboxMap.executeJS("addLine(" + DirectionsResponse.getInstance().getGeometry() + ");"));

        Button fromParisToCopenhagen = new Button("Cirkelhuset -> Doetinchem",
                e -> mapboxMap.executeJS("addLine(" + DeprecatedDirectionsResponse.getInstance().getGeometry() + ");"));


        layerButtons.add(addLayer, hideLayer, unhideLayer, addPolygon);
        animationsButtons.add(new Label("Animations:"), turkuNewYork, startAnimation, fromParisToCopenhagen);

        HorizontalLayout controlButtons = new HorizontalLayout();
        Button activatePointerCoordinates = new Button("Add pointer coordinates", e -> mapboxMap.executeJS("activatePointerLocation();"));
        Button deactivatePointerCoordinates = new Button("Deactivate all listeners", e -> mapboxMap.executeJS("deactivateAllListeners();"));

        controlButtons.add(activatePointerCoordinates, deactivatePointerCoordinates);

        add(layerButtons);
        add(animationsButtons);
        add(controlButtons);
    }

    public static Layer getExampleLayer()
    {
        // getLayer().toString() or getLayer().toString().replace("\"", "\'") is not needed
        Layer layer = new Layer("points", "symbol");

        Layer.Properties mapboxDCProperties = new Layer.Properties("National Bank", Sprite.Bank);
        GeoLocation mapboxDCLocation = new GeoLocation(-77.03238901390978, 38.913188059745586);
        Layer.Feature mapboxDCFeature = new Layer.Feature("Feature", mapboxDCProperties, mapboxDCLocation);
        layer.addFeature(mapboxDCFeature);

        Layer.Properties mapboxDangerProperties = new Layer.Properties("National danger", Sprite.Danger);
        GeoLocation mapboxDangerLocation = new GeoLocation(30, -20);
        Layer.Feature mapboxDangerFeature = new Layer.Feature("Feature", mapboxDangerProperties, mapboxDangerLocation);
        layer.addFeature(mapboxDangerFeature);

        Layer.Properties mapboxSFProperties = new Layer.Properties("Helicopter Haven", Sprite.Helicopter);
        GeoLocation mapboxSFLocation = new GeoLocation(-122.414, 37.776);
        Layer.Feature mapboxSFFeature = new Layer.Feature("Feature", mapboxSFProperties, mapboxSFLocation);
        layer.addFeature(mapboxSFFeature);

        // System.out.println(layer.toString(2));

        return layer;
    }
}
