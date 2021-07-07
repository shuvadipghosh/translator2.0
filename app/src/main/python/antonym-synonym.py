import nltk
from nltk.corpus import wordnet
while not nltk.download('wordnet'):
    print("Retrying download")
synonyms = []
syno=""
anto=""
antonyms = []
def an_sy(n,types):
    print("Searching our database...............................")
    synonyms = []
    syno=""
    anto=""
    antonyms = []
    for syn in wordnet.synsets(n):
        for l in syn.lemmas():
            synonyms.append(l.name())
            if l.antonyms():
                antonyms.append(l.antonyms()[0].name())

    if len(set(synonyms)) != 0:
        print("Synonyms of {} are : ".format(n))
        c = 0
        for s in set(synonyms):
            c = c + 1
            print('{}. {}'.format(c, s))
            if c!=1:
                syno= syno+" , "+s
            else:
                s=s.replace('_',' ')
                syno=syno+""+s

        if types=='Synonyms':
            print(syno)
            return syno

    else:
        print("No Synonyms found")
        return "No Synonyms found"
    print("Antonyms of {} are : ".format(n))
    if len(set(antonyms)) != 0:
        c = 0
        for an in set(antonyms):
            c = c + 1
            if types=="Antonyms":
                print('{}. {}'.format(c, an))
                if c!=1:
                    anto= anto+" , "+an
                else:
                    anto=anto+""+an
        if types=='Antonyms':
            print(anto)
            return anto

    else:
        print("No Antonyms found")
        return "No Antonyms found"
