package com.github.markhm.mapbox;

import com.github.markhm.mapbox.directions.Converter;
import com.github.markhm.mapbox.directions.DeprecatedDirectionsResponse;
import com.github.markhm.mapbox.directions.DirectionsResponse;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;

@Route("")
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
        H3 title = new H3("Mapbox-Flow Demo");
        add(title);

        addTopButtons();

        add(new InfoBox());
        mapboxMap = new MapboxMap(GeoLocation.InitialView_Turku_NY, 2);
        add(mapboxMap);

        addBottomButtons();
    }

    public void referencingGeneratedClasses()
    {
        Converter converter = null;
    }


    private void addTopButtons()
    {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setAlignItems(Alignment.CENTER);

        Button zoomTurku = new Button("Turku", e -> mapboxMap.zoomTo(GeoLocation.Turku, 10));
        Button zoomCopenhagen = new Button("Copenhagen", e -> mapboxMap.zoomTo(GeoLocation.Copenhagen, 16));
        Button zoomAmsterdam = new Button("Amsterdam", e -> mapboxMap.flyTo(GeoLocation.Amsterdam));
        Button zoomParis = new Button("Paris", e -> mapboxMap.zoomTo(GeoLocation.Paris,8));
        Button zoomNewYork = new Button("New York JFK", e -> mapboxMap.flyTo(GeoLocation.NewYork_JFK));

        Button zoomWorld = new Button("World", e ->
        {
            mapboxMap.zoomTo(GeoLocation.Center, 1);
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
        Button addLayer = new Button("Add layer", e -> mapboxMap.executeJs("addLayer(" + getExampleLayer() + ");"));
        // Button removeLayer = new Button("Remove layer", e -> mapboxMap.executeJS("removeLayer('" + getLayer().getId()+ "');"));
        Button hideLayer = new Button("Hide layer", e -> mapboxMap.executeJs("hideLayer('" + getExampleLayer().getId()+ "');"));
        Button unhideLayer = new Button("Unhide layer", e -> mapboxMap.executeJs("unhideLayer('" + getExampleLayer().getId()+ "');"));

        JSONObject jsonObject = TestData.loadFile();

        Button addPolygon = new Button("Add DK Polygon", e -> mapboxMap.executeJs("addPolygon("+jsonObject+");"));
        Button turkuNewYork = new Button("From Turku to New York", e ->
        {
            mapboxMap.drawOriginDestinationFlight(GeoLocation.Turku, GeoLocation.NewYork_JFK);
        });
        turkuNewYork.setId("replay");

//        Button fromParisToCopenhagen = new Button("Utrecht -> Roskilde",
//                e -> mapboxMap.executeJs("addLine(" + DeprecatedDirectionsResponse.getInstance().getGeometry() + ");"));

        Button linesButton = new Button("Utrecht -> Roskilde",
                e -> mapboxMap.addLine(DirectionsResponse.getInstance().getRoutes().get(0).getGeometry(), Color.NAVY_BLUE));

        layerButtons.add(addLayer, hideLayer, unhideLayer, addPolygon);
        animationsButtons.add(new Label("Animations:"), turkuNewYork, startAnimation, linesButton);

        HorizontalLayout controlButtons = new HorizontalLayout();
        Button activatePointerCoordinates = new Button("Add pointer coordinates", e -> mapboxMap.executeJs("activatePointerLocation();"));
        Button deactivatePointerCoordinates = new Button("Deactivate all listeners", e -> mapboxMap.executeJs("deactivateAllListeners();"));

        controlButtons.add(activatePointerCoordinates, deactivatePointerCoordinates);

        add(layerButtons);
        add(animationsButtons);
        add(controlButtons);
    }

    public static Layer getExampleLayer()
    {
        // getLayer().toString() or getLayer().toString().replace("\"", "\'") is not needed
        Layer layer = new Layer("points", "symbol");

        Layer.Properties mapboxDCProperties = new Layer.Properties("National Bank", Sprite.Bank.toString());
        GeoLocation mapboxDCLocation = new GeoLocation(38.913188059745586, -77.03238901390978);
        Layer.Feature mapboxDCFeature = new Layer.Feature("Feature", mapboxDCProperties, mapboxDCLocation);
        layer.addFeature(mapboxDCFeature);

        Layer.Properties mapboxDangerProperties = new Layer.Properties("National danger", Sprite.Danger.toString());
        GeoLocation mapboxDangerLocation = new GeoLocation(-20, 30);
        Layer.Feature mapboxDangerFeature = new Layer.Feature("Feature", mapboxDangerProperties, mapboxDangerLocation);
        layer.addFeature(mapboxDangerFeature);

        Layer.Properties mapboxSFProperties = new Layer.Properties("Helicopter Haven", Sprite.Helicopter.toString());
        GeoLocation mapboxSFLocation = new GeoLocation(37.776, -122.414);
        Layer.Feature mapboxSFFeature = new Layer.Feature("Feature", mapboxSFProperties, mapboxSFLocation);
        layer.addFeature(mapboxSFFeature);

        // System.out.println(layer.toString(2));

        return layer;
    }
}
