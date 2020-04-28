package com.github.markhm.mapbox.ui.dk;

import com.github.markhm.mapbox.AccessToken;
import com.github.markhm.mapbox.GeoLocation;
import com.github.markhm.mapbox.MapboxMap;
import com.github.markhm.mapbox.ViewUtil;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import mapboxflow.layer.Layer;
import mapboxflow.layer.Paint;
import mapboxflow.layer.Source;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Route("dk_map")
public class DKKommunerView extends VerticalLayout
{
    private static Log log = LogFactory.getLog(DKKommunerView.class);

    private MapboxMap mapboxMap = null;

    boolean alreadyRendered = false;

    private VerticalLayout content = new VerticalLayout();

    private boolean sourceAlreadyAdded = false;

    public DKKommunerView()
    {
        setAlignItems(Alignment.CENTER);

        content.setAlignItems(Alignment.START);
        content.setWidth("1200px");

        if (!alreadyRendered)
        {
            render();

            alreadyRendered = true;
        }
    }

    private void render()
    {
        H3 title = new H3("Danmarks Kommuner");
        content.add(title);

        content.add(addTopButtons());

        mapboxMap = new MapboxMap(AccessToken.getToken(), GeoLocation.InitialView_Denmark, 6);
        mapboxMap.setWidth("1200px");
        mapboxMap.setHeight("700px");
        content.add(mapboxMap);

        content.add(addSelectionButtons());
        content.add(addBottomButtons());

        add(content);
    }

