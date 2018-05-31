package PosTagger;

import Corpus.Sentence;

public interface PosTagger {

    void train(PosTaggedCorpus corpus);
    Sentence posTag(Sentence sentence);
    void saveModel();
    void loadModel();

}
