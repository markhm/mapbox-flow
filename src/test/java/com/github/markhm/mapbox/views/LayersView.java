package com.github.markhm.mapbox.views;

import com.github.markhm.mapbox.*;
import com.github.markhm.mapbox.component.InfoBox;
import com.github.markhm.mapbox.component.LayerSelectBox;
import com.github.markhm.mapbox.service.LineBuilder;
import com.github.markhm.mapbox.style.DirectionsResponseLoader;
import com.github.markhm.mapbox.util.AccessToken;
import com.github.markhm.mapbox.util.Color;
import com.github.markhm.mapbox.util.ViewUtil;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.Route;
import mapboxflow.jsonobject.layer.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Route("layers")
public class LayersView extends VerticalLayout {
    // http://www.jsonschema2pojo.org/

    private static Log log = LogFactory.getLog(LayersView.class);

    private static final String LEADING_WIDTH = "125px";

    private MapboxMap mapboxMap = null;

    private LayerSelectBox layerSelectBox = null;

    boolean alreadyRendered = false;

    public LayersView() {
        setAlignItems(Alignment.CENTER);

        if (!alreadyRendered) {
            render();
            alreadyRendered = true;
        }
    }

    private void render() {
        VerticalLayout contentBox = new VerticalLayout();
        contentBox.setWidth("1200px");
        add(contentBox);

        Anchor sourceLink = new Anchor("https://github.com/markhm/mapbox-flow", " (source code on GitHub)");
        sourceLink.setTarget("_blank");

        HorizontalLayout titleBox = new HorizontalLayout();
        titleBox.setAlignItems(Alignment.BASELINE);
        H3 title = new H3("Mapbox-Flow Demo");
        titleBox.add(title, sourceLink);
        contentBox.add(titleBox);

        contentBox.add(renderZoomButtons());

        HorizontalLayout mapboxLine = new HorizontalLayout();

        String accessToken = AccessToken.getToken();

        MapboxProperties properties = new MapboxProperties(accessToken);
        properties.setInitialLocation(GeoLocation.InitialView_Turku_NY);
        properties.setInitialZoom(2);
        mapboxMap = new MapboxMap(properties);
        mapboxMap.setWidth("1000px");
        mapboxMap.setHeight("500px");

        Set<String> initialLayers = new HashSet<>();
        layerSelectBox = new LayerSelectBox(mapboxMap, initialLayers);
        mapboxLine.add(mapboxMap, layerSelectBox);

        contentBox.add(mapboxLine);

        contentBox.add(renderLayerButtons());
        contentBox.add(renderMoveButtons());
        // contentBox.add(renderAnimationButtons());
        contentBox.add(renderControlButtons());
    }

    private TextArea renderTextArea() {
        TextArea textArea = new TextArea("Last JavaScript command");
        textArea.setHeight("120px");
        textArea.setWidth("1200px");
        textArea.getStyle().set("align-self", "center");

        return textArea;
    }

    private HorizontalLayout renderZoomButtons() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setAlignItems(Alignment.CENTER);

        Button zoomTurku = new Button("Turku", e -> mapboxMap.flyTo(GeoLocation.Turku, 10));
        Button zoomCopenhagen = new Button("Copenhagen", e -> mapboxMap.flyTo(GeoLocation.Copenhagen, 16));
        Button zoomAmsterdam = new Button("Amsterdam", e -> mapboxMap.flyTo(GeoLocation.Amsterdam));
        Button zoomParis = new Button("Paris", e -> mapboxMap.flyTo(GeoLocation.Paris, 8));
        Button zoomNewYork = new Button("New York JFK", e -> mapboxMap.flyTo(GeoLocation.NewYork_JFK));

        Button zoomWorld = new Button("World", e -> mapboxMap.flyTo(GeoLocation.Center, 1));
        Button zoomToLevel16 = new Button("Zoom in", e -> mapboxMap.zoomTo(16));

        Label leadingLabel = new Label("Zoom to: ");
        leadingLabel.setWidth(LEADING_WIDTH);

        horizontalLayout.add(leadingLabel, zoomTurku, zoomCopenhagen, zoomAmsterdam, zoomParis, zoomNewYork, zoomWorld, zoomToLevel16);

