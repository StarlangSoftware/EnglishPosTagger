package PosTagger;

import Corpus.Sentence;
import Dictionary.Word;
import Hmm.*;

import java.io.*;
import java.util.ArrayList;

public class HmmPosTagger implements PosTagger{
    private Hmm<String, Word> hmm;

    public void train(PosTaggedCorpus corpus) {
        ArrayList<String>[] emittedSymbols = new ArrayList[corpus.sentenceCount()];
        for (int i = 0; i < emittedSymbols.length; i++){
            emittedSymbols[i] = new ArrayList<String>();
            for (int j = 0; j < corpus.getSentence(i).wordCount(); j++){
                PosTaggedWord word = (PosTaggedWord) corpus.getSentence(i).getWord(j);
                emittedSymbols[i].add(word.getTag());
            }
        }
        hmm = new Hmm1<String, Word>(corpus.getTagList(), emittedSymbols, corpus.getAllWordsAsArray());
    }

    public Sentence posTag(Sentence sentence) {
        Sentence result = new Sentence();
        ArrayList<String> tagList = hmm.viterbi(sentence.getWords());
        for (int i = 0; i < sentence.wordCount(); i++){
            result.addWord(new PosTaggedWord(sentence.getWord(i).getName(), tagList.get(i)));
        }
        return result;
    }

    public void saveModel() {
        hmm.save("Model/hmmpostagger.bin");
    }

    public void loadModel() {
        FileInputStream inFile;
        ObjectInputStream inObject;
        try {
            inFile = new FileInputStream("Model/hmmpostagger.bin");
            inObject = new ObjectInputStream(inFile);
            hmm = (Hmm1<String, Word>) inObject.readObject();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }
}
