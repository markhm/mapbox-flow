package com.github.markhm.mapbox;

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
import org.json.JSONArray;

@Route("dk_map")
@Deprecated
public class DKKommunerView extends VerticalLayout
{
    private static Log log = LogFactory.getLog(DKKommunerView.class);

    // private DeprecatedMapboxMap mapboxMap = null;
    private MapboxMap mapboxMap = null;

    boolean alreadyRendered = false;

    public DKKommunerView()
    {
        if (!alreadyRendered)
        {
            render();

            alreadyRendered = true;
        }
    }

    private void render()
    {
        H3 title = new H3("Danmarks Kommuner");
        add(title);

        addTopButtons();

        // mapboxMap = new DeprecatedMapboxMap(GeoLocation.InitialView_Denmark, 6, false);
        mapboxMap = new MapboxMap(AccessToken.getToken(), GeoLocation.InitialView_Denmark, 6);
        mapboxMap.setWidth("1200px");
        mapboxMap.setHeight("700px");
        add(mapboxMap);

        addBottomButtons();
    }

    private void addTopButtons()
    {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setAlignItems(Alignment.CENTER);

        Button zoomAarhus = new Button("Aarhus", e -> mapboxMap.flyTo(GeoLocation.Aarhus, 12));
        Button zoomAalborg = new Button("Aalborg", e -> mapboxMap.flyTo(GeoLocation.Aalborg, 12));
        Button zoomBornholm = new Button("Bornholm", e -> mapboxMap.flyTo(GeoLocation.Bornholm, 10));
        Button zoomCopenhagen = new Button("Copenhagen", e -> mapboxMap.flyTo(GeoLocation.Copenhagen, 10));
        Button zoomSkagen = new Button("Skagen", e -> mapboxMap.flyTo(GeoLocation.Skagen, 12));
        Button zoomParis = new Button("Odense", e -> mapboxMap.flyTo(GeoLocation.Odense, 14));

        Button zoomDenmark = new Button("Denmark", e -> mapboxMap.flyTo(GeoLocation.InitialView_Denmark, 6));

        horizontalLayout.add(new Label("Zoom til:"), zoomAarhus, zoomAalborg, zoomBornholm, zoomCopenhagen, zoomSkagen, zoomParis, zoomDenmark);

        add(horizontalLayout);
    }

    private void addBottomButtons()
    {
        HorizontalLayout defaultButtons = new HorizontalLayout();
        defaultButtons.setAlignItems(Alignment.CENTER);

        Button addWireframe = new Button("Trådramme", e -> addWireframeLayer());
        Button addSelection = new Button("Udvalgte kommuner", e -> addSelectionLayer());

        Button removeSelection = new Button("Fjern markering", e -> removeSelection());

        defaultButtons.add(new Label("Tilføj:"), addWireframe, addSelection, removeSelection);

        add(defaultButtons);
    }

    private void addWireframeLayer()
    {
        Source source = new Source();
        source.setType("vector");
        source.put("url", "mapbox://markhm.ck4aedo8f03l32nmyksha2171-5k3o4");

        mapboxMap.addSource("danske-kommuner", source);

        Paint paint = new Paint(Paint.Type.line);
        paint.setLineColor("#ff69b4");
        paint.setLineWidth(1);

        Layer layer = new Layer("terrain-data", Layer.Type.line);
        // layer.setLayout(lineLayout);
        layer.setPaint(paint);
        layer.put("source", "danske-kommuner");
        layer.put("source-layer", "Danske_Kommuner");

        mapboxMap.addLayer(layer);
    }

    private JSONArray getKommuner()
    {
        String[] kommuner = {"Skive","Haderslev","Esbjerg","Lejre","Køge","Nyborg","Brønderslev","Rødovre","Aarhus","Odder"};

        JSONArray resultArray = new JSONArray();

        for (int i=0; i < kommuner.length; i++)
        {
            resultArray.put(i, kommuner[i]);
        }

        return resultArray;
    }

    private void addSelectionLayer()
    {
        Source source = new Source();
        source.setType("vector");
        source.put("url", "mapbox://markhm.ck4aedo8f03l32nmyksha2171-5k3o4");

        mapboxMap.addSource("danske-kommuner", source);

        Layer layer = new Layer("color", Layer.Type.fill);
        layer.put("source", "danske-kommuner");
        layer.put("source-layer", "Danske_Kommuner");
        Paint paint = new Paint(Paint.Type.fill);

        JSONArray paintArray = new JSONArray();

        JSONArray paintNameArray = new JSONArray();
        paintNameArray.put(0, "get");
        paintNameArray.put(1, "name");

        paintArray.put(0, "match");
        paintArray.put(1, paintNameArray);
        paintArray.put(2, getKommuner());
        paintArray.put(3, "hsla(271, 38%, 61%, 0.5)");
        paintArray.put(4, "hsla(0, 0%, 0%, 0)");

        paint.setFillColor(paintArray);
        layer.setPaint(paint);

        if (!sourceAlreadyAdded)
        {
            mapboxMap.addSource("Danske_Kommuner", source);
            sourceAlreadyAdded = true;
        }

        mapboxMap.addLayer(layer);
    }

    private boolean sourceAlreadyAdded = false;

    private void removeSelection()
    {
        String command = "this.map.removeLayer('color')";
        mapboxMap.executeJs(command);
    }
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