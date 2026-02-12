package com.github.markhm.mapbox.views.dk;

import com.github.markhm.mapbox.*;
import com.github.markhm.mapbox.component.LayerLegend;
import com.github.markhm.mapbox.util.AccessToken;
import com.github.markhm.mapbox.util.ColorPalette;
import com.github.markhm.mapbox.util.ViewUtil;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import mapboxflow.jsonobject.layer.Layer;
import mapboxflow.jsonobject.layer.Paint;
import mapboxflow.jsonobject.layer.Source;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;
import java.util.stream.Collectors;

@Route("dk_map")
public class DKKommunerView extends VerticalLayout {
    private static Log log = LogFactory.getLog(DKKommunerView.class);

    private String[] CURRENT_PALETTE = ColorPalette.RED_PALETTE_7;

    private MapboxMap mapboxMap = null;
    private LayerLegend legend = null;

    private static final double opacity = 0.7;

    private VerticalLayout content = new VerticalLayout();
    boolean alreadyRendered = false;

    private boolean sourceAlreadyAdded = false;

    private boolean layersActive = false;

    public DKKommunerView() {
        setAlignItems(Alignment.CENTER);

        content.setAlignItems(Alignment.START);
        content.setWidth("1200px");

        if (!alreadyRendered) {
            render();

            alreadyRendered = true;
        }
    }

    private void render() {
        H3 title = new H3("Danmarks Kommuner");
        content.add(title);

        content.add(addTopButtons());

        HorizontalLayout mapLine = new HorizontalLayout();

        MapboxProperties properties = new MapboxProperties(AccessToken.getToken());
        properties.setInitialLocation(GeoLocation.InitialView_Denmark);
        properties.setInitialZoom(6);
        mapboxMap = new MapboxMap(properties);

        mapboxMap.setWidth("1000px");
        mapboxMap.setHeight("500px");
        legend = createLayerLegend();
        mapLine.add(mapboxMap, legend);
        content.add(mapLine);

        content.add(addBottomButtons());

        add(content);
    }

    private HorizontalLayout addTopButtons() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setAlignItems(Alignment.CENTER);

        Button zoomAarhus = new Button("Aarhus", e -> mapboxMap.flyTo(GeoLocation.Aarhus, 12));
        Button zoomAalborg = new Button("Aalborg", e -> mapboxMap.flyTo(GeoLocation.Aalborg, 12));
        Button zoomBornholm = new Button("Bornholm", e -> mapboxMap.flyTo(GeoLocation.Bornholm, 10));
        Button zoomCopenhagen = new Button("Copenhagen", e -> mapboxMap.flyTo(GeoLocation.Copenhagen, 10));
        Button zoomSkagen = new Button("Skagen", e -> mapboxMap.flyTo(GeoLocation.Skagen, 12));
        Button zoomOdense = new Button("Odense", e -> mapboxMap.flyTo(GeoLocation.Odense, 14));

        Button zoomDenmark = new Button("Denmark", e -> mapboxMap.flyTo(GeoLocation.InitialView_Denmark, 6));

        horizontalLayout.add(new Label("Zoom til:"), zoomAarhus, zoomAalborg, zoomBornholm, zoomCopenhagen, zoomSkagen, zoomOdense, ViewUtil.horizontalWhiteSpace(15), zoomDenmark);

