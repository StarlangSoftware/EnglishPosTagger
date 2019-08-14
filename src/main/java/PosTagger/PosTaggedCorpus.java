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

    /**
     * A constructor of {@link PosTaggedCorpus} which initializes the sentences of the corpus, the word list of
     * the corpus, and all possible tags.
     */
    public PosTaggedCorpus(){
        sentences = new ArrayList<Sentence>();
        wordList = new CounterHashMap<>();
        tagList = new CounterHashMap<>();
    }

    /**
     * A clone method for the {@link PosTaggedCorpus} class.
     *
     * @return A copy of the current {@link PosTaggedCorpus} class.
     */
    public PosTaggedCorpus emptyCopy(){
        return new PosTaggedCorpus();
    }

    /**
     * Another constructor of {@link PosTaggedCorpus} which takes a fileName of the corpus as an input, reads the
     * corpus from that file.
     *
     * @param fileName Name of the corpus file.
     */
    public PosTaggedCorpus(String fileName){
        String line, name, tag, shortTag;
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
                            if (tag.contains("+")){
                                shortTag = tag.substring(0, tag.indexOf("+"));
                            } else {
                                if (tag.contains("-")){
                                    shortTag = tag.substring(0, tag.indexOf("-"));
                                } else {
                                    shortTag = tag;
                                }
                            }
                            tagList.put(shortTag);
                            newSentence.addWord(new PosTaggedWord(name, shortTag));
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

    /**
     * getTagList returns all possible tags as a set.
     *
     * @return Set of all possible tags.
     */
    public Set<String> getTagList(){
        return tagList.keySet();
    }

}
