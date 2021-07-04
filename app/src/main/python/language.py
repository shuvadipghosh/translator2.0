from ibm_cloud_sdk_core.authenticators import IAMAuthenticator
from ibm_watson import LanguageTranslatorV3
authenticator = IAMAuthenticator('1welCJfVny9OAzDh6mGXzx3HJ1J113jztxTsLqQyc5QH')
language_translator = LanguageTranslatorV3(
    version='2021-05-05',
    authenticator=authenticator
)
languages = 0
lang_code = []
lang_name = []
lang_code_name = {}
lang_name_code = {}

def loadLanguages():
    # Initialise the service url for Watson API to use
    language_translator.set_service_url('https://api.jp-tok.language-translator.watson.cloud.ibm.com/instances/53be2550'
                                        '-1b04-42a4-87ea-4fc584ccefef')

    # Get the result as to which languages are supported by the IBM Watson
    languages = language_translator.list_identifiable_languages().get_result()
    for i in range(0, 76):
        lang_code.append(languages['languages'][i]['language'])
        lang_name.append(languages['languages'][i]['name'])

    for i in range(0, 76):
        lang_code_name[lang_code[i]] = lang_name[i]
        lang_name_code[lang_name[i]] = lang_code[i]
def dispLanguages():
    loadLanguages()
    i = 1
    print("We support the following languages ")
    for j in lang_name_code.keys():
        print("{}.{}".format(i, j))
        i = i + 1
def main_process(inp,tcl):
    dispLanguages()
    # To auto detect the language
    language1 = language_translator.identify(inp).get_result()

    dl = language1['languages'][0]['language']  # Detect the language
    print("The entered language name is :  {}".format(lang_code_name[dl]))
    tclc = lang_name_code[tcl]
    print("The language will be translated into {} ".format(lang_code_name[tclc]))

    # To translate the language
    s1 = dl + '-' + tclc
    print("Translating  now ...... ")
    translation = language_translator.translate(
        text=inp,
        model_id=s1).get_result()

    # To get the translated text
    s = str(translation['translations'])
    s = s[18:len(s) - 3]

    print("The original text entered is : {}".format(inp))
    print("The translated text entered is : {}".format(s))
    return str(lang_code_name[dl])+","+str(s)