import json

import requests


def dict(language,word):
    try:
        print("Searching our database...............................")
        response = requests.get('https://api.dictionaryapi.dev/api/v2/entries/' + language + '/' + word)
        s = json.loads(response.text)  # Get the request in json format
    # To extract the exact meaning of the word from the json
        s = "Meaning of  {} is  ' {} '".format(word, s[0]['meanings'][0]['definitions'][0]['definition'])
        print(s)
        return str(s)
    except Exception as e:
        print("Exception found . Please retry again")
        return ""
