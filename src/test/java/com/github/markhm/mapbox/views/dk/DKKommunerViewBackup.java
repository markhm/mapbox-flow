package com.github.markhm.mapbox.views.dk;

import com.github.markhm.mapbox.*;
import com.github.markhm.mapbox.component.LayerLegend;
import com.github.markhm.mapbox.util.AccessToken;
import com.github.markhm.mapbox.util.ViewUtil;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import elemental.json.Json;
import elemental.json.JsonObject;
import mapboxflow.jsonobject.layer.Layer;
import mapboxflow.jsonobject.layer.Paint;
import mapboxflow.jsonobject.layer.Source;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Route("dk_map_backup")
public class DKKommunerViewBackup extends VerticalLayout {
    private static Log log = LogFactory.getLog(DKKommunerViewBackup.class);

    private MapboxMap mapboxMap = null;

    boolean alreadyRendered = false;

    private VerticalLayout content = new VerticalLayout();

    private boolean sourceAlreadyAdded = false;

    public DKKommunerViewBackup() {
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

        MapboxProperties properties = new MapboxProperties(AccessToken.getToken());
        properties.setInitialLocation(GeoLocation.InitialView_Denmark);
        properties.setInitialZoom(6);
        mapboxMap = new MapboxMap(properties);

        mapboxMap.setWidth("1000px");
        mapboxMap.setHeight("500px");
        content.add(mapboxMap);

        content.add(addSelectionButtons());
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

        Button addWireframe = new Button("Trådramme", e -> addWireframeLayer("wireframe"));
        Button addWireframeviaJson = new Button("Trådramme via JsonObject", e -> addWireframeViaJson("wireframe"));
        Button removeWireframe = new Button("Fjern trådramme", e -> removeSelectionLayer("wireframe"));

        // Button addSelection = new Button("Udvalgte kommuner", e -> addSelectionLayer());
        Button removeSelection = new Button("Fjern markering", e -> removeSelectionLayer("layer_1"));

        Button addData = new Button("Add service usage data", e -> addServiceUsageData());

        defaultButtons.add(new Label("Tilføj:"), addWireframe, addWireframeviaJson, removeWireframe,
                ViewUtil.horizontalWhiteSpace(15), removeSelection, ViewUtil.horizontalWhiteSpace(15),
                addData);

        return defaultButtons;
    }

    private HorizontalLayout addSelectionButtons() {
        HorizontalLayout selectionButtons = new HorizontalLayout();
        selectionButtons.setAlignItems(Alignment.CENTER);

        Set<String> random1 = pickRandomKommuner(10);
        Set<String> random2 = pickRandomKommuner(10);
        Set<String> random3 = pickRandomKommuner(10);
        Set<String> random4 = pickRandomKommuner(10);
        Set<String> random5 = pickRandomKommuner(10);
        Set<String> random6 = pickRandomKommuner(10);
        Set<String> random7 = pickRandomKommuner(10);

        Button addSelection_1 = new Button("Selection 1", e -> addSelectionLayer("1", random1.toArray(new String[random1.size()]), green_1));
        Button addSelection_2 = new Button("Selection 2", e -> addSelectionLayer("2", random2.toArray(new String[random2.size()]), green_2));
        Button addSelection_3 = new Button("Selection 3", e -> addSelectionLayer("3", random3.toArray(new String[random3.size()]), green_3));
        Button addSelection_4 = new Button("Selection 4", e -> addSelectionLayer("4", random4.toArray(new String[random4.size()]), green_4));
        Button addSelection_5 = new Button("Selection 5", e -> addSelectionLayer("5", random5.toArray(new String[random5.size()]), green_5));
        Button addSelection_6 = new Button("Selection 6", e -> addSelectionLayer("6", random6.toArray(new String[random6.size()]), green_6));
        Button addSelection_7 = new Button("Selection 7", e -> addSelectionLayer("7", random7.toArray(new String[random7.size()]), green_7));

        Button addSelectionRandom = new Button("Selection Random", e -> addSelectionLayerRandom());

        selectionButtons.add(new Label("Tilføj:"),
                addSelection_1, addSelection_2, addSelection_3, addSelection_4, addSelection_5, addSelection_6, addSelection_7,
                ViewUtil.horizontalWhiteSpace(15), addSelectionRandom);

        // addAllSelections, ViewUtil.horizontalWhiteSpace(15),
        return selectionButtons;
    }

    Set<String> alreadyChosen = new HashSet<>();

