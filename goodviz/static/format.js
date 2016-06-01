const fs = require('fs')

var data = fs.readFileSync('data.csv', 'utf-8');

data = data.replace(/;/g, ",");

fs.writeFileSync('data.csv', data, 'utf-8');
