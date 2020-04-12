package com.github.markhm.mapbox;

import com.github.markhm.mapbox.directions.Converter;
import com.github.markhm.mapbox.directions.DirectionsResponse;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@Route("mapbox-flow")
public class DemoView extends VerticalLayout
{
    // http://www.jsonschema2pojo.org/

    private static Log log = LogFactory.getLog(DemoView.class);

    private MapboxMap mapboxMap = null;

    boolean alreadyRendered = false;

    private static final String LEADING_WIDTH = "125px";

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

        addLayerButtons();
        addAnimationButtons();
        addControlButtons();
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

        Button zoomWorld = new Button("World", e -> mapboxMap.zoomTo(GeoLocation.Center, 1));
        Button zoomToLevel16 = new Button("Zoom in", e -> mapboxMap.zoomTo( 16));

        Label leadingLabel = new Label("Zoom to: ");
        leadingLabel.setWidth(LEADING_WIDTH);

        horizontalLayout.add(leadingLabel, zoomTurku, zoomCopenhagen, zoomAmsterdam, zoomParis, zoomNewYork, zoomWorld, zoomToLevel16);

        add(horizontalLayout);
    }

    private void addLayerButtons()
    {
        HorizontalLayout layerButtons = new HorizontalLayout();
        layerButtons.setAlignItems(Alignment.CENTER);

        Button addLayer = new Button("Add", e -> mapboxMap.executeJs("addLayer(" + getExampleLayer() + ");"));
        // Button removeLayer = new Button("Remove layer", e -> mapboxMap.executeJS("removeLayer('" + getLayer().getId()+ "');"));
        Button hideLayer = new Button("Hide", e -> mapboxMap.executeJs("hideLayer('" + getExampleLayer().getId()+ "');"));
        Button unhideLayer = new Button("Unhide", e -> mapboxMap.executeJs("unhideLayer('" + getExampleLayer().getId()+ "');"));

        String layerId = "cars";

        Layer.Feature feature = getFeature();

        Button queryLayer = new Button("Query", e -> mapboxMap.executeJs("addFeature('" + layerId + "', " + feature + ")"));

        Button addRoute = new Button("Utrecht -> Roskilde",
                e -> mapboxMap.addLine(DirectionsResponse.getInstance().getRoutes().get(0).getGeometry(), Color.NAVY_BLUE));

        Button addCarInRoskilde = new Button("Add car Roskilde", e -> addCar("Car 1", GeoLocation.Roskilde) );
        Button addCarInMadrid = new Button("Add car Madrid", e -> addCar("Car 2", GeoLocation.Madrid) );

        Label leadingLabel = new Label("Layer functions: ");
        leadingLabel.setWidth(LEADING_WIDTH);

        layerButtons.add(leadingLabel, addLayer, hideLayer, unhideLayer, queryLayer, addRoute, addCarInRoskilde, addCarInMadrid);

        add(layerButtons);
    }

    private Layer.Feature getFeature()
    {
        GeoLocation initialPosition = GeoLocation.Bornholm;
        // log.info("Adding " + animatedItem.getDescription() + " to initial position " + initialPosition + " on map.");

        Layer.Properties itemProperties = new Layer.Properties("Car 2", Sprite.Car.toString());
        Layer.Feature itemFeature = new Layer.Feature("Feature", itemProperties, initialPosition);

        return itemFeature;
    }

    private void addCar(String description, GeoLocation location)
    {
        AnimatedItem car = new AnimatedItem()
        {
            @Override
            public String getLayerId()
            {
                return "cars";
            }

            @Override
            public Sprite getSprite()
            {
                return Sprite.Car;
            }

            @Override
            public GeoLocation getLocation()
            {
                return location;
            }

            @Override
            public String getDescription()
            {
                return description;
            }
        };

        mapboxMap.addAnimatedItem(car);

    }

    private void addControlButtons()
    {
        HorizontalLayout controlButtons = new HorizontalLayout();
        controlButtons.setAlignItems(Alignment.CENTER);
        Button activatePointerCoordinates = new Button("Add pointer coordinates", e -> mapboxMap.executeJs("activatePointerLocation();"));
        Button deactivatePointerCoordinates = new Button("Deactivate all listeners", e -> mapboxMap.executeJs("deactivateAllListeners();"));

        Label leadingLabel = new Label("Controls: ");
        leadingLabel.setWidth(LEADING_WIDTH);

        controlButtons.add(leadingLabel, activatePointerCoordinates, deactivatePointerCoordinates);
        add(controlButtons);
    }

    private void addAnimationButtons()
    {
        HorizontalLayout animationsButtons = new HorizontalLayout();
        animationsButtons.setAlignItems(Alignment.CENTER);

        Button turkuNewYork = new Button("Fly Turku to New York", e ->
        {
            mapboxMap.drawOriginDestinationFlight(GeoLocation.Turku, GeoLocation.NewYork_JFK);
        });
        turkuNewYork.setId("replay");

        Button startAnimation = new Button("Circle animation", e -> mapboxMap.startAnimation());

        Label leadingLabel = new Label("Animations: ");
        leadingLabel.setWidth(LEADING_WIDTH);

        animationsButtons.add(leadingLabel, turkuNewYork, startAnimation);

        add(animationsButtons);
    }

    public static Layer getExampleLayer()
    {
        // getLayer().toString() or getLayer().toString().replace("\"", "\'") is not needed
        Layer layer = new Layer("points", Layer.Type.symbol);

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

        log.info("adding feature: "+mapboxSFFeature.toString(2));

        // System.out.println(layer.toString(2));

        return layer;
    }
}
