<template>
  <div class="hello">
    <div class="container barContainer">
      <div v-for="line in heights.slice(1)" track-by="$index" class="bar">
        <div class="barInner" :style="{height: (line*100) + '%', backgroundColor: generateColor($index)}"></div>
      </div>
    </div>
    <div class="hud">
      <button @click="play = !play">
        {{play?'Stop':'Play'}}
      </button>
      <input type="range" :min="min" :step="3600*24" v-model="selected" :max="max">
      <div class="currentDate">
        {{currentDate}}
      </div>
    </div>
  </div>
</template>

<script>

import _ from 'lodash';
import Chance from 'chance';
import dateformat from 'dateformat';
const chance = new Chance();

const data = _.map(require('../../static/data.csv'),
  (x) => _.values(_.mapValues(x, (y) => parseFloat(y, 10)))
);

const colors = {};

const duration = 15;

export default {
  created() {
    setInterval(() => {
      if (this.play) {
        this.selected += 3600 * 24;
      }
      if (this.selected > this.max) {
        this.selected = this.max;
        this.play = false;
      }
    }, duration * 1000 / (3600 * 24) / (this.max - this.min));
  },
  data() {
    const timestamps = _.sortBy(_.map(data, (x) => x[0]));
    const result = {
      timestamps,
      min: _.minBy(timestamps, parseInt),
      max: _.maxBy(timestamps, parseInt),
      data,
      play: false,
      count: Object.keys(data).length,
      selected: _.min(timestamps),
    };
    return result;
  },
  methods: {
    generateColor(i) {
      if (colors[i] === undefined) colors[i] = chance.color();
      return colors[i];
    },
  },
  computed: {
    currentDate() {
      return dateformat(new Date(this.selected * 1000), 'dd/mm/yyyy');
    },
    heights() {
      const selected = this.selected;
      const letter = parseInt(_.findIndex(this.timestamps, x => x >= selected), 10);

      let result = null;
      if (letter === 0) {
        result = this.data[0];
      } else {
        const i1 = this.timestamps[letter - 1];
        const i2 = this.timestamps[letter];
        const progress = (selected - i1) / (i2 - i1);
        const zippedData = _.zip(this.data[letter - 1], this.data[letter]);
        result = _.map(zippedData, ([a, b]) =>
          a * (1 - progress) + b * progress
        );
      }
      return result;
    },
  },
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h1 {
  color: #42b983;
}

.container {
  width: 100%;
  height: 500px;
}

.hello {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
}

.barContainer {
  display: flex;
  align-items: stretch;
  flex-grow: 1;
  width: 100%;
  height: 100%;

}

.barInner {
  opacity:0.7;
  position:absolute;
  bottom: 0;
  width:100%;
  background-color: red;
}

.bar {
  flex-grow: 1;
  position: relative;
  margin: 1%;
}

.barInner:hover {
  opacity:1;
}

.hud {
  display: flex;
  flex-direction: row;
}

input {
  width: 100%;
  padding: 0;
}
</style>
