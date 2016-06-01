import glob
import os
import re
from collections import deque
import dateparser # $ pip import dateparser

headerReg = r"[0-9]+\. (.+) de (.+) à (.+)"
class Text:
    def __init__(self, id, header, content):
        self.id = id
        contentLines = content.split('\n')
        metaData = contentLines[0]
        date = re.match(dateReg, metaData)
        self.text = '\n'.join(contentLines[1:])
        self.day = date.group(1)
        self.month = date.group(2)
        self.year = date.group(3)
        self.header = header
        self.id = id
        self.fullDate = dateparser.parse(" ".join([self.day, self.month, self.year])).date()
        headerInfos = re.match(headerReg, header)
        self.tpe = headerInfos.group(1)
        self.fro = headerInfos.group(2)
        self.to = headerInfos.group(3)
        self.wl = [re.sub(r"[\,\.0-9:«»/\\;\)\(\?\-]", "", x) for x in self.text.split()]
        self.wl = [w.split('\'')[-1] for w in self.wl]
        wlPos = [a for a in zip(self.wl, range(len(self.wl))) if a[0].lower() not in wordList and a[0] != a[0].lower()]
        maxDist = max([a[1] for a in wlPos])
        graph = {}
        for w1, p1 in wlPos:
            for w2, p2 in wlPos:
                if w1 != w2:
                    p = maxDist - abs(p1 - p2)
                    if (w2, w1) in graph:
                        graph[(w2, w1)] += p
                    elif (w1, w2) in graph:
                        graph[(w1, w2)] += p
                    else:
                        graph[(w1, w2)] = p
        print(graph)
                        

def uniformizeDashes(text):
    dashesForm = ['–', '—', '——']
    for d in dashesForm:
        text = text.replace(d, '-')
    return text

def changeApostrophee(text):
    return text.replace('’', '\'')

def removeStrangeﬁ(text):
    return text.replace('ﬁ', 'fi')

def removeAnnotations(text):
    (text, n) = re.subn(r"(\s[a-zA-z]+)[0-9]+", r'\1', text)
    return text

def uncutWords(text):
    (text, n) = re.subn(r"([a-zA-z\-\']+)\-\n([a-zA-z\-\']+)", '\1\2\n', text)
    return text


sourceDir = os.path.abspath('../txt')
dicodir = os.path.abspath('../dico/fr')

print("loading dico")
wordList = set([x.strip() for x in open(dicodir + "/fr.dic", "r", encoding="utf-16")])
print("dico loaded")
print(len(wordList))

filenames = glob.glob(sourceDir + "/*.txt")
filenames.sort(key=lambda x:int(os.path.basename(x).split('.')[0]))
data =[''.join([line for line in open(fname, 'r')]) for fname in filenames]
print("We have %s files to process" % len(data))
print("Task 1 : Uniformization of dashes")
data = [uniformizeDashes(content) for content in data]
print("Task 2 : Using standard apostrophee")
data = [changeApostrophee(content) for content in data]
print("Task 3 : Remove strange fi")
data = [removeStrangeﬁ(content) for content in data]
print("Task 4 : Deleting annotation number")
data = [removeAnnotations(content) for content in data]
print("Task 5 : joining cut words")
data = [uncutWords(content) for content in data]

print("Separating Letters")

fullText = "\n\n\n".join(data)
titleReg = r"(\d+\. \w+(?: \w+)? de \w+(?: \w+)+ à \w+(?: \w+))\n+"
dateReg = r"[^0-9]*(\d+) (\w+) (\d+)"
lettersSeparated = deque(re.split(titleReg, fullText))
lettersSeparated.popleft()
i = 0
while len(lettersSeparated) > 1:
    i += 1
    header = lettersSeparated.popleft()
    content = lettersSeparated.popleft()
    Text(id, header, content)
print("Number of letters found %s" % i)

