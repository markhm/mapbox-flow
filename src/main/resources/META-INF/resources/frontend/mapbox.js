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

function activatePointerLocation()
{
    map.on('mousemove', function(e)
    {
        document.getElementById('info').innerHTML =
    // e.point is the x, y coordinates of the mousemove event relative
    // to the top-left corner of the map
            JSON.stringify(e.point) +
            '<br />' +
            // e.lngLat is the longitude, latitude geographical position of the event
            JSON.stringify(e.lngLat.wrap());
    });
}

function addPolygon(featureCollection)
{
    var polyLayer = {
        'id': 'maine',
        'type': 'fill',
        'source': {
            'type': 'geojson',
            'data': {
                featureCollection
            }
        },
        'layout': {},
        'paint': {
            'fill-color': '#088',
            'fill-opacity': 0.8
        }
    };

    console.log(polyLayer);

    map.addLayer(polyLayer);
}


function addLine(geometry)
{
    var layer = {
        'id': 'routeLine',
        'type': 'line',
        'source': {
            'type': 'geojson',
            'data': {
                'type': 'Feature',
                'properties': {},
                'geometry': geometry
            }
        },
        'layout': {
            'line-join': 'round',
            'line-cap': 'round'
        },
        'paint': {
            'line-color': '#d54648',
            'line-width': 3
        }
    };

    console.log(layer);

    map.addLayer(layer);

    // map.flyTo({center: [-122.486052, 37.830348], zoom: 15});

//     'coordinates': [
//     [-122.48369693756104, 37.83381888486939],
//     [-122.48348236083984, 37.83317489144141],
//     [-122.48339653015138, 37.83270036637107],
//     [-122.48356819152832, 37.832056363179625],
//     [-122.48404026031496, 37.83114119107971],
//     [-122.48404026031496, 37.83049717427869],
//     [-122.48348236083984, 37.829920943955045],
//     [-122.48356819152832, 37.82954808664175],
//     [-122.48507022857666, 37.82944639795659],
//     [-122.48610019683838, 37.82880236636284],
//     [-122.48695850372314, 37.82931081282506],
//     [-122.48700141906738, 37.83080223556934],
//     [-122.48751640319824, 37.83168351665737],
//     [-122.48803138732912, 37.832158048267786],
//     [-122.48888969421387, 37.83297152392784],
//     [-122.48987674713133, 37.83263257682617],
//     [-122.49043464660643, 37.832937629287755],
//     [-122.49125003814696, 37.832429207817725],
//     [-122.49163627624512, 37.832564787218985],
//     [-122.49223709106445, 37.83337825839438],
//     [-122.49378204345702, 37.83368330777276]
// ]
}


function deactivateAllListeners()
{
    map.removeAllListeners();
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
    map.addSource('animation', {
        "type": "geojson",
        "data": pointOnCircle(0)
    });

    map.addLayer(
        {
            "id": "animation",
            "source": "animation",
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
        map.getSource('animation').setData(pointOnCircle(timestamp / 1000));

        // Request the next frame of the animation.
        requestAnimationFrame(animateMarker);
    }

    // Start the animation.
    animateMarker(0);
}

// --*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--
// --*  https://docs.mapbox.com/mapbox-gl-js/example/geojson-markers/                    *--
// --*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--

function addLayer(layer)
{
    console.log("Adding layer "+layer);
    map.addLayer(layer);
}

function removeLayer(id)
{
    // Note that the id cannot be reused, so removeLayer does not seem useful
    if (map.getLayer(id)) map.removeLayer(id);
}

function hideLayer(id)
{
    map.setFilter(id, ['==', 'type', 'Feature']);
}

function unhideLayer(id)
{
    map.setFilter(id, null);
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
    // A simple line from origin to destination
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
// Coordinates initially set to origin.
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
        "id": "originDestinationRoute",
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
        // console.log(point);
        // console.log(route);

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


var polyLayerOrig = {
    'id': 'maine',
    'type': 'fill',
    'source': {
        'type': 'geojson',
        'data': {
            'type': 'Feature',
            'geometry': {
                'type': 'Polygon',
                'coordinates': [
                    [
                        [-67.13734351262877, 45.137451890638886],
                        [-66.96466, 44.8097],
                        [-68.03252, 44.3252],
                        [-69.06, 43.98],
                        [-70.11617, 43.68405],
                        [-70.64573401557249, 43.090083319667144],
                        [-70.75102474636725, 43.08003225358635],
                        [-70.79761105007827, 43.21973948828747],
                        [-70.98176001655037, 43.36789581966826],
                        [-70.94416541205806, 43.46633942318431],
                        [-71.08482, 45.3052400000002],
                        [-70.6600225491012, 45.46022288673396],
                        [-70.30495378282376, 45.914794623389355],
                        [-70.00014034695016, 46.69317088478567],
                        [-69.23708614772835, 47.44777598732787],
                        [-68.90478084987546, 47.184794623394396],
                        [-68.23430497910454, 47.35462921812177],
                        [-67.79035274928509, 47.066248887716995],
                        [-67.79141211614706, 45.702585354182816],
                        [-67.13734351262877, 45.137451890638886]
                    ]
                ]
            }
        }
    },
    'layout': {},
    'paint': {
        'fill-color': '#088',
        'fill-opacity': 0.8
    }
};

