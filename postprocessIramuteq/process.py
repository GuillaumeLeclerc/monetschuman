from json import dumps
from datetime import date
from time import mktime
from collections import Counter
import re

f = open("./ptable.csv")
extractDate = re.compile('.*date_(\\d+)-(\\d+)-(\\d+).*')
extractClass = re.compile(".*\*classe_(\\d+)")
f = open("./export_corpus_classe.txt", "r", encoding="utf-8")
counts = dict()
def groupBy(f):
    try:
        while True:
            infos = next(f)
            words = next(f).strip().split()
            next(f)
            match = extractDate.match(infos)
            classMatch = extractClass.match(infos)

            if match != None:
                y, m, d = [int(x) for x in match.groups()]
                day = mktime(date(y, m, d).timetuple())
                yield day, int(classMatch.groups()[0])
    except StopIteration:
        f.close()
maxClass = 0
for day, cls in sorted(list(groupBy(f))):
    maxClass = max([maxClass, cls])
    if day not in counts:
        counts[day] = Counter()
    counts[day].update([cls])

items = dict()

for time, counter in counts.items():
    s = sum(counter.values())
    weights = [counter[x]/s for x in range(1, maxClass + 1)]
    items[time] = weights
items = sorted(items.items())
f.close()

with open('./output.csv', "w+") as f:
    f.write('time,' + ','.join(['class' + str(x) for x in range(1, maxClass + 1)]) + "\n")
    for time, clses in items:
        f.write(str(time) + ',' + ','.join([str(x) for x in clses]) + "\n")
