import React from 'react';
import ReactDOM from 'react-dom';
import App from './App';
import _ from 'lodash'

import Viva from 'vivagraphjs'
import data from 'json!../../txt/tagged/edges.json'
import atlas from 'ngraph.forceatlas2'
var graph = Viva.Graph.graph();
var container = document.body;

var max = 15;
for (let key in data) {
  const node = data[key];
  graph.addNode(key);
}
for (let key in data) {
  const node = data[key];
  _.each(node, (n, index) => {
    _.each(n, (dist) => {
      if (dist < max)
      graph.addLink(key, index, {dist});
    });
  });
}

var domLabels = generateDOMLabels(graph);

function generateDOMLabels(graph) {
  // this will map node id into DOM element
  var labels = Object.create(null);
  graph.forEachNode(function(node) {
    var label = document.createElement('span');
    label.classList.add('node-label');
    label.innerText = node.id;
    labels[node.id] = label;
    container.appendChild(label);
  });
  // NOTE: If your graph changes over time you will need to
  // monitor graph changes and update DOM elements accordingly
  return labels;
}
var idealLength = 0;
/*var layout = Viva.Graph.Layout.forceDirected(graph, {
  springLength: 0,
    springCoeff : 0.001,
    dragCoeff: 0.1,
    gravity : -1000,
    timeStep: 10,
    // This is the main part of this example. We are telling force directed
    // layout, that we want to change length of each physical spring
    // by overriding `springTransform` method:
    springTransform: function (link, spring) {
      spring.length = idealLength;
      spring.weight = _.clamp(max - link.data.dist, 0, max)/max;
      spring.weight = Math.pow(spring.weight,1 );
      console.log(spring.weight);
    }
});*/

var layout = atlas(graph, {
    gravity: 1,
    linLogMode: true,
    strongGravityMode: false,
    slowDown: 1,
    outboundAttractionDistribution: false,
    iterationsPerRender: 1,
    barnesHutOptimize: false,
    barnesHutTheta: 0.5,
    worker: true
});

// Construct the graph

// Set custom nodes appearance
var graphics = Viva.Graph.View.webglGraphics();

graphics.placeNode(function(ui, pos) {
  // This callback is called by the renderer before it updates
  // node coordinate. We can use it to update corresponding DOM
  // label position;
  // we create a copy of layout position
  var domPos = {
    x: pos.x,
  y: pos.y
  };
  // And ask graphics to transform it to DOM coordinates:
  graphics.transformGraphToClientCoordinates(domPos);
  // then move corresponding dom label to its own position:
  var nodeId = ui.node.id;
  var labelStyle = domLabels[nodeId].style;
  labelStyle.left = domPos.x + 'px';
  labelStyle.top = domPos.y + 'px';
});

graphics.node((node) => {
  return Viva.Graph.View.webglSquare(1 + node.links.length/5);
});

var renderer = Viva.Graph.View.renderer(graph,
    {
      graphics, layout
    });
renderer.run();
