import json
from collections import Counter
positions = json.loads($(cat position_from_google))
matches = $(cat locmatches).split("\n")[:-1]
matches = [x.split(":") for x in matches]
matches = [(letter, place) for [letter, _, place] in matches]
matches = [(letter.replace("./", "").replace(".txt", ""), place.lower().strip()) for letter, place in matches]
letterPos = [(letter, positions[place]) for (letter, place) in matches]
print(len(letterPos))
letters = {k: Counter() for k in [letter for (letter,_) in matches]}
for letter, pos in matches:
    letters[letter].update([pos])

letters = {k: list(counts.items()) for (k, counts) in letters.items()}
print(letters)
