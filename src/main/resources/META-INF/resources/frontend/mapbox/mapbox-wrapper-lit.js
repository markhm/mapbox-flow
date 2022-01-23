// import {PolymerElement} from '@polymer/polymer/polymer-element.js';
// import {html} from '@polymer/polymer/lib/utils/html-tag.js';
// import '@polymer/polymer/lib/utils/html-tag.js';
import {LitElement, html} from "lit";

// Based on https://github.com/appreciated/apexcharts-flow/blob/master/src/main/resources/META-INF/resources/frontend/com/github/appreciated/apexcharts/apexcharts-wrapper.js

class MapboxWrapperLit extends LitElement {

    static get template() {
        return html`
        <slot></slot>
    `;
    }

    static get is() {
        console.log("**** - At is()");
        return 'mapbox-wrapper-lit';
    }

    static get properties() {

        console.log("**** - At properties()");

        return {
            accessToken: { type: String },
            initialLocation: { type: Object },
            lng: { type: Number },
            lat: { type: Number },
            zoomCenter: { type: Object },
            layer: { type: Object },
            source: { type: Object },
            data: { type: Object },
            layerId: { type: String },
            url: { type: String },
            iconName: { type: String },
            sourceId: { type: String },
            zoomLevel: { type: Number }
        }
    }

    updateConfig() {
        console.log("**** - At updateConfig()");

        // change this.debug to true
        if (true) {
            console.log(this.config);
        }
    }

    // ready() {
    //     super.ready();
    //
    //     console.log("at ready()");
    //
    //     mapboxgl.accessToken = this.accessToken;
    //
    //     console.log("initialLocation = " + this.initialLocation);
    //
    //     this.map = new mapboxgl.Map(
    //         {
    //             container: 'map', // container id" +
    //             style: 'mapbox://styles/mapbox/streets-v11', // stylesheet location" +
    //             center: JSON.parse(this.initialLocation), // starting position [lng, lat]" +
    //             zoom: this.zoomLevel // starting zoom" +
    //         });
    // }

    ready() {
        super.ready();

        console.log("at ready()");

        mapboxgl.accessToken = this.accessToken;

        console.log("initialLocation = " + this.initialLocation);
    }

    connectedCallback() {
        super.connectedCallback();

        // to avoid the map being initialized twice
        if(this.offsetWidth === 0) {
            return;
        }

        console.log('this.initialLocation');
        console.log(this.initialLocation);
        console.log('this.lng');
        console.log(this.lng);
        console.log('this.lat');
        console.log(this.lat);
        //console.log(JSON.parse(this.initialLocation));

        console.log('accessToken');
        console.log(this.accessToken);
        mapboxgl.accessToken = this.accessToken;

        const initialLocationLocal = JSON.stringify({ lng: this.lng, lat: this.lat});
        const initialLocationObject = JSON.parse(initialLocationLocal);
        // const initialLocationArray = [this.lng, this.lat];

        try {
            this.map = new mapboxgl.Map(
                {
                    container: 'lit-map', // container id" +
                    style: 'mapbox://styles/mapbox/streets-v11', // stylesheet location" +
                    center: initialLocationObject, // starting position [lng, lat]" +
                    zoom: 9 // starting zoom" +
                });
        }
        catch (e) {
            console.log('Something went wrong, so catching error');
            console.log(e);
        }
    }

    addFullScreenControl() {
        super.connectedCallback();

        // to avoid adding the control if the map is not yet available
        if(!this.map){
            console.log("this.map not yet available; cannot add fullScreenControl.");
            return;
        }

        this.map.addControl(new mapboxgl.FullscreenControl());
    }



    /**
     * This is due to the way the eval function works eval("function (){return \"test\"}") will throw an
     * Uncaught SyntaxError: Function statements require a function name.
     *
     * If the string is wrapped with brackets, as for example eval("(function (){return \"test\"})") the function
     * returns ƒ (){return "test"} which is what is needed.
     * @param string for example "function (){return \"test\"}"
     * @returns {function} returns an actual JavaScript instance of the passed string function
     */
    evalFunction(string) {
        return eval("(" + string + ")");
    }

    addLayer() {
        console.log("Adding layer: " + this.layer);
        this.map.addLayer(JSON.parse(this.layer));
    }

    addLayerArgumented(layer)
    {
        console.log("Adding layer: " + layer);
        this.map.addLayer(JSON.parse(layer));
    }

    addSource() {
        this.map.addSource(this.sourceId, JSON.parse(this.source));
    }

    resetData() {
        this.map.getSource(this.sourceId).setData(JSON.parse(this.data));
    }

    hideLayer() {
        this.map.setFilter(this.layerId, ['==', 'type', 'Feature']);
    }

    unhideLayer() {
        this.map.setFilter(this.layerId, null);
    }

    zoomTo()
    {
        this.map.zoomTo(zoomLevel, { "duration": 1500 } );
    }

    flyTo()
    {
        if (this.zoomLevel) {
            this.map.flyTo({center: JSON.parse(this.zoomCenter), zoom: this.zoomLevel, duration: 1500});
        }
        else
        {
            this.map.flyTo({center: JSON.parse(this.zoomCenter), duration: 1500});
        }
    }

    activatePointerLocation()
    {
        this.map.on('mousemove', function(e)
        {
            console.log("Mouse moved to: " + JSON.stringify(e.point) + " which equals " + JSON.stringify(e.lngLat.wrap()));
            // document.getElementsByClassName('info').innerHTML = "test";
            document.getElementById("infoBox").innerHTML =
                // e.point is the x, y coordinates of the mouse move event relative
                // to the top-left corner of the map
                JSON.stringify(e.point) +
                '<br />' +
                // e.lngLat is the longitude, latitude geographical position of the event
                JSON.stringify(e.lngLat.wrap());
        });
    }

