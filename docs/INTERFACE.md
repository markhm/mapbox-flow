# Interface for Mapbox-Flow

### Layer

#### Collection of points
    // layer
    //   layout (icon-image, text-field, text-font, text-offset, text-anchor)
    //   source (type=geojson)
    //     data (type=FeatureCollection)
    //       features
    //         feature (type=Feature)
    //           properties
    //             geometry (type=Point)
    //               coordinates
    //                 [[], []]

#### Line

    // layer (id, type=line, layout, paint)
    //   source (type=geojson)
    //     data (type = feature, properties)
    //       // geometry



![Screenshot](docs/img/mapbox-flow-screenshot.png)