    private HorizontalLayout addTopButtons()
    {
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

    private HorizontalLayout addBottomButtons()
    {
        HorizontalLayout defaultButtons = new HorizontalLayout();
        defaultButtons.setAlignItems(Alignment.CENTER);

        Button addWireframe = new Button("Trådramme", e -> addWireframeLayer("wireframe"));
        Button removeWireframe = new Button("Fjern trådramme", e -> removeSelectionLayer("wireframe"));

        Button addSelection = new Button("Udvalgte kommuner", e -> addSelectionLayer_1());
        Button removeSelection = new Button("Fjern markering", e -> removeSelectionLayer("layer_1"));

        defaultButtons.add(new Label("Tilføj:"), addWireframe, removeWireframe,
                ViewUtil.horizontalWhiteSpace(15), addSelection, removeSelection);

        Button addData = new Button("Add service usage data", e -> addServiceUsageData());

        return defaultButtons;
    }

    private HorizontalLayout addSelectionButtons()
    {
        HorizontalLayout selectionButtons = new HorizontalLayout();
        selectionButtons.setAlignItems(Alignment.CENTER);

        Button addAllSelections = new Button("All selections", e ->
        {
            addSelectionLayer_1();
            addSelectionLayer_2();
            addSelectionLayer_3();
            addSelectionLayer_4();
            addSelectionLayer_5();
        });

        Button addSelection_1 = new Button("Selection 1", e -> addSelectionLayer_1());
        Button addSelection_2 = new Button("Selection 2", e -> addSelectionLayer_2());
        Button addSelection_3 = new Button("Selection 3", e -> addSelectionLayer_3());
        Button addSelection_4 = new Button("Selection 4", e -> addSelectionLayer_4());
        Button addSelection_5 = new Button("Selection 5", e -> addSelectionLayer_5());

        Button addSelectionRandom = new Button("Selection Random", e -> addSelectionLayerRandom());

        selectionButtons.add(new Label("Tilføj:"), addAllSelections, ViewUtil.horizontalWhiteSpace(15),
                addSelection_1, addSelection_2, addSelection_3, addSelection_4, addSelection_5, ViewUtil.horizontalWhiteSpace(15), addSelectionRandom);

        return selectionButtons;
    }

    public List<String> pickRandomKommuner(int amount)
    {
        int currentAmount = 0;
        List<String> result = new ArrayList<>();
        while (currentAmount < amount)
        {
            String nextPick = pickRandomKommune();
            if (!result.contains(nextPick))
            {
                result.add(nextPick);
                currentAmount++;
            }
        }
        return result;
    }

    public String pickRandomKommune()
    {
        Random random = new Random();
        int luckyNumber = random.nextInt(99);
        return DK_KOMMUNER[luckyNumber];
    }

    private void addServiceUsageData()
    {

    }

    private void addWireframeLayer(String layerId)
    {
        if (!sourceAlreadyAdded)
        {
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

    private String color1 = "hsla(120, 100%, 10%, 0.7)";
    private String color2 = "hsla(120, 100%, 18%, 0.7)";
    private String color3 = "hsla(120, 100%, 26%, 0.7)";
    private String color4 = "hsla(120, 100%, 34%, 0.7)";
    private String color5 = "hsla(120, 100%, 42%, 0.7)";

//    private String color2 = "hsla(271, 38%, 51%, 0.5)";
//    private String color3 = "hsla(271, 38%, 41%, 0.5)";
//    private String color4 = "hsla(271, 38%, 31%, 0.5)";
//    private String color5 = "hsla(271, 38%, 21%, 0.5)";

    private void addSourceIfNeeded()
    {
        if (!sourceAlreadyAdded)
        {
            addSource();
            sourceAlreadyAdded = true;
        }
    }

    private void addSelectionLayer_1()
    {
        addSourceIfNeeded();

        String colorRgB = "rgb(157, 118, 193)";

        // String niceBlue = "hsla(195, 100%, 50%, 0.5)";
        // String[] kommuner = {"Skive", "Haderslev", "Esbjerg", "Lejre", "Køge", "Nyborg", "Brønderslev", "Rødovre", "Aarhus", "Odder"};
        String[] kommuner_1 = {"Skive", "Haderslev"};
        Layer layer_1 = LayerHelper.createBinLayer("layer_1", LayerHelper.mapToJSONArray(kommuner_1), color1);
        mapboxMap.addLayer(layer_1);
    }

    private void addSelectionLayer_2()
    {
        addSourceIfNeeded();

        String[] kommuner_2 = {"Odder", "Lejre"};
        Layer layer_2 = LayerHelper.createBinLayer("layer_2", LayerHelper.mapToJSONArray(kommuner_2), color2);
        mapboxMap.addLayer(layer_2);
    }

    private void addSelectionLayer_3()
    {
        addSourceIfNeeded();

        String[] kommuner_3 = {"Køge", "Nyborg"};
        Layer layer_3 = LayerHelper.createBinLayer("layer_3", LayerHelper.mapToJSONArray(kommuner_3), color3);
        mapboxMap.addLayer(layer_3);
    }

    private void addSelectionLayer_4()
    {
        addSourceIfNeeded();

        String[] kommuner_4 = {"Brønderslev", "Esbjerg"};
        Layer layer_4 = LayerHelper.createBinLayer("layer_4", LayerHelper.mapToJSONArray(kommuner_4), color4);
        mapboxMap.addLayer(layer_4);
    }

    private void addSelectionLayer_5()
    {
        addSourceIfNeeded();

        String[] kommuner_5 = {"Aarhus", "Rødovre"};
        Layer layer_5 = LayerHelper.createBinLayer("layer_5", LayerHelper.mapToJSONArray(kommuner_5), color5);
        mapboxMap.addLayer(layer_5);
    }

    int layerCounter = 6;

    private void addSelectionLayerRandom()
    {
        addSourceIfNeeded();

        Layer newLayer = LayerHelper.createBinLayer("layer_"+layerCounter, LayerHelper.mapToJSONArray(pickRandomKommuner(5)), color1);
        mapboxMap.addLayer(newLayer);

        layerCounter++;
    }

    @Deprecated // To be moved to mapboxMap
    private void removeSelectionLayer(String layerId)
    {
        // mapboxMap.hideLayer("");
        String command = "this.map.removeLayer('" + layerId + "')";
        mapboxMap.executeJs(command);
    }

    private void addSource()
    {
        Source source = new Source();
        source.setType("vector");
        source.put("url", "mapbox://markhm.ck4aedo8f03l32nmyksha2171-5k3o4");

        mapboxMap.addSource("danske-kommuner", source);
    }

    private static final String[] DK_KOMMUNER = {"Herning", "København", "Århus", "Næstved", "Aalborg", "Faaborg-Midtfyn", "Esbjerg", "Frederiksberg", "Skanderborg",
            "Haderslev","Holstebro", "Albertslund", "Guldborgsund", "Fredensborg", "Randers", "Skive", "Ballerup", "Rudersdal", "Kolding", "Norddjurs",
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