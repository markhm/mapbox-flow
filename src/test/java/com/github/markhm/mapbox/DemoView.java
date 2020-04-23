package com.github.markhm.mapbox;

import com.github.markhm.mapbox.component.InfoBox;
import com.github.markhm.mapbox.component.LayerSelectBox;
import com.github.markhm.mapbox.directions.DirectionsResponse;
import com.github.markhm.mapbox.util.Color;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.Route;
import mapboxflow.ConversionUtil;
import mapboxflow.layer.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Route("")
// @StyleSheet("./com/github/markhm/mapbox-flow/mapbox.css")
// @JavaScript("./com/github/markhm/mapbox-flow/mapbox.js")
public class DemoView extends VerticalLayout
{
    // http://www.jsonschema2pojo.org/

    private static Log log = LogFactory.getLog(DemoView.class);

    private static final String LEADING_WIDTH = "125px";

    private MapboxMap mapboxMap = null;
    private LayerSelectBox layerSelectBox = null;

    private TextArea textArea = null;

    boolean alreadyRendered = false;

    public DemoView()
    {
        setAlignItems(Alignment.CENTER);

        if (!alreadyRendered)
        {
            render();
            alreadyRendered = true;
        }
    }

    private void render()
    {
        VerticalLayout contentBox = new VerticalLayout();
        contentBox.setWidth("1200px");
        add(contentBox);

        HorizontalLayout titleBox = new HorizontalLayout();
        H3 title = new H3("Mapbox-Flow Demo");
        titleBox.add(title);
        contentBox.add(titleBox);

        contentBox.add(renderZoomButtons());

        HorizontalLayout mapboxLine = new HorizontalLayout();
        mapboxMap = new MapboxMap(GeoLocation.InitialView_Turku_NY, 2);
        Set<String> initialLayers = new HashSet<>();
        layerSelectBox = new LayerSelectBox(mapboxMap, initialLayers);
        mapboxLine.add(mapboxMap, layerSelectBox);

        contentBox.add(mapboxLine);

        contentBox.add(renderLayerButtons());
        contentBox.add(renderAnimationButtons());
        contentBox.add(renderControlButtons());

        textArea = renderTextArea();
        contentBox.add(textArea);
        contentBox.add(ViewUtil.verticalWhitespace(5));
        contentBox.add(new InfoBox());
    }

    private TextArea renderTextArea()
    {
        TextArea textArea = new TextArea("Last JavaScript command");
        textArea.setHeight("120px");
        textArea.setWidth("1200px");
        textArea.getStyle().set("align-self", "center");

        return textArea;
    }

    private HorizontalLayout renderZoomButtons()
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

