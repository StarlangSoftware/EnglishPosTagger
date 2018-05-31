package PosTagger;

import Corpus.*;
import DataStructure.CounterHashMap;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

public class PosTaggedCorpus extends Corpus{

    private CounterHashMap<String> tagList;

    public PosTaggedCorpus(){
        sentences = new ArrayList<Sentence>();
        wordList = new CounterHashMap<>();
        tagList = new CounterHashMap<>();
    }

    public PosTaggedCorpus emptyCopy(){
        return new PosTaggedCorpus();
    }

    public PosTaggedCorpus(String fileName){
        String line, name, tag;
        Sentence newSentence = new Sentence();
        sentences = new ArrayList<Sentence>();
        tagList = new CounterHashMap<>();
        try {
            FileReader fr = new FileReader(fileName);
            BufferedReader br = new BufferedReader(fr);
            line = br.readLine();
            while (line != null){
                String words[] = line.split("[ \\t]");
                for (String word:words){
                    if (!word.isEmpty()){
                        if (word.contains("/")){
                            name = word.substring(0, word.lastIndexOf('/'));
                            tag = word.substring(word.lastIndexOf('/') + 1);
                            tagList.put(tag);
                            newSentence.addWord(new PosTaggedWord(name, tag));
                            if (tag.equalsIgnoreCase(".")){
                                addSentence(newSentence);
                                newSentence = new Sentence();
                            }
                        } else {
                            System.out.println("Word " + word + " does not contain /\n");
                        }
                    }
                }
                line = br.readLine();
            }
            if (newSentence.wordCount() > 0){
                addSentence(newSentence);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Set<String> getTagList(){
        return tagList.keySet();
    }

}
