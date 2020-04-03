package PosTagger;

import Corpus.Sentence;

import java.io.*;
import java.util.*;

public class DummyPosTagger implements PosTagger{
    private String[] tagList;

    /**
     * Train method for the Dummy pos tagger. The algorithm gets all possible tag list.
     *
     * @param corpus Training data for the tagger.
     */
    public void train(PosTaggedCorpus corpus) {
        Set<String> corpusTagList = corpus.getTagList();
        tagList = new String[corpusTagList.size()];
        tagList = (String[]) corpusTagList.toArray(tagList);
    }

    /**
     * Test method for the Dummy pos tagger. For each word, the method chooses randomly a tag from all possible
     * tag list.
     *
     * @param sentence Sentence to be tagged.
     * @return Annotated (tagged) sentence.
     */
    public Sentence posTag(Sentence sentence) {
        Random random = new Random();
        Sentence result = new Sentence();
        for (int i = 0; i < sentence.wordCount(); i++){
            result.addWord(new PosTaggedWord(sentence.getWord(i).getName(), tagList[random.nextInt(tagList.length)]));
        }
        return result;
    }

    /**
     * The method saves the pos tagger model.
     */
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

    /**
     * The method loads the pos tagger model.
     */
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
