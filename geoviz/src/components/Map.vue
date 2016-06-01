<template>
  <div class="map" v-el:map>
  </div>
  <div class="ui">
    <input class="range" :min="0" :max type="range" v-model="selected">
  </div>
</template>

<script>
import positions from "../../static/positions.json"
import metadata from "../../static/metadata.json"


function parseDate(input) {
    var parts = input.split('-');
    return new Date(parts[0], parts[1]-1, parts[2]); // Note: months are 0-based
}

const letters = _.sortBy(_.toArray(metadata), (x) => parseDate(x.date));

import _ from "lodash"

export default {
  data() {
    return {
      heatL: null,
      selected: 0,
    };
  },
  methods: {
    generatePoints() {
       const points = _.flatten(_.map(letters[this.selected].locations, (locName) => {
          const coords = positions[locName];
          return _.times(100, () => [coords.lat, coords.lng , 1])
       }));
       this.heatL.setLatLngs(points);
    }
  },
  ready() {
    var map = L.map(this.$els.map).setView([46, 0], 3);
    var tiles = L.tileLayer('http://{s}.tile.osm.org/{z}/{x}/{y}.png', {
        attribution: '&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors',
    }).addTo(map);
    this.heatL = L.heatLayer([] , {minOpacity: 0.4,max:1, radius: 25});
    this.heatL.addTo(map)
    this.generatePoints()
  },
  watch: {
    'selected': function () {
      this.generatePoints();
    }
  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
div.map {
  width: 100%;
  height: 100%;
  margin: 0;
  padding: 0;
  position:relative;
  z-index: 0;
}

.ui {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  background: red;
  z-index: 199;
}

input[type=range] {
  width: 100%;
}
</style>