        return horizontalLayout;
    }

    private HorizontalLayout addBottomButtons() {
        HorizontalLayout defaultButtons = new HorizontalLayout();
        defaultButtons.setAlignItems(Alignment.CENTER);

        Button addWireframe = new Button("Wireframe", e -> addWireframeLayer("wireframe"));
        Button removeWireframe = new Button("Remove wireframe", e -> removeSelectionLayer("wireframe"));

        Button addSelection = new Button("Add layers", e -> addServiceUsageLayers());
        Button removeSelection = new Button("Remove layers", e -> removeServiceUsageLayers());

        defaultButtons.add(new Label("Add:"), addWireframe, removeWireframe,
                ViewUtil.horizontalWhiteSpace(15), addSelection, removeSelection);

        return defaultButtons;
    }

    private void removeServiceUsageLayers() {
        if (layersActive) {
            for (int i = 1; i < 8; i++) {
                removeSelectionLayer("layer_" + i);
            }
            legend.setVisible(false);
        }
    }

    private LayerLegend createLayerLegend() {
        LayerLegend layerLegend = new LayerLegend();
        layerLegend.setVisible(false);

        layerLegend.addLegendLineFrom(5000000, CURRENT_PALETTE[7 - 1], opacity);
        layerLegend.addLegendLine(2000000, 5000000, CURRENT_PALETTE[6 - 1], opacity);
        layerLegend.addLegendLine(1000000, 2000000, CURRENT_PALETTE[5 - 1], opacity);
        layerLegend.addLegendLine(500000, 1000000, CURRENT_PALETTE[4 - 1], opacity);
        layerLegend.addLegendLine(200000, 5000000, CURRENT_PALETTE[3 - 1], opacity);
        layerLegend.addLegendLine(20000, 200000, CURRENT_PALETTE[2 - 1], opacity);
        layerLegend.addLegendLine(2000, 20000, CURRENT_PALETTE[1 - 1], opacity);

        return layerLegend;
    }

    private void addServiceUsageLayers() {
        addSourceIfNeeded();

        DataLoader dataLoader = new DataLoader(DataLoader.DATA_FILE);
        dataLoader.load();

        List<DataLoader.KommuneData> list = dataLoader.getData();

        if (list != null && list.size() > 0) {
            for (int i = 1; i < 8; i++) {
                Layer layer = LayerHelper.createBinLayer("layer_" + i, LayerHelper.mapToJSONArray(getKommunerForBin(list, i)), CURRENT_PALETTE[i - 1], opacity);
                mapboxMap.addLayer(layer);
            }
            legend.setVisible(true);
            layersActive = true;
        }
    }

    public Set<String> getKommunerForBin(List<DataLoader.KommuneData> data, int bin) {
        Set<String> layerData = data.stream().filter(item -> item.getBin() == bin).map(DataLoader.KommuneData::getKommune).collect(Collectors.toSet());
        return layerData;
    }

    private void addWireframeLayer(String layerId) {
        if (!sourceAlreadyAdded) {
            addSource();
            // mapboxMap.addSource("Danske_Kommuner", source);
            sourceAlreadyAdded = true;
        }

        Paint paint = new Paint(Paint.Type.line);
        paint.setLineColor("#ff69b4");
        paint.setLineWidth(1);

        Layer layer = new Layer(layerId, Layer.Type.line);
        // layer.setLayout(lineLayout);
        layer.setPaint(paint);
        layer.put("source", "danske-kommuner");
        layer.put("source-layer", "Danske_Kommuner");

        mapboxMap.addLayer(layer);
    }

    private void addSourceIfNeeded() {
        if (!sourceAlreadyAdded) {
            addSource();
            sourceAlreadyAdded = true;
        }
    }

    @Deprecated // To be moved to mapboxMap
    private void removeSelectionLayer(String layerId) {
        // mapboxMap.hideLayer("");
        String command = "this.map.removeLayer('" + layerId + "')";
        mapboxMap.executeJs(command);
    }

    private void addSource() {
        Source source = new Source();
        source.setType("vector");
        source.put("url", "mapbox://markhm.ck4aedo8f03l32nmyksha2171-5k3o4");

        mapboxMap.addSource("danske-kommuner", source);
    }

    private static final String[] DK_KOMMUNER = {"Herning", "København", "Århus", "Næstved", "Aalborg", "Faaborg-Midtfyn", "Esbjerg", "Frederiksberg", "Skanderborg",
            "Haderslev", "Holstebro", "Albertslund", "Guldborgsund", "Fredensborg", "Randers", "Skive", "Ballerup", "Rudersdal", "Kolding", "Norddjurs",
            "Mariagerfjord", "Lyngby-Taarbæk", "Viborg", "Odense", "Silkeborg", "Kerteminde", "Roskilde", "Slagelse", "Aabenraa", "Holbæk", "Hvidovre", "Svendborg", "Horsens",
            "Helsingør", "Sønderborg", "Syddjurs", "Middelfart", "Vejle", "Kalundborg", "Høje-Taastrup", "Varde", "Lolland", "Vejen", "Nyborg", "Odder", "Gladsaxe", "Fredericia",
            "Favrskov", "Gentofte", "Thisted", "Frederikshavn", "Brøndby", "Ærø", "Ringkøbing-Skjern", "Hillerød", "Gribskov", "Hjørring", "Egedal", "Odsherred", "Billund", "Rødovre",
            "Nordfyns", "Vordingborg", "Frederikssund", "Køge", "Greve", "Halsnæs", "Assens", "Ikast-Brande", "Vesthimmerlands", "Bornholm", "Tårnby", "Jammerbugt", "Herlev", "Lemvig",
            "Furesø", "Sorø", "Vallensbæk", "Glostrup", "Morsø", "Stevns", "Struer", "Allerød", "Tønder", "Solrød", "Rebild", "Ishøj", "Brønderslev", "Faxe", "Ringsted", "Fanø",
            "Hørsholm", "Langeland", "Lejre", "Samsø", "Dragør", "Læsø"};

    private String[] kommuner_1 = {"Skive", "Haderslev"};
    private String[] kommuner_2 = {"Odder", "Lejre"};
    private String[] kommuner_3 = {"Køge", "Nyborg"};
    private String[] kommuner_4 = {"Brønderslev", "Esbjerg"};
    private String[] kommuner_5 = {"Aarhus", "Rødovre"};

}

// --------------------------------------------
// Working:
//
//    String layerCommand = "map.addLayer({" +
//            "'id': 'terrain-data'," +
//            "'type': 'line'," +
//            "'source': 'danske-kommuner'," +
//            "'source-layer': 'Danske_Kommuner'," +
//            "'layout': {" +
//            "'line-join': 'round'," +
//            "'line-cap': 'round'" +
//            "}," +
//            "'paint': " +
//            "{" +
//            "'line-color': '#ff69b4'," +
////                    "'fill-color': '#ff8ad0'," +
//            "'line-width': 1" +
//            "}" +
//            "})";

// Working:
//        String addLayerLimited = "map.addLayer({" +
//                "    'id': 'color'," +
//                "    'type': 'fill'," +
//                "    'source': 'danske-kommuner'," +
//                "    'source-layer': 'Danske_Kommuner'," +
//                "    'paint': " +
//                "    {" +
//                "      'fill-color': ['match', ['get', 'name'], ['Skive'], 'hsla(271, 38%, 61%, 0.5)', 'hsla(0, 0%, 0%, 0)']" +
//                "    }" +
//                "      }, );";