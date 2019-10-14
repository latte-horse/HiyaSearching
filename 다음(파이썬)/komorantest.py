from konlpy.tag import Komoran
import os
from gensim.models import Word2Vec
import tensorflow as tf

komoran = Komoran()


##########################################절대경로 표시 
def allfiles(path):
    res = []

    for root, dirs, files in os.walk(path):
        rootpath = os.path.join(os.path.abspath(path), root)

        for file in files:
            filepath = os.path.join(rootpath, file)
            res.append(filepath)

    return res

result = allfiles("C:/바탕화면/SAMPLE~1")

pos = []
del pos[:]


for filelist in result:
    try:
        doc = open(filelist , encoding = 'UTF8').read()
        pos.append(komoran.nouns(doc))
    
    
    
        #cnt = Counter(pos)
        
    except Exception:
        print('error')



embedding_model = Word2Vec(pos, size = 20 ,  window = 2 , iter = 100 , sg = 1 , workers = 4) 
print(embedding_model.most_similar(positive = ["노조"] ,  topn = 20))





