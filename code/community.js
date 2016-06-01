const fs = require('fs');
const _ = require('lodash');

// PARAMETERS
const CLUSTER_WEIGHT = 1;
const DOCUMENT_WEIGHT = 2;

const CHOSEN_METADATA = "locations";

const OUTPUT_PATH = "output.csv";
const METADATA_PATH = "../txt/letters/metadata.json";

// clear output file
fs.writeFileSync(OUTPUT_PATH, '');

const metadata = JSON.parse(fs.readFileSync(METADATA_PATH));

const documentKeys = Object.keys(metadata);

const findCharactersOfCluster = letterNumber => {

    let letterCharacters = metadata["letter-" + letterNumber][CHOSEN_METADATA];
    if (letterCharacters === undefined) {
        letterCharacters = [];
    }

    const appendixRegex = new RegExp("appendix-" + letterNumber + "-\\d");

    let appendixKeys = _.filter(documentKeys, x => appendixRegex.test(x));
    if (appendixKeys === undefined) {
        appendixKeys = [];
    }

    const appendixCharacters = _.flatMap(appendixKeys, x => metadata[x][CHOSEN_METADATA]);

    const characterCluster = _.filter(letterCharacters.concat(appendixCharacters),
                                      x => ! _.isUndefined(x));

    return _.uniq(characterCluster);
};

const writeHeader = () => {
    fs.writeFileSync(OUTPUT_PATH, "Source,Target,\n");
};

const cleanCharacterName = (characterString) => {
    return characterString.replace(/\s/g, '_').replace(/,/g, '')
        .replace(/__/g, '_');
}

const characterLinks = (characters, weight) => {
    var result = [];
    for (var i = 0; i < characters.length; i++) {
        for (var j = i + 1; j < characters.length; j++) {
            for (var k = 0; k < weight; k++) {
                const person1 = cleanCharacterName(characters[i]);
                const person2 = cleanCharacterName(characters[j]);
                result.push(person1 + ',' + person2);
            }
        }
    }
    return result;
};

const documentCharacters = () => {
    const characters = _.map(documentKeys, x => metadata[x][CHOSEN_METADATA]);
    return _.filter(characters, x => ! _.isUndefined(x));
};

const writeClusters = () => {
    for (var i = 1; i < 69; i++) {
        const letterNumber = i > 9? i.toString() : '0' + i.toString();
        const characterClusters = findCharactersOfCluster(letterNumber);
        writeEdges(characterClusters, CLUSTER_WEIGHT);
    }
};

const writeDocuments = () => {
    documentCharacters().forEach(characters => {
        writeEdges(characters, DOCUMENT_WEIGHT);
    });
};

const writeEdges = (characters, weight) => {
    const charLinks = characterLinks(characters, weight);
    if (charLinks.length > 0) {
        const linkString = charLinks.join('\n');
        console.log(linkString);
        fs.appendFileSync(OUTPUT_PATH, linkString);
        fs.appendFileSync(OUTPUT_PATH, '\n');
    }
};


writeHeader();
writeDocuments();
writeClusters();
//console.log(characterLinks(findCharactersOfCluster("49"), 1));
//console.log(documentCharacters());