        return horizontalLayout;
    }

    private HorizontalLayout renderMoveButtons() {
        HorizontalLayout moveButtons = new HorizontalLayout();
        moveButtons.setAlignItems(Alignment.CENTER);

        Button addCarInUtrecht = new Button("Add car Utrecht", e -> addCar("car_Utrecht", "Utrecht", GeoLocation.Utrecht));
        Button moveCar = new Button("Move car", e ->
        {
            MapItem car = mapboxMap.getMapItem("car_Utrecht");
            mapboxMap.moveItemTo(car, GeoLocation.Roskilde);
        });
        moveButtons.add(addCarInUtrecht, moveCar);

        Label leadingLabel = new Label("Layer examples: ");
        leadingLabel.setWidth(LEADING_WIDTH);

        moveButtons.add(leadingLabel, addCarInUtrecht, moveCar);
        return moveButtons;
    }

    private HorizontalLayout renderLayerButtons() {
        HorizontalLayout layerButtons = new HorizontalLayout();
        layerButtons.setAlignItems(Alignment.CENTER);

        Button addLayer = new Button("Add point(s)", e -> {
            Layer layer = getExampleLayer();
            mapboxMap.addLayer(layer);
            layerSelectBox.registerLayer(layer.getId());
        });

        Button addLine = new Button("Add route", e -> {
            String layerId = "route_line";
            com.github.markhm.mapbox.style.Geometry geometry = DirectionsResponseLoader.createDirectionsResponseFrom(DirectionsResponseLoader.MAD_TO_CPH).getRoutes().get(0).getGeometry();
            Layer routeLineLayer = LineBuilder.buildLineLayer(layerId, geometry, Color.GREEN);
            mapboxMap.addLayer(routeLineLayer);
            layerSelectBox.registerLayer(layerId);
        });

        Button addPolygon = new Button("Add polygon", e -> {
            String layerId = "polygon";
            Layer polygonLayer = getPolygonLayer(layerId);
            log.info("polygonLayer = " + polygonLayer);
            mapboxMap.addLayer(polygonLayer);
            layerSelectBox.registerLayer(layerId);
        });

        Button revisePolygon = new Button("Move polygon", e -> {
            Geometry geometry = new Geometry(Geometry.Type.Polygon);

            List<List<Double>> coordinates = new ArrayList<>();
            coordinates.add(GeoLocation.Amsterdam.getCoordList());
            coordinates.add(GeoLocation.Copenhagen.getCoordList());
            coordinates.add(GeoLocation.Berlin.getCoordList());
            geometry.setCoordinates(coordinates);

            Data data = new Data(Data.Type.single);
            Feature feature = new Feature(Feature.FEATURE, new Properties(), geometry);
            data.addFeature(feature);

            mapboxMap.resetSourceData("polygon", data);
        });

        Button addCarInNewYork = new Button("Add car New York", e -> addCar("car_NY", "New York", GeoLocation.NewYork));
        Button addCarInMadrid = new Button("Add car Madrid", e -> addCar("car_Madrid,", "Madrid", GeoLocation.Madrid));

        Button addTenCars = new Button("Add 10 cars", e -> addCars());

        Label leadingLabel = new Label("Layer examples: ");
        leadingLabel.setWidth(LEADING_WIDTH);
        layerButtons.add(leadingLabel, addLayer, addLine, addPolygon, revisePolygon,
                ViewUtil.horizontalWhiteSpace(30), addCarInNewYork, addCarInMadrid, addTenCars);

        return layerButtons;
    }

    private Layer getPolygonLayer(String layerId) {
        Layer layer = new Layer(layerId, Layer.Type.fill);
        Layout fillLayout = new Layout(Layer.Type.fill);
        layer.setLayout(fillLayout);
        Paint paint = new Paint(Paint.Type.fill);
        paint.setFillColor(Color.RED_LINE);
        paint.setFillOpacity(0.5);
        layer.setPaint(paint);

        Geometry geometry = new Geometry(Geometry.Type.Polygon);

        List<List<Double>> coordinates = new ArrayList<>();
        coordinates.add(GeoLocation.Bermuda_1_Florida.getCoordList());
        coordinates.add(GeoLocation.Bermuda_2_Bermuda.getCoordList());
        coordinates.add(GeoLocation.Bermuda_3_PuertoRico.getCoordList());
        geometry.setCoordinates(coordinates);

        Data data = new Data(Data.Type.single);
        Feature feature = new Feature(Feature.FEATURE, new Properties(), geometry);
        data.addFeature(feature);

        Source source = new Source();
        source.setData(data);
        layer.setSource(source);

        return layer;
    }

    private void addNivaHome() {
        addCar("arena", "Royal Arena", new GeoLocation(55.626066, 12.573845));
    }

    private void addCars() {
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

    private void addCar(String id, String description, GeoLocation location) {
        MapItem car = new MapItem() {
            @Override
            public String getLayerId() {
                return "cars";
            }

            @Override
            public MapboxSprite getSprite() {
                return MapboxSprite.Car;
            }

            @Override
            public GeoLocation getLocation() {
                return location;
            }

            @Override
            public String getDescription() {
                return description;
            }

            @Override
            public String getId() {
                return id;
            }
        };

        mapboxMap.addMapItem(car);
        layerSelectBox.registerLayer(car.getLayerId());
    }

    private HorizontalLayout renderControlButtons() {
        HorizontalLayout controlButtons = new HorizontalLayout();
        controlButtons.setAlignItems(Alignment.CENTER);
        Button addCoordinatesButton = new Button("Coordinate listener", e -> mapboxMap.activatePointerLocation());
        Button removeCoordinatesButton = new Button("Remove", e -> mapboxMap.deactivateListeners());

        Label leadingLabel = new Label("Controls: ");
        leadingLabel.setWidth(LEADING_WIDTH);

        InfoBox infoBox = new InfoBox();

        controlButtons.add(leadingLabel, addCoordinatesButton, removeCoordinatesButton, ViewUtil.horizontalWhiteSpace(30), infoBox);
        return controlButtons;
    }

    private HorizontalLayout renderAnimationButtons() {
        HorizontalLayout animationsButtons = new HorizontalLayout();
        animationsButtons.setAlignItems(Alignment.CENTER);

        Button turkuNewYork = new Button("Airplane Turku to New York", e ->
        {
            CommonViewElements.showNotification("This example does not work yet");

            // mapboxMap.drawOriginDestinationFlight(GeoLocation.Turku, GeoLocation.NewYork_JFK);

            // layerSelectBox.registerLayer("airplane");
            // layerSelectBox.registerLayer("airplane_route");

        });
        turkuNewYork.setId("replay");

//        Button startAnimation = new Button("Circle animation", e ->
//        {
//            mapboxMap.startAnimation();
//            layerSelectBox.addLayer("circle_animation");
//        });

        Label leadingLabel = new Label("Animations: ");
        leadingLabel.setWidth(LEADING_WIDTH);

        // animationsButtons.add(leadingLabel, turkuNewYork, startAnimation);
        animationsButtons.add(leadingLabel, turkuNewYork);

        return animationsButtons;
    }

    public static Layer getExampleLayer() {
        // getLayer().toString() or getLayer().toString().replace("\"", "\'") is not needed
        Layer layer = new Layer("symbols", Layer.Type.symbol);

        Properties mapboxDCProperties = new Properties("bank", "National Bank", MapboxSprite.Bank.toString());
        GeoLocation mapboxDCLocation = new GeoLocation(38.913188059745586, -77.03238901390978);
        Feature mapboxDCFeature = new Feature("Feature", mapboxDCProperties, mapboxDCLocation);
        layer.addFeature(mapboxDCFeature);

        Properties mapboxDangerProperties = new Properties("danger", "Danger", MapboxSprite.Fire_Station.toString());
        GeoLocation mapboxDangerLocation = new GeoLocation(-20, 30);
        Feature mapboxDangerFeature = new Feature("Feature", mapboxDangerProperties, mapboxDangerLocation);
        layer.addFeature(mapboxDangerFeature);

        Properties mapboxSFProperties = new Properties("helicopter", "Helicopter Haven", MapboxSprite.Helicopter.toString());
        GeoLocation mapboxSFLocation = new GeoLocation(37.776, -122.414);
        Feature mapboxSFFeature = new Feature("Feature", mapboxSFProperties, mapboxSFLocation);
        layer.addFeature(mapboxSFFeature);

        log.info("adding Layer: " + layer.toString());

        return layer;
    }
}
