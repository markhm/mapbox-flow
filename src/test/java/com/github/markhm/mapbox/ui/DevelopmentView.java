package com.github.markhm.mapbox.ui;

import com.github.markhm.mapbox.*;
import com.github.markhm.mapbox.Geometry;
import com.github.markhm.mapbox.directions.DirectionsResponse;
import com.github.markhm.mapbox.util.Color;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinServletService;
import elemental.json.Json;
import elemental.json.JsonObject;
import mapboxflow.elemental.json.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
// import org.vaadin.addon.sliders.PaperSlider;

import java.util.ArrayList;
import java.util.List;

@Route("dev")
public class DevelopmentView extends VerticalLayout
{
    private static Log log = LogFactory.getLog(DevelopmentView.class);

    private MapboxMap mapboxMap = null;

    boolean alreadyRendered = false;

    private VerticalLayout content = new VerticalLayout();

//    private PaperSlider slider = null;
//    private Label sliderValue = null;

    public DevelopmentView()
    {
        setAlignItems(Alignment.CENTER);

        content.setAlignItems(Alignment.START);
        content.setWidth("1200px");

        if (!alreadyRendered)
        {
            render(content);
            alreadyRendered = true;
        }
    }

    private void render(VerticalLayout content)
    {
        add(content);

        renderHeader(content);
        renderMap(content);
        // renderSlider(content); //deactivated to remove dependency on paper-slider-1.0.1

        Button fullScreenControl = new Button("Add fullscreen control", e -> mapboxMap.addFullScreenControl());
        content.add(fullScreenControl);

        renderNewLayer(content);

        renderDynamicLayerButton(content);

        renderImage(content);
    }

    private void renderHeader(VerticalLayout content)
    {
        HorizontalLayout titleBox = new HorizontalLayout();
        titleBox.setAlignItems(Alignment.BASELINE);
        H3 title = new H3("Mapbox-Flow Development");
        content.add(title);
    }

    private void renderMap(VerticalLayout content)
    {
        String accessToken = AccessToken.getToken();
        mapboxMap = new ItemMapboxMap(accessToken, GeoLocation.InitialView_Turku_NY, 2);
        mapboxMap.setWidth("1200px");
        mapboxMap.setHeight("500px");

        content.add(mapboxMap);
    }

//    private void renderSlider(VerticalLayout content)
//    {
//        sliderValue = new Label("Slider value  ");
//        slider = new PaperSlider(0, 100, 50);
//        slider.addValueChangeListener(e -> sliderValue.setText("Slide value: "+e.getValue()));
//
//        HorizontalLayout sliderLine = new HorizontalLayout();
//        sliderLine.setWidthFull();
//        sliderLine.setAlignItems(Alignment.START);
//        sliderLine.add(sliderValue, ViewUtil.horizontalWhiteSpace(25), slider);
//        content.add(sliderLine);
//    }

    public void renderNewLayer(VerticalLayout content)
    {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setAlignItems(Alignment.CENTER);

        buttonLayout.add("elemental.json-based layers: ");
        Button addSymbols = new Button("Add symbols", e -> addSymbolLayer());
        buttonLayout.add(addSymbols);

        Button addLine = new Button("Add line", e -> addLineLayer());
        buttonLayout.add(addLine);

        Button addPolygon = new Button("Add polygon", e -> addPolygonLayer());
        buttonLayout.add(addPolygon);

        content.add(buttonLayout);
    }

    public void addPolygonLayer()
    {
        JsonObject polygonLayer = LayerHelper.createLayer("polygon", LayerHelper.Type.fill);
        JsonObject paint = PaintHelper.createPaint(PaintHelper.Type.fill);
        paint.put("fill-color", Color.RED_LINE.toString());
        paint.put("fill-opacity", 0.5);
        polygonLayer.put("paint", paint);

        JsonObject geometry = GeometryHelper.createGeometry(GeometryHelper.Type.Polygon);

        List<List<Double>> coordinates = new ArrayList<>();
        coordinates.add(GeoLocation.Bermuda_1_Florida.getCoordList());
        coordinates.add(GeoLocation.Bermuda_2_Bermuda.getCoordList());
        coordinates.add(GeoLocation.Bermuda_3_PuertoRico.getCoordList());

        GeometryHelper.setCoordinates(geometry, coordinates);

        JsonObject data = DataHelper.createData(DataHelper.Type.single);
        FeatureHelper.createFeature("Feature", PropertiesHelper.createProperties(), geometry);
        data.put("geometry", geometry);

        JsonObject source = SourceHelper.createSource();
        source.put("data", data);

        polygonLayer.put("source", source);

        log.info("polygonLayer: "+polygonLayer.toJson());

        mapboxMap.addLayer(polygonLayer);
    }

    public void addLineLayer()
    {
        JsonObject lineLayer = LayerHelper.createLayer("route_line", LayerHelper.Type.line);

        JsonObject paint = PaintHelper.createPaint(PaintHelper.Type.line, Color.RED_LINE, 3);
        lineLayer.put("paint", paint);

        com.github.markhm.mapbox.Geometry geometry = DirectionsResponse.getInstance().getRoutes().get(0).getGeometry();

        JsonObject geometryObject = Json.createObject();
        geometryObject.put("type", "LineString");
        geometryObject.put("coordinates", GeometryHelper.toJsonArray(geometry.getCoordinates()));

        JsonObject source = SourceHelper.createSource();

        JsonObject data = DataHelper.createData(DataHelper.Type.single);
        source.put("data", data);

        JsonObject feature = FeatureHelper.createFeature("LineString", PropertiesHelper.createProperties(), geometryObject);
        DataHelper.addFeature(data, feature);

        lineLayer.put("source", source);

        mapboxMap.addLayer(lineLayer);
    }

    public void addSymbolLayer()
    {
        JsonObject layer = LayerHelper.createLayer("test", LayerHelper.Type.symbol);

        JsonObject mapboxDCProperties = PropertiesHelper.createProperties("bank", "National Bank", Sprite.Bank.toString());
        mapboxDCProperties.put("color", Color.NAVY_BLUE.toString());
        GeoLocation mapboxDCLocation = new GeoLocation(38.913188059745586, -77.03238901390978);
        JsonObject mapboxDCFeature = FeatureHelper.createFeature("Feature", mapboxDCProperties, mapboxDCLocation);
        LayerHelper.addFeature(layer, mapboxDCFeature);

        JsonObject mapboxDangerProperties = PropertiesHelper.createProperties("danger", "Danger", Sprite.Fire_Station.toString());
        mapboxDangerProperties.put("color", Color.RED.toString());
        GeoLocation mapboxDangerLocation = new GeoLocation(-20, 30);
        JsonObject mapboxDangerFeature = FeatureHelper.createFeature("Feature", mapboxDangerProperties, mapboxDangerLocation);
        LayerHelper.addFeature(layer, mapboxDangerFeature);

        JsonObject mapboxSFProperties = PropertiesHelper.createProperties("helicopter", "Helicopter Haven", Sprite.Helicopter.toString());
        mapboxSFProperties.put("color", Color.RED.toString());
        GeoLocation mapboxSFLocation = new GeoLocation(37.776, -122.414);
        JsonObject mapboxSFFeature = FeatureHelper.createFeature("Feature", mapboxSFProperties, mapboxSFLocation);
        LayerHelper.addFeature(layer, mapboxSFFeature);

        // https://stackoverflow.com/questions/33838802/mapbox-gl-change-icon-color
        // https://github.com/mapbox/maki/blob/b0060646e28507037e71edf049a17fab470a0080/sdf-render.js
        // https://github.com/mapbox/spritezero

//        JsonObject paint = PaintHelper.createPaint(PaintHelper.Type.symbol);
//        JsonArray array = Json.createArray();
//        array.set(0, "get");
//        array.set(1, "color");
//        paint.put("icon-color", array);
//
//        // paint.put("icon-color", Color.RED_LINE.toString());
//        layer.put("paint", paint);

        log.info("layer = "+layer);

        mapboxMap.addLayer(layer);
    }

    private void renderDynamicLayerButton(VerticalLayout content)
    {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setAlignItems(Alignment.CENTER);

        Button dynamicData = new Button("Add dynamic data layer from /data", e -> renderDynamicLayer());
        buttonLayout.add(dynamicData);

        Button updateData = new Button("Update data", e -> updateData());
        buttonLayout.add(updateData);

        content.add(buttonLayout);
    }

    private void updateData()
    {
        mapboxMap.executeJs("this.map.getSource('polygon').setData('/data')");
    }

    private void renderDynamicLayer()
    {
        JsonObject polygonLayer = LayerHelper.createLayer("polygon", LayerHelper.Type.fill);
        JsonObject paint = PaintHelper.createPaint(PaintHelper.Type.fill);
        paint.put("fill-color", Color.RED_LINE.toString());
        paint.put("fill-opacity", 0.5);
        polygonLayer.put("paint", paint);

        JsonObject source = SourceHelper.createSource();
        source.put("data", "/data");

        polygonLayer.put("source", source);

        mapboxMap.addLayer(polygonLayer);
    }

    private void renderImage(VerticalLayout content)
    {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setAlignItems(Alignment.CENTER);

        Button imageButton = new Button("Add image", e -> addImage());
        buttonLayout.add(imageButton);

        Button placeImageButton = new Button("Place image", e -> placeImage());
        buttonLayout.add(placeImageButton);

        content.add(buttonLayout);
    }

    private void placeImage()
    {

    }

    private void addImage()
    {
        String imageString = "/img/atom.png";
        String resolvedImage = VaadinServletService.getCurrent().resolveResource(imageString);
        log.info("resolvedImage = "+resolvedImage);
        Image image = new Image(resolvedImage, "Atom");
        content.add(image);

        String extendedImageString = "'http://localhost:8080/img/atom.png'";

        mapboxMap.addImage(extendedImageString, "'Atom'");

        // mapboxMap.getElement().callJsFunction("laadtDiePlaat");
        // mapboxMap.executeJs("loadImage('\" + extendedImageString + \"', 'atom')");
    }

}
