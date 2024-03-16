package PosTagger;

import Corpus.Sentence;
import Dictionary.Word;
import Hmm.*;

import java.io.*;
import java.util.ArrayList;

public class HmmPosTagger implements PosTagger{
    private Hmm<String, Word> hmm;

    /**
     * Train method for the Hmm pos tagger. The algorithm trains a Hmm from the corpus, where corpus constitutes
     * as an observation array.
     *
     * @param corpus Training data for the tagger.
     */
    public void train(PosTaggedCorpus corpus) {
        ArrayList<String>[] emittedSymbols = new ArrayList[corpus.sentenceCount()];
        for (int i = 0; i < emittedSymbols.length; i++){
            emittedSymbols[i] = new ArrayList<>();
            for (int j = 0; j < corpus.getSentence(i).wordCount(); j++){
                PosTaggedWord word = (PosTaggedWord) corpus.getSentence(i).getWord(j);
                emittedSymbols[i].add(word.getTag());
            }
        }
        hmm = new Hmm1<>(corpus.getTagList(), emittedSymbols, corpus.getAllWordsAsArray());
    }

    /**
     * Test method for the Hmm pos tagger. For each sentence, the method uses the viterbi algorithm to produce the
     * most possible state sequence for the given sentence.
     *
     * @param sentence Sentence to be tagged.
     * @return Annotated (tagged) sentence.
     */
    public Sentence posTag(Sentence sentence) {
        Sentence result = new Sentence();
        ArrayList<String> tagList = hmm.viterbi(sentence.getWords());
        for (int i = 0; i < sentence.wordCount(); i++){
            result.addWord(new PosTaggedWord(sentence.getWord(i).getName(), tagList.get(i)));
        }
        return result;
    }

    /**
     * The method saves the pos tagger model.
     */
    public void saveModel() {
        hmm.save("Model/hmmpostagger.bin");
    }

    /**
     * The method loads the pos tagger model.
     */
    public void loadModel() {
        FileInputStream inFile;
        ObjectInputStream inObject;
        try {
            inFile = new FileInputStream("Model/hmmpostagger.bin");
            inObject = new ObjectInputStream(inFile);
            hmm = (Hmm1<String, Word>) inObject.readObject();
        } catch (ClassNotFoundException | IOException ignored) {
        }
    }
}
