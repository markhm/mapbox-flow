import {PolymerElement} from '@polymer/polymer/polymer-element.js';
import {html} from '@polymer/polymer/lib/utils/html-tag.js';
import '@polymer/polymer/lib/utils/html-tag.js';

// Based on https://github.com/appreciated/apexcharts-flow/blob/master/src/main/resources/META-INF/resources/frontend/com/github/appreciated/apexcharts/apexcharts-wrapper.js

class MapboxWrapper extends PolymerElement {

    static get template() {
        return html`
        <slot></slot>
    `;
    }

    // ::slotted(map)

    static get is() {
        console.log("**** - At is()");
        return 'mapbox-wrapper';
    }

    static get properties() {

        console.log("**** - At properties()");

        return {
            accessToken: {
                type: String
            },
            initialLocation: {
                type: Object
            },
            layer: {
                type: Object
            },
            layerId:{
                type: String
            }
        }
    }

    updateConfig() {

        console.log("**** - At updateConfig()");

        // change this.debug to true
        if (true) {
            console.log(this.config);
        }
    }

    ready() {
        super.ready();

        console.log("at ready()");

        // this.color = require('onecolor');
        // const div = document.createElement('div');
        // this.appendChild(div);
        // this.updateConfig();

        mapboxgl.accessToken = this.accessToken;

        console.log("initialLocation = " + this.initialLocation);

        this.map = new mapboxgl.Map(
            {
                container: 'map', // container id" +
                style: 'mapbox://styles/mapbox/streets-v11', // stylesheet location" +
                center: JSON.parse(this.initialLocation), // starting position [lng, lat]" +
                zoom: 3 // starting zoom" +
            });
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
        this.map.addLayer(JSON.parse(this.layer));
    }

    addSource()
    {
        this.map.addSource(JSON.parse(this.source));
    }

    setData()
    {
        map.getSource(this.sourceId).setData(this.data);
    }


    hideLayer() {
        this.map.setFilter(this.layerId, ['==', 'type', 'Feature']);
    }

    unhideLayer() {
        this.map.setFilter(this.layerId, null);
    }

    setData() {
        this.map.getSource(this.sourceId).setData(this.data);
    }

    zoomTo()
    {
        this.map.zoomTo(zoomLevel, { "duration": 1500 } );
    }

    removeListeners()
    {
        this.map.removeAllListeners();
    }

    updateData() {

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
}

customElements.define(MapboxWrapper.is, MapboxWrapper);
export {MapboxWrapper};