        return horizontalLayout;
    }

    private HorizontalLayout renderLayerButtons()
    {
        HorizontalLayout layerButtons = new HorizontalLayout();
        layerButtons.setAlignItems(Alignment.CENTER);

        Button addLayer = new Button("Add point(s)", e ->
        {
            Layer layer = getExampleLayer();
            String command = "addLayer(" + layer + ")";
            textArea.setValue(command);
            mapboxMap.addLayer(getExampleLayer());
            layerSelectBox.addLayer(layer.getId());
        });

        Button addLine = new Button("Add line", e ->
        {
            String layerId = "route_line";
            Layer routeLineLayer = getLineLayer(layerId);
            mapboxMap.addLayer(routeLineLayer);
            layerSelectBox.addLayer(layerId);

        });

        Button addPolygon = new Button("Add polygon", e ->
        {
            String layerId = "polygon";
            Layer polygonLayer = getPolygonLayer(layerId);
            mapboxMap.addLayer(polygonLayer);
            layerSelectBox.addLayer(layerId);
        });

        Button revisePolygon = new Button ("Revise polygon", e ->
        {
            mapboxflow.layer.Geometry geometry = new mapboxflow.layer.Geometry(mapboxflow.layer.Geometry.Type.Polygon);

            List<List<Double>> coordinates = new ArrayList<>();
            coordinates.add(GeoLocation.Amsterdam.getCoordList());
            coordinates.add(GeoLocation.Copenhagen.getCoordList());
            coordinates.add(GeoLocation.Berlin.getCoordList());
            geometry.setCoordinates(coordinates);

            mapboxflow.layer.Data data = new mapboxflow.layer.Data(mapboxflow.layer.Data.Type.single);
            Feature feature = new Feature(Feature.FEATURE, new Properties(), geometry);
            data.addFeature(feature);

            mapboxMap.resetSourceData("polygon", data);
        });

        Button addCarInRoskilde = new Button("Add car New York", e -> addCar("car_NY", "New York", GeoLocation.NewYork) );
        Button addCarInMadrid = new Button("Add car Madrid", e -> addCar("car_Madrid,", "Madrid", GeoLocation.Madrid) );

        Button addTenCars = new Button("Add 10 cars", e -> addCars());

        Label leadingLabel = new Label("Layer examples: ");
        leadingLabel.setWidth(LEADING_WIDTH);

        layerButtons.add(leadingLabel, addLayer, addLine, addPolygon, revisePolygon,
                ViewUtil.horizontalWhiteSpace(30), addCarInRoskilde, addCarInMadrid, addTenCars);

        return layerButtons;
    }

    private Layer getPolygonLayer(String layerId)
    {
        Layer layer = new Layer(layerId, Layer.Type.fill);
        Layout fillLayout = new Layout(Layer.Type.fill);
        layer.setLayout(fillLayout);
        Paint paint = new Paint(Paint.Type.fill);
        paint.setFillColor(Color.RED_LINE);
        paint.setFillOpacity(0.5);
        layer.setPaint(paint);

        mapboxflow.layer.Geometry geometry = new mapboxflow.layer.Geometry(mapboxflow.layer.Geometry.Type.Polygon);

        List<List<Double>> coordinates = new ArrayList<>();
        coordinates.add(GeoLocation.Bermuda_1_Florida.getCoordList());
        coordinates.add(GeoLocation.Bermuda_2_Bermuda.getCoordList());
        coordinates.add(GeoLocation.Bermuda_3_PuertoRico.getCoordList());
        geometry.setCoordinates(coordinates);

        mapboxflow.layer.Data data = new mapboxflow.layer.Data(mapboxflow.layer.Data.Type.single);
        Feature feature = new Feature(Feature.FEATURE, new Properties(), geometry);
        data.addFeature(feature);

        Source source = new Source();
        source.setData(data);
        layer.setSource(source);

        return layer;
    }

    private Layer getLineLayer(String layerId)
    {
        Layer layer = new Layer(layerId, Layer.Type.line);
        Layout lineLayout = new Layout(Layer.Type.line);
        layer.setLayout(lineLayout);
        layer.setPaint(new Paint(Paint.Type.line, Color.RED_LINE, 3));

        Geometry geometry = DirectionsResponse.getInstance().getRoutes().get(0).getGeometry();
        mapboxflow.layer.Geometry convertedGeometry = ConversionUtil.convert(geometry);

        Source source = new Source();
        mapboxflow.layer.Data data = new mapboxflow.layer.Data(mapboxflow.layer.Data.Type.single);
        Feature feature = new Feature(Feature.FEATURE, null, convertedGeometry);
        data.addFeature(feature);

        source.setData(data);
        layer.setSource(source);

        return layer;
    }

    private void addCars()
    {
        addCar("car_1", "Aarhus", GeoLocation.Aarhus);
        addCar("car-2", "Odense", GeoLocation.Odense);
        addCar("car-3", "Aalborg", GeoLocation.Aalborg);
        addCar("car-4", "Bornholm", GeoLocation.Bornholm);
        addCar("car_5", "Roskilde", GeoLocation.Roskilde);
        addCar("car-6", "Kijkduin", GeoLocation.Kijkduin);
        addCar("car-7", "Amsterdam", GeoLocation.Amsterdam);
        addCar("car-8", "Utrecht", GeoLocation.Utrecht);
        addCar("car-9", "Boston", GeoLocation.Boston);
        addCar("car-10", "Moscow", GeoLocation.Moscow);
    }

    private void addCar(String id, String description, GeoLocation location)
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

            @Override
            public String getId()
            {
                return id;
            }
        };

        String command = mapboxMap.addAnimatedItem(car);
        layerSelectBox.addLayer(car.getLayerId());

        textArea.setValue(command);
    }

    private HorizontalLayout renderControlButtons()
    {
        HorizontalLayout controlButtons = new HorizontalLayout();
        controlButtons.setAlignItems(Alignment.CENTER);
        Button activatePointerCoordinates = new Button("Coordinate listener", e -> mapboxMap.executeJs("activatePointerLocation();"));
        Button deactivatePointerCoordinates = new Button("Deactivate all listeners", e -> mapboxMap.executeJs("deactivateAllListeners();"));

        Label leadingLabel = new Label("Controls: ");
        leadingLabel.setWidth(LEADING_WIDTH);

        controlButtons.add(leadingLabel, activatePointerCoordinates, deactivatePointerCoordinates);
        return controlButtons;
    }

    private HorizontalLayout renderAnimationButtons()
    {
        HorizontalLayout animationsButtons = new HorizontalLayout();
        animationsButtons.setAlignItems(Alignment.CENTER);

        Button turkuNewYork = new Button("Airplane Turku to New York", e ->
        {
            mapboxMap.drawOriginDestinationFlight(GeoLocation.Turku, GeoLocation.NewYork_JFK);

            layerSelectBox.addLayer("airplane");
            layerSelectBox.addLayer("airplane_route");

        });
        turkuNewYork.setId("replay");

        Button startAnimation = new Button("Circle animation", e ->
        {
            mapboxMap.startAnimation();
            layerSelectBox.addLayer("circle_animation");
        });

        Label leadingLabel = new Label("Animations: ");
        leadingLabel.setWidth(LEADING_WIDTH);

        animationsButtons.add(leadingLabel, turkuNewYork, startAnimation);

        return animationsButtons;
    }

    public static Layer getExampleLayer()
    {
        // getLayer().toString() or getLayer().toString().replace("\"", "\'") is not needed
        Layer layer = new Layer("symbols", Layer.Type.symbol);

        Properties mapboxDCProperties = new Properties("National Bank", Sprite.Bank.toString());
        GeoLocation mapboxDCLocation = new GeoLocation(38.913188059745586, -77.03238901390978);
        Feature mapboxDCFeature = new Feature("Feature", mapboxDCProperties, mapboxDCLocation);
        layer.addFeature(mapboxDCFeature);

        Properties mapboxDangerProperties = new Properties("National danger", Sprite.Fire_Station.toString());
        GeoLocation mapboxDangerLocation = new GeoLocation(-20, 30);
        Feature mapboxDangerFeature = new Feature("Feature", mapboxDangerProperties, mapboxDangerLocation);
        layer.addFeature(mapboxDangerFeature);

        Properties mapboxSFProperties = new Properties("Helicopter Haven", Sprite.Helicopter.toString());
        GeoLocation mapboxSFLocation = new GeoLocation(37.776, -122.414);
        Feature mapboxSFFeature = new Feature("Feature", mapboxSFProperties, mapboxSFLocation);
        layer.addFeature(mapboxSFFeature);

        // log.info("adding feature: "+mapboxSFFeature.toString(2));

        // System.out.println(layer.toString(2));

        return layer;
    }
}
