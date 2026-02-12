package com.github.markhm.mapbox;

/* A selection of Mapbox Maki Sprites. Full list via: https://labs.mapbox.com/maki-icons/.
 */
public class MapboxSprite {
    private String id = null;

    private MapboxSprite(String id) {
        this.id = id;
    }

    public String toString() {
        return id;
    }

    // Available in the default map
    public static final MapboxSprite Airport = new MapboxSprite("airport");
    public static final MapboxSprite Car = new MapboxSprite("car");

    public static final MapboxSprite Bank = new MapboxSprite("bank");
    public static final MapboxSprite Helicopter = new MapboxSprite("heliport");
    public static final MapboxSprite Fire_Station = new MapboxSprite("fire-station");

    // Unevaluated
    public static final MapboxSprite Harbor = new MapboxSprite("harbor");

    public static final MapboxSprite Airplane = new MapboxSprite("airport");
    public static final MapboxSprite Bicycle = new MapboxSprite("bicycle");
    public static final MapboxSprite Boat = new MapboxSprite("ferry");
    public static final MapboxSprite Bus = new MapboxSprite("bus");

    public static final MapboxSprite Rocket = new MapboxSprite("rocket");
    public static final MapboxSprite Train = new MapboxSprite("rail");

    public static final MapboxSprite Doctor = new MapboxSprite("doctor");
    public static final MapboxSprite Police = new MapboxSprite("police");
    public static final MapboxSprite Hospital = new MapboxSprite("hospital");

    public static final MapboxSprite Globe = new MapboxSprite("globe");
    public static final MapboxSprite Information = new MapboxSprite("information");
    public static final MapboxSprite Marker = new MapboxSprite("marker");
    public static final MapboxSprite Marker_Open = new MapboxSprite("marker-stroked");

    public static final MapboxSprite Flag = new MapboxSprite("embassy");

    public static final MapboxSprite Heart = new MapboxSprite("heart");
    public static final MapboxSprite Star = new MapboxSprite("star");
    public static final MapboxSprite Star_Open = new MapboxSprite("star-stroked");
    public static final MapboxSprite Circle = new MapboxSprite("circle");
    public static final MapboxSprite Circle_Open = new MapboxSprite("circle-stroked");
    public static final MapboxSprite Triangle = new MapboxSprite("triangle");
    public static final MapboxSprite Triangle_Open = new MapboxSprite("circle-stroked");

    public static final MapboxSprite Home = new MapboxSprite("home");

    public static final MapboxSprite Monument = new MapboxSprite("monument");

    public static final MapboxSprite Building = new MapboxSprite("building");
    public static final MapboxSprite Castle = new MapboxSprite("castle");
    public static final MapboxSprite Commercial = new MapboxSprite("commercial");
    public static final MapboxSprite Convenience = new MapboxSprite("convenience");
    public static final MapboxSprite Factory = new MapboxSprite("industry");
    public static final MapboxSprite Farm = new MapboxSprite("farm");
    public static final MapboxSprite Tulip = new MapboxSprite("garden");
    public static final MapboxSprite Shop = new MapboxSprite("shop");
    public static final MapboxSprite Town_hall = new MapboxSprite("town-hall");
    public static final MapboxSprite Warehouse = new MapboxSprite("warehouse");
    public static final MapboxSprite Windmill = new MapboxSprite("windmill");

    // Unavailable in the default map
    public static final MapboxSprite Danger = new MapboxSprite("danger");
    public static final MapboxSprite Cross = new MapboxSprite("cross");
}
