package PosTagger;

import Corpus.Sentence;

public class TestPosTag {

    private static void saveHmmTagger(PosTaggedCorpus corpus, PosTagger posTagger){
        posTagger.train(corpus);
        posTagger.saveModel();
    }

    private static Sentence loadHmmTagger(Sentence sentence){
        HmmPosTagger hmmPosTagger = new HmmPosTagger();
        hmmPosTagger.loadModel();
        return hmmPosTagger.posTag(sentence);
    }

    private static Sentence hmmTagger(PosTaggedCorpus corpus, Sentence sentence){
        HmmPosTagger hmmPosTagger = new HmmPosTagger();
        hmmPosTagger.train(corpus);
        return hmmPosTagger.posTag(sentence);
    }

    private static void testTagger(PosTagger posTagger){
        PosTaggedCorpus posTaggedCorpus = new PosTaggedCorpus("brown.txt");
        posTagger.train(posTaggedCorpus);
        int correct = 0, incorrect = 0;
        for (int i = 0; i < posTaggedCorpus.sentenceCount(); i++){
            Sentence taggedSentence = posTagger.posTag(posTaggedCorpus.getSentence(i));
            for (int j = 0; j < taggedSentence.wordCount(); j++){
                if (((PosTaggedWord) posTaggedCorpus.getSentence(i).getWord(j)).getTag().equals(((PosTaggedWord)taggedSentence.getWord(j)).getTag())){
                    correct++;
                } else {
                    incorrect++;
                }
            }
        }
        System.out.println("Accuracy: " + 100 * correct / (correct + incorrect + 0.0));
    }

    public static void main(String[] args){
        PosTagger tagger = new DummyPosTagger();
        //PosTagger tagger = new NaivePosTagger();
        //PosTagger tagger = new HmmPosTagger();
        testTagger(tagger);
    }
}
