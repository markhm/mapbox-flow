package com.github.markhm.mapbox;

/* A selection of Mapbox Maki Sprites. Full list via: https://labs.mapbox.com/maki-icons/.
 */
public class Sprite {
    private String id = null;

    private Sprite(String id) {
        this.id = id;
    }

    public String toString() {
        return id;
    }

    // Available in the default map
    public static final Sprite Airport = new Sprite("airport");
    public static final Sprite Car = new Sprite("car");

    public static final Sprite Bank = new Sprite("bank");
    public static final Sprite Helicopter = new Sprite("heliport");
    public static final Sprite Fire_Station = new Sprite("fire-station");

    // Unevaluated
    public static final Sprite Harbor = new Sprite("harbor");

    public static final Sprite Airplane = new Sprite("airport");
    public static final Sprite Bicycle = new Sprite("bicycle");
    public static final Sprite Boat = new Sprite("ferry");
    public static final Sprite Bus = new Sprite("bus");

    public static final Sprite Rocket = new Sprite("rocket");
    public static final Sprite Train = new Sprite("rail");

    public static final Sprite Doctor = new Sprite("doctor");
    public static final Sprite Police = new Sprite("police");
    public static final Sprite Hospital = new Sprite("hospital");

    public static final Sprite Globe = new Sprite("globe");
    public static final Sprite Information = new Sprite("information");
    public static final Sprite Marker = new Sprite("marker");
    public static final Sprite Marker_Open = new Sprite("marker-stroked");

    public static final Sprite Flag = new Sprite("embassy");

    public static final Sprite Heart = new Sprite("heart");
    public static final Sprite Star = new Sprite("star");
    public static final Sprite Star_Open = new Sprite("star-stroked");
    public static final Sprite Circle = new Sprite("circle");
    public static final Sprite Circle_Open = new Sprite("circle-stroked");
    public static final Sprite Triangle = new Sprite("triangle");
    public static final Sprite Triangle_Open = new Sprite("circle-stroked");

    public static final Sprite Home = new Sprite("home");

    public static final Sprite Monument = new Sprite("monument");

    public static final Sprite Building = new Sprite("building");
    public static final Sprite Castle = new Sprite("castle");
    public static final Sprite Commercial = new Sprite("commercial");
    public static final Sprite Convenience = new Sprite("convenience");
    public static final Sprite Factory = new Sprite("industry");
    public static final Sprite Farm = new Sprite("farm");
    public static final Sprite Tulip = new Sprite("garden");
    public static final Sprite Shop = new Sprite("shop");
    public static final Sprite Town_hall = new Sprite("town-hall");
    public static final Sprite Warehouse = new Sprite("warehouse");
    public static final Sprite Windmill = new Sprite("windmil");

    // Unavailable in the default map
    public static final Sprite Danger = new Sprite("danger");
    public static final Sprite Cross = new Sprite("cross");
}
