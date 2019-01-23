package PosTagger;

import Corpus.Sentence;

import java.io.*;
import java.util.*;

public class DummyPosTagger implements PosTagger{
    private String[] tagList;

    public void train(PosTaggedCorpus corpus) {
        Set<String> corpusTagList = corpus.getTagList();
        tagList = new String[corpusTagList.size()];
        tagList = (String[]) corpusTagList.toArray(tagList);
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
