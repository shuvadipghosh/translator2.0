import networkx as nx
import numpy as np
import nltk
from nltk.cluster.util import cosine_distance
from nltk.corpus import stopwords
while not nltk.download('stopwords'):
    print("Retrying download")
para = ["On 28 June 1914, Gavrilo Princip, a Bosnian Serb Yugoslav nationalist and member of the Serbian Black Hand military society, assassinated the Austro-Hungarian heir Archduke Franz Ferdinand in Sarajevo, leading to the July Crisis.[15][16] In response, Austria-Hungary issued an ultimatum to Serbia on 23 July. Serbia's reply failed to satisfy the Austrians, and the two moved to a war footing. A network of interlocking alliances enlarged the crisis from a bilateral issue in the Balkans to one involving most of Europe. By July 1914, the great powers of Europe were divided into two coalitions: the Triple Entente, consisting of France, Russia, and Britain; and the Triple Alliance of Germany, Austria-Hungary, and Italy. The Triple Alliance was only defensive in nature, allowing Italy to stay out of the war until 26 April 1915, when it joined the Allied Powers after its relations with Austria-Hungary deteriorated.[17] Russia felt it necessary to back Serbia, and approved partial mobilisation after Austria-Hungary shelled the Serbian capital of Belgrade, which was a few kilometres from the border, on 28 July 1914.[18] Full Russian mobilisation was announced on the evening of 30 July; the following day, Austria-Hungary and Germany did the same, while Germany demanded Russia demobilise within twelve hours.[19] When Russia failed to comply, Germany declared war on Russia on 1 August 1914 in support of Austria-Hungary, the latter following suit on 6 August 1914. France ordered full mobilisation in support of Russia on 2 August 1914.[20] In the end, World War I would see the continent of Europe split into two major opposing alliances; the Allied Powers, primarily composed of the United Kingdom of Great Britain & Ireland, the United States, France, the Russian Empire, Italy, Japan, Portugal, and the many aforementioned Balkan States such as Serbia and Montenegro; and the Central Powers, primarily composed of the German Empire, the Austro-Hungarian Empire, the Ottoman Empire and Bulgaria.Germany strategy for a war on two fronts against France and Russia was to rapidly concentrate the bulk of its army in the West to defeat France within 6 weeks, then shift forces to the East before Russia could fully mobilise; this was later known as the Schlieffen Plan.[21] On 2 August, Germany demanded free passage through Belgium, an essential element in achieving a quick victory over France.[22] When this was refused, German forces invaded Belgium on 3 August and declared war on France the same day; the Belgian government invoked the 1839 Treaty of London and, in compliance with its obligations under this treaty, Britain declared war on Germany on 4 August. On 12 August, Britain and France also declared war on Austria-Hungary; on 23 August, Japan sided with Britain, seizing German possessions in China and the Pacific. In November 1914, the Ottoman Empire entered the war on the side of Austria-Hungary and Germany, opening fronts in the Caucasus, Mesopotamia, and the Sinai Peninsula. The war was fought in (and drew upon) each power's colonial empire also, spreading the conflict to Africa and across the globe."]


# This is the module to find out the  summary of the given passage

# To read the article
def read_article(file_name):
    try:
        file = open(file_name, "r")
        fileData = file.readlines()
        fn = len(fileData[0])
        article = fileData[0].split(". ")
        sentences = []
        print("Original passage : ")
        print(fileData[0])
        for sentence in article:
            sentences.append(sentence.replace("[^a-zA-Z]", " ").split(" "))
        sentences.pop()
        for i in range(0, 75):
            print('*', end=" ")
        print("Number of the characters : {}".format(fn))
        return sentences
    except OSError:
        print('{} {} {}'.format("File ", file_name, "not found "))
        print("\nExiting now from the application")
        exit()


# To find out the similarity matrix to find out the similarity between the sentences
def sentence_similarity(sent1, sent2, stopWords=None):
    if stopWords is None:
        stopWords = []

    sent1 = [w.lower() for w in sent1]
    sent2 = [w.lower() for w in sent2]

    all_words = list(set(sent1 + sent2))

    vector1 = [0] * len(all_words)
    vector2 = [0] * len(all_words)

    # build the vector for the first sentence
    for w in sent1:
        if w in stopWords:
            continue
        vector1[all_words.index(w)] += 1

    # build the vector for the second sentence
    for w in sent2:
        if w in stopWords:
            continue
        vector2[all_words.index(w)] += 1

    return 1 - cosine_distance(vector1, vector2)


# Method to create the similarity matrix
def build_similarity_matrix(sentences, stop_words):
    # Create an empty similarity matrix
    similarity_matrix = np.zeros((len(sentences), len(sentences)))

    for idx1 in range(len(sentences)):
        for idx2 in range(len(sentences)):
            if idx1 == idx2:  # ignore if both are same sentences
                continue
            similarity_matrix[idx1][idx2] = sentence_similarity(sentences[idx1], sentences[idx2], stop_words)

    return similarity_matrix


def generate_summary(sentences, top_n=5):
    print(sentences)
    sentences=list(sentences)
    article = sentences[0].split('.')
    print("Inside python")
    for sentence in article:
        sentences.append(sentence.replace("[^a-zA-Z]", " ").split(" "))
    sentences.pop()
    stop_words = stopwords.words('english')
    summarize_text = []

    # Step 2 - Generate Similarity Matrix across sentences
    sentence_similarity_martix = build_similarity_matrix(sentences, stop_words)

    # Step 3 - Rank sentences in similarity matrix
    sentence_similarity_graph = nx.from_numpy_array(sentence_similarity_martix)
    scores = nx.pagerank(sentence_similarity_graph)

    # Step 4 - Sort the rank and pick top sentences
    ranked_sentence = sorted(((scores[i], s) for i, s in enumerate(sentences)), reverse=True)
    # print("Indexes of top ranked_sentence order are ", ranked_sentence)

    for i in range(top_n):
        summarize_text.append(" ".join(ranked_sentence[i][1]))
    for i in range(0, 5):
        print()
    # Step 5 - Off course, output the summarize text
    print("Summarize Text: \n", ". ".join(summarize_text))
    print(summarize_text)
    return ". ".join(summarize_text)

