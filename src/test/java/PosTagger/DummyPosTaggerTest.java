package PosTagger;

import Corpus.Sentence;

import static org.junit.Assert.*;

public class DummyPosTaggerTest {

    @org.junit.Test
    public void testPosTag() {
        PosTagger posTagger = new DummyPosTagger();
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
        assertEquals(0.88, 100 * correct / (correct + incorrect + 0.0), 0.01);
    }
}