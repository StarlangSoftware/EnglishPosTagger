package PosTagger;

import Dictionary.Word;

import java.io.Serializable;

public class PosTaggedWord extends Word implements Serializable{

    private String tag;

    public PosTaggedWord(String name, String tag){
        this.name = name;
        this.tag = tag;
    }

    public String getTag(){
        return tag;
    }
}
