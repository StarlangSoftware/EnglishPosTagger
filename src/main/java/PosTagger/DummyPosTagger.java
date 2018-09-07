package PosTagger;

import Corpus.Sentence;

import java.io.*;
import java.util.*;

public class DummyPosTagger implements PosTagger{
    private String[] tagList;

    public void train(PosTaggedCorpus corpus) {
        Set tagList;
        tagList = new HashSet();
        for (int i = 0; i < corpus.sentenceCount(); i++){
             Sentence s = corpus.getSentence(i);
            for (int j = 0; j < s.wordCount(); j++){
                PosTaggedWord word = (PosTaggedWord) corpus.getSentence(i).getWord(j);
                tagList.add(word.getTag());
            }
        }
        this.tagList = new String[tagList.size()];
        this.tagList = (String[]) tagList.toArray(this.tagList);
    }

    public Sentence posTag(Sentence sentence) {
        Random random = new Random();
        Sentence result = new Sentence();
        for (int i = 0; i < sentence.wordCount(); i++){
            result.addWord(new PosTaggedWord(sentence.getWord(i).getName(), tagList[random.nextInt(tagList.length)]));
        }
        return result;
    }

    public void saveModel() {
        FileOutputStream outFile;
        ObjectOutputStream outObject;
        try {
            outFile = new FileOutputStream("dummy.bin");
            outObject = new ObjectOutputStream (outFile);
            outObject.writeObject(tagList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadModel() {
        FileInputStream inFile;
        ObjectInputStream inObject;
        try {
            inFile = new FileInputStream("dummy.bin");
            inObject = new ObjectInputStream(inFile);
            tagList = (String[]) inObject.readObject();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }
}
