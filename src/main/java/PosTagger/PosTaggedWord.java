package PosTagger;

import Dictionary.Word;

import java.io.Serializable;

public class PosTaggedWord extends Word implements Serializable{

    private final String tag;

    /**
     * A constructor of {@link PosTaggedWord} which takes name and tag as input and sets the corresponding attributes
     * @param name Name of the word
     * @param tag Tag of the word
     */
    public PosTaggedWord(String name, String tag){
        super(name);
        this.tag = tag;
    }

    /**
     * Accessor method for tag attribute.
     *
     * @return Tag of the word.
     */
    public String getTag(){
        return tag;
    }
}
