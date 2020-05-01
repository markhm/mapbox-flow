package com.github.markhm.mapbox.ui.dk;

import com.github.markhm.mapbox.AccessToken;
import com.github.markhm.mapbox.GeoLocation;
import com.github.markhm.mapbox.MapboxMap;
import com.github.markhm.mapbox.ViewUtil;
import com.github.markhm.mapbox.component.LayerLegend;
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
import sun.security.provider.SHA;

import java.util.*;
import java.util.stream.Collectors;

@Route("dk_map")
public class DKKommunerView extends VerticalLayout
{
    private static Log log = LogFactory.getLog(DKKommunerView.class);

    private MapboxMap mapboxMap = null;
    private LayerLegend legend = null;

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
        H3 title = new H3("Servicebrug af Danmarks Kommuner");
        content.add(title);

        content.add(addTopButtons());

        HorizontalLayout mapLine = new HorizontalLayout();
        mapboxMap = new MapboxMap(AccessToken.getToken(), GeoLocation.InitialView_Denmark, 6);
        mapboxMap.setWidth("1000px");
        mapboxMap.setHeight("500px");
        legend = createLayerLegend();
        mapLine.add(mapboxMap, legend);
        content.add(mapLine);

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

        Button addSelection = new Button("Servicebrug nov 2019", e -> addServiceUsageLayers());
        Button removeSelection = new Button("Fjern markering", e -> removeServiceUsageLayers());

        defaultButtons.add(new Label("Tilføj:"), addWireframe, removeWireframe,
                ViewUtil.horizontalWhiteSpace(15), addSelection, removeSelection);

        return defaultButtons;
    }

    private void removeServiceUsageLayers()
    {
        for (int i = 1; i < 8 ; i++)
        {
            removeSelectionLayer("layer_"+i);
        }

        legend.setVisible(false);
    }

    private LayerLegend createLayerLegend()
    {
        LayerLegend layerLegend = new LayerLegend();
        layerLegend.setVisible(false);

        layerLegend.addLegendLineFrom(5000000, SHADES_OF_GREEN[7 - 1]);
        layerLegend.addLegendLine(2000000, 5000000, SHADES_OF_GREEN[6 - 1]);
        layerLegend.addLegendLine(1000000, 2000000, SHADES_OF_GREEN[5 - 1]);
        layerLegend.addLegendLine(500000, 1000000, SHADES_OF_GREEN[4 - 1]);
        layerLegend.addLegendLine(200000, 5000000, SHADES_OF_GREEN[3 - 1]);
        layerLegend.addLegendLine(20000, 200000, SHADES_OF_GREEN[2 - 1]);
        layerLegend.addLegendLine(2000, 20000, SHADES_OF_GREEN[1 - 1]);

        return layerLegend;
    }

    private void addServiceUsageLayers()
    {
        addSourceIfNeeded();

        DataLoader dataLoader = new DataLoader(DataLoader.DATA_FILE);
        dataLoader.load();

        List<DataLoader.KommuneData> list = dataLoader.getData();

        for (int i = 1 ; i < 8 ; i++)
        {
            Layer layer = LayerHelper.createBinLayer("layer_"+i, LayerHelper.mapToJSONArray(getKommunerForBin(list, i)), SHADES_OF_GREEN[i-1]);
            mapboxMap.addLayer(layer);
        }

        legend.setVisible(true);
    }

    public Set<String> getKommunerForBin(List<DataLoader.KommuneData> data, int bin)
    {
        Set<String> layerData = data.stream().filter(item -> item.getBin() == bin).map(DataLoader.KommuneData::getKommune).collect(Collectors.toSet());
        return layerData;
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

    private void addSourceIfNeeded()
    {
        if (!sourceAlreadyAdded)
        {
            addSource();
            sourceAlreadyAdded = true;
        }
    }

//    private void addSelectionLayer(String layerId, String[] kommuner, String color)
//    {
//        addSourceIfNeeded();
//
//        // String colorRgB = "rgb(157, 118, 193)";
//
//        // String niceBlue = "hsla(195, 100%, 50%, 0.5)";
//        // String[] kommuner = {"Skive", "Haderslev", "Esbjerg", "Lejre", "Køge", "Nyborg", "Brønderslev", "Rødovre", "Aarhus", "Odder"};
//
//        Layer layer = LayerHelper.createBinLayer("layer_"+layerId, LayerHelper.mapToJSONArray(kommuner), color);
//        mapboxMap.addLayer(layer);
//    }

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