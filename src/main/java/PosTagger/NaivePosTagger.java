package PosTagger;

import Corpus.Sentence;
import DataStructure.CounterHashMap;

import java.io.*;
import java.util.HashMap;

public class NaivePosTagger implements PosTagger{
    private HashMap<String, String> maxMap;

    public void train(PosTaggedCorpus corpus) {
        HashMap<String, CounterHashMap<String>> map = new HashMap<>();
        for (int i = 0; i < corpus.sentenceCount(); i++){
            Sentence s = corpus.getSentence(i);
            for (int j = 0; j < s.wordCount(); j++){
                PosTaggedWord word = (PosTaggedWord) corpus.getSentence(i).getWord(j);
                if (map.containsKey(word.getName())){
                    map.get(word.getName()).put(word.getTag());
                } else {
                    CounterHashMap<String> counterMap = new CounterHashMap<>();
                    counterMap.put(word.getTag());
                    map.put(word.getName(), counterMap);
                }
            }
        }
        maxMap = new HashMap<>();
        for (String word : map.keySet()){
            maxMap.put(word, map.get(word).max());
        }
    }

    public Sentence posTag(Sentence sentence) {
        Sentence result = new Sentence();
        for (int i = 0; i < sentence.wordCount(); i++){
            result.addWord(new PosTaggedWord(sentence.getWord(i).getName(), maxMap.get(sentence.getWord(i).getName())));
        }
        return result;
    }

    public void saveModel() {
        FileOutputStream outFile;
        ObjectOutputStream outObject;
        try {
            outFile = new FileOutputStream("naive.bin");
            outObject = new ObjectOutputStream (outFile);
            outObject.writeObject(maxMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadModel() {
        FileInputStream inFile;
        ObjectInputStream inObject;
        try {
            inFile = new FileInputStream("naive.bin");
            inObject = new ObjectInputStream(inFile);
            maxMap = (HashMap<String, String>) inObject.readObject();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }
}
