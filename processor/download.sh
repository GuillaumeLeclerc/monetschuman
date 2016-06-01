#!/bin/sh
mkdir lib
wget -nc "http://nlp.stanford.edu/software/stanford-corenlp-full-2015-12-09.zip"
wget -nc "http://nlp.stanford.edu/software/stanford-french-corenlp-2016-01-14-models.jar"
unzip -o "stanford-corenlp-full-2015-12-09.zip"
cd lib
rm -f *.jar
ln -s "../stanford-french-corenlp-2016-01-14-models.jar" "./stanford-french-corenlp-2016-01-14-models.jar"
ln -s ../stanford-corenlp-full-2015-12-09/*.jar ./
