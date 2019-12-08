var map;
var radius = 20;

function renderMapbox(center, initialZoom)
{
    map = new mapboxgl.Map(
    {
        container: 'map', // container id" +
        style: 'mapbox://styles/mapbox/streets-v11', // stylesheet location" +
        center: center, // starting position [lng, lat]" +
        zoom: initialZoom // starting zoom" +
    });
}

function zoomTo(level)
{
    map.zoomTo(level,
        { "duration": 1500 }
    );
}

function pointOnCircle(angle)
{
    return {
        "type": "Point",
        "coordinates": [
            Math.cos(angle) * radius,
            Math.sin(angle) * radius
        ]
    };
}

// --*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--
// --*  Example: https://docs.mapbox.com/mapbox-gl-js/example/animate-point-along-line/  *--
// --*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--

function startAnimation()
{
// Add a source and layer displaying a point which will be animated in a circle.
    map.addSource('point', {
        "type": "geojson",
        "data": pointOnCircle(0)
    });

    map.addLayer(
        {
            "id": "point",
            "source": "point",
            "type": "circle",
            "paint": {
                "circle-radius": 10,
                "circle-color": "#007cbf"
            }
        });

    function animateMarker(timestamp)
    {
        // Update the data to a new position based on the animation timestamp. The
        // divisor in the expression `timestamp / 1000` controls the animation speed.
        map.getSource('point').setData(pointOnCircle(timestamp / 1000));

        // Request the next frame of the animation.
        requestAnimationFrame(animateMarker);
    }

    // Start the animation.
    animateMarker(0);
}

// --*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--
// --*  https://docs.mapbox.com/mapbox-gl-js/example/geojson-markers/                    *--
// --*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--

function addLayer()
{
    map.addLayer({
        "id": "points",
        "type": "symbol",
        "source": {
            "type": "geojson",
            "data": {
                "type": "FeatureCollection",
                "features": [{
// feature for Mapbox DC
                    "type": "Feature",
                    "geometry": {
                        "type": "Point",
                        "coordinates": [-77.03238901390978, 38.913188059745586]
                    },
                    "properties": {
                        "title": "Mapbox DC",
                        "icon": "monument"
                    }
                }, {
// feature for Mapbox SF
                    "type": "Feature",
                    "geometry": {
                        "type": "Point",
                        "coordinates": [-122.414, 37.776]
                    },
                    "properties": {
                        "title": "Mapbox SF",
                        "icon": "harbor"
                    }
                }]
            }
        },
        "layout": {
// get the icon name from the source's "icon" property
// concatenate the name to get an icon from the style's sprite sheet
            "icon-image": ["concat", ["get", "icon"], "-15"],
// get the title name from the source's "title" property
            "text-field": ["get", "title"],
            "text-font": ["Open Sans Semibold", "Arial Unicode MS Bold"],
            "text-offset": [0, 0.6],
            "text-anchor": "top"
        }
    });
}

// --*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--
// --*  Example: https://docs.mapbox.com/mapbox-gl-js/example/animate-point-along-route/ *--
// --*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--

// A single point that animates along the route.
// Coordinates are initially set to origin.

var route;
var point;

var lineDistance; // = turf.lineDistance(route.features[0], 'kilometers');

var arc = [];

// Number of steps to use in the arc and animation, more steps means
// a smoother arc and animation, but too many steps will result in a
// low frame rate
var steps = 500;

// Used to increment the value of the point measurement against the route.
var counter = 0;

function setOriginDestination(origin, destination)
{
    // A simple line from oorsprong to bestemming.
    route = {
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
// Coordinates are initially set to oorsprong.
    point = {
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

    lineDistance = turf.lineDistance(route.features[0], 'kilometers');

    // Draw an arc between the `origin` & `destination` of the two points
    for (var i = 0; i < lineDistance; i += lineDistance / steps)
    {
        var segment = turf.along(route.features[0], i, "kilometers");
        arc.push(segment.geometry.coordinates);
    }

    // Update the route with calculated arc coordinates
    route.features[0].geometry.coordinates = arc;
}

function fromOriginToDestination(origin, destination)
{
    // prepare route and point objects
    // and the arc for the flight
    setOriginDestination(origin, destination);

    map.addSource('route',
        {
            "type": "geojson",
            "data": route
        });

    map.addSource('point',
        {
            "type": "geojson",
            "data": point
        });

    map.addLayer({
        "id": "route",
        "source": "route",
        "type": "line",
        "paint": {
            "line-width": 2,
            "line-color": "#007cbf"
        }
    });

    map.addLayer({
        "id": "point",
        "source": "point",
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
        console.log(point);
        console.log(route);

        // Update point geometry to a new position based on counter denoting
        // the index to access the arc.
        point.features[0].geometry.coordinates = route.features[0].geometry.coordinates[counter];

        // Calculate the bearing to ensure the icon is rotated to match the route arc
        // The bearing is calculate between the current point and the next point, except
        // at the end of the arc use the previous point and the current point

        point.features[0].properties.bearing = turf.bearing(
            turf.point(route.features[0].geometry.coordinates[counter >= steps ? counter - 1 : counter]),
            turf.point(route.features[0].geometry.coordinates[counter >= steps ? counter : counter + 1])
        );

        // Update the source with this new data.
        map.getSource('point').setData(point);

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
        point.features[0].geometry.coordinates = origin;

        // Update the source layer
        map.getSource('point').setData(point);

        // Reset the counter
        counter = 0;

        // Restart the animation.
        animate(counter);
    });

    // Start the animation.
    animate(counter);
}