    removeListeners()
    {
        this.map.off('mousemove', function(e)
        {
            console.log("Mouse moved to: " + JSON.stringify(e.point) + " which equals " + JSON.stringify(e.lngLat.wrap()));
            // document.getElementsByClassName('info').innerHTML = "test";
            document.getElementById("infoBox").innerHTML =
                // e.point is the x, y coordinates of the mouse move event relative
                // to the top-left corner of the map
                JSON.stringify(e.point) +
                '<br />' +
                // e.lngLat is the longitude, latitude geographical position of the event
                JSON.stringify(e.lngLat.wrap());
        });
    }

    updateData(){

        console.log("**** - At updateData()");

        if (this.layer)
        {
            console.log("**** - At updateData() in if(this.layer)");
        }

        // if (this.chartComponent) {
        //     this.chartComponent.updateSeries(JSON.parse(this.series));
        // }

        if (this.debug) {
            console.log(this.chartComponent);
        }
    }

    render() {
        console.log("**** - At render()");
    }

    loadIcon()
    {
        console.log("this.url: "+this.url);
        console.log("this.iconName: "+this.iconName);

        this.url = "http://localhost:8080/img/atom.png";
        this.iconName = "atom";

        console.log("this.url: "+this.url);
        console.log("this.iconName: "+this.iconName);

        this.map.loadImage(this.url,
            function(error, image) {
                if (error) throw error;
                this.map.addImage(this.iconName, image);
                this.map.addSource('point', {
                    'type': 'geojson',
                    'data': {
                        'type': 'FeatureCollection',
                        'features': [
                            {
                                'type': 'Feature',
                                'geometry': {
                                    'type': 'Point',
                                    'coordinates': [0, 0]
                                }
                            }
                        ]
                    }
                });
                this.map.addLayer({
                    'id': 'points',
                    'type': 'symbol',
                    'source': 'point',
                    'layout': {
                        'icon-image': this.iconName,
                        'icon-size': 0.25
                    }
                });
            });
    }

    fromOriginToDestination()
    {

        var airplane_route;
        var airplane;

        var lineDistance; // = turf.lineDistance(route.features[0], 'kilometers');

        var arc = [];

        // Number of steps to use in the arc and animation, more steps means
        // a smoother arc and animation, but too many steps will result in a
        // low frame rate
        var steps = 500;

        // Used to increment the value of the point measurement against the route.
        var counter = 0;

        // prepare route and point objects
        // and the arc for the flight
        setOriginDestination(this.origin, this.destination);

        map.addSource('airplane_route',
            {
                "type": "geojson",
                "data": airplane_route
            });

        map.addSource('airplane',
            {
                "type": "geojson",
                "data": airplane
            });

        map.addLayer({
            "id": "airplane_route",
            "source": "airplane_route",
            "type": "line",
            "paint": {
                "line-width": 2,
                "line-color": "#007cbf"
            }
        });

        map.addLayer({
            "id": "airplane",
            "source": "airplane",
            "type": "symbol",
            "layout": {
                "icon-image": "airport-15",
                "icon-rotate": ["get", "bearing"],
                "icon-rotation-alignment": "map",
                "icon-allow-overlap": true,
                "icon-ignore-placement": true
            }
        });

        function animate()
        {
            // console.log(point);
            // console.log(airplane_route);

            // Update point geometry to a new position based on counter denoting
            // the index to access the arc.
            airplane.features[0].geometry.coordinates = airplane_route.features[0].geometry.coordinates[counter];

            // Calculate the bearing to ensure the icon is rotated to match the airplane_route arc
            // The bearing is calculate between the current point and the next point, except
            // at the end of the arc use the previous point and the current point

            airplane.features[0].properties.bearing = turf.bearing(
                turf.point(airplane_route.features[0].geometry.coordinates[counter >= steps ? counter - 1 : counter]),
                turf.point(airplane_route.features[0].geometry.coordinates[counter >= steps ? counter : counter + 1])
            );

            // Update the source with this new data.
            map.getSource('airplane').setData(airplane);

            // Request the next frame of animation so long the end has not been reached.
            if (counter < steps)
            {
                requestAnimationFrame(animate);
            }

            counter = counter + 1;
        }

        document.getElementById('replay').addEventListener('click', function()
        {
            // Set the coordinates of the original point back to origin
            airplane.features[0].geometry.coordinates = origin;

            // Update the source layer
            map.getSource('airplane').setData(airplane);

            // Reset the counter
            counter = 0;

            // Restart the animation.
            animate(counter);
        });

        // Start the animation.
        animate(counter);
    }

    setOriginDestination(origin, destination)
    {
        // A simple line from origin to destination
        airplane_route = {
            type: "FeatureCollection",
            features: [{
                type: "Feature",
                geometry: {
                    type: "LineString",
                    coordinates: [
                        origin,
                        destination
                    ]
                }
            }]
        };

// A single point that animates along the route.
// Coordinates initially set to origin.
        airplane = {
            "type": "FeatureCollection",
            "features": [{
                "type": "Feature",
                "properties": {},
                "geometry": {
                    "type": "Point",
                    "coordinates": origin
                }
            }]
        };

        lineDistance = turf.lineDistance(airplane_route.features[0], 'kilometers');

        // Draw an arc between the `origin` & `destination` of the two points
        for (var i = 0; i < lineDistance; i += lineDistance / steps)
        {
            var segment = turf.along(airplane_route.features[0], i, "kilometers");
            arc.push(segment.geometry.coordinates);
        }

        // Update the route with calculated arc coordinates
        airplane_route.features[0].geometry.coordinates = arc;
    }

}

customElements.define(MapboxWrapperLit.is, MapboxWrapperLit);
export {MapboxWrapperLit};
