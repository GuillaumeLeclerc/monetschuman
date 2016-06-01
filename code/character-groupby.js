const fs = require('fs');

const characters = JSON.parse(fs.readFileSync('characters.json'));

var characterOccurences = {}

for (var character in characters) {
    console.log(character);
    characters[character].occurences.forEach((letterNumber) => {
        console.log(letterNumber);
        if (characterOccurences[letterNumber] === undefined) {
            characterOccurences[letterNumber] = [];
        }
        characterOccurences[letterNumber].push(character);
    });
}

console.log(characterOccurences);

var metadataObjects = JSON.parse(fs.readFileSync('metadata.json'));

var characterIndex = 1;

for (var letterKey in metadataObjects) {
    var characterList = characterOccurences[characterIndex];
    if (characterList !== undefined) {
        metadataObjects[letterKey].characters = characterList;
    }
    characterIndex++;
}

fs.writeFileSync("metadata-v2.txt", JSON.stringify(metadataObjects));