    public Set<String> pickRandomKommuner(int amount) {
        Set<String> result = new HashSet<>();
        int currentAmount = 0;

        while (currentAmount < amount) {
            String nextPick = pickRandomKommune();
            if (!result.contains(nextPick) && !alreadyChosen.contains(nextPick)) {
                result.add(nextPick);
                currentAmount++;
            }
        }

        alreadyChosen.addAll(result);
        return result;
    }

    public String pickRandomKommune() {
        Random random = new Random();
        int luckyNumber = random.nextInt(96);
        return DK_KOMMUNER[luckyNumber];
    }

    private void addServiceUsageData() {
        addSourceIfNeeded();

        DataLoader dataLoader = new DataLoader(DataLoader.DATA_FILE);
        dataLoader.load();

        List<DataLoader.KommuneData> list = dataLoader.getData();

        list.forEach(item -> {
            if (item.getKommune().equals("Århus")) log.info("Yes, we found: " + item.toString());
        });

        LayerLegend layerLegend = new LayerLegend();
        for (int i = 1; i < 8; i++) {
            Layer layer = LayerHelper.createBinLayer("layer" + i, LayerHelper.mapToJSONArray(getKommunerForBin(list, i)), SHADES_OF_GREEN[i - 1], 0.7);
            mapboxMap.addLayer(layer);
        }
    }

    public Set<String> getKommunerForBin(List<DataLoader.KommuneData> data, int bin) {
        Set<String> layerData = data.stream().filter(item -> item.getBin() == bin).map(DataLoader.KommuneData::getKommune).collect(Collectors.toSet());
        return layerData;
    }


    private void addWireframeViaJson(String layerId) {
        JsonObject layer = Json.createObject();
        layer.put("id", layerId);
        layer.put("type", "line");

        if (!sourceAlreadyAdded) {
            addSource();
            // mapboxMap.addSource("Danske_Kommuner", source);
            sourceAlreadyAdded = true;
        }

        JsonObject paint = Json.createObject();
        paint.put("line-color", "#ff69b4");
        paint.put("line-width", 1);

        layer.put("paint", paint);
        layer.put("source", "danske-kommuner");
        layer.put("source-layer", "Danske_Kommuner");

        mapboxMap.addLayer(layer);
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
    // private String color1 = "hsla(271, 38%, 61%, 0.5)";

    private String green_1 = "hsla(120, 100%, 66%, 0.7)";
    private String green_2 = "hsla(120, 100%, 50%, 0.7)";
    private String green_3 = "hsla(120, 100%, 42%, 0.7)";
    private String green_4 = "hsla(120, 100%, 34%, 0.7)";
    private String green_5 = "hsla(120, 100%, 26%, 0.7)";
    private String green_6 = "hsla(120, 100%, 18%, 0.7)";
    private String green_7 = "hsla(120, 100%, 10%, 0.7)";

    private String[] SHADES_OF_GREEN = {green_1, green_2, green_3, green_4, green_5, green_6, green_7};

    private String[] kommuner_1 = {"Skive", "Haderslev"};
    private String[] kommuner_2 = {"Odder", "Lejre"};
    private String[] kommuner_3 = {"Køge", "Nyborg"};
    private String[] kommuner_4 = {"Brønderslev", "Esbjerg"};
    private String[] kommuner_5 = {"Aarhus", "Rødovre"};

    private void addSourceIfNeeded() {
        if (!sourceAlreadyAdded) {
            addSource();
            sourceAlreadyAdded = true;
        }
    }

    private void addSelectionLayer(String layerId, String[] kommuner, String color) {
        addSourceIfNeeded();

        // String colorRgB = "rgb(157, 118, 193)";

        // String niceBlue = "hsla(195, 100%, 50%, 0.5)";
        // String[] kommuner = {"Skive", "Haderslev", "Esbjerg", "Lejre", "Køge", "Nyborg", "Brønderslev", "Rødovre", "Aarhus", "Odder"};

        Layer layer = LayerHelper.createBinLayer("layer_" + layerId, LayerHelper.mapToJSONArray(kommuner), color, 0.7);
        mapboxMap.addLayer(layer);
    }

    int layerCounter = 6;

    private void addSelectionLayerRandom() {
        addSourceIfNeeded();

        Layer newLayer = LayerHelper.createBinLayer("layer_" + layerCounter, LayerHelper.mapToJSONArray(pickRandomKommuner(5)), green_5, 0.7);
        mapboxMap.addLayer(newLayer);

        layerCounter++;
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