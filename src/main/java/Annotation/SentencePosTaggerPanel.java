package Annotation;

import AnnotatedSentence.AnnotatedSentence;
import AnnotatedSentence.ViewLayerType;
import AnnotatedSentence.AnnotatedWord;
import DataCollector.Sentence.SentenceAnnotatorPanel;

import java.awt.*;
import java.util.HashMap;

public class SentencePosTaggerPanel extends SentenceAnnotatorPanel {
    private String[] pennTagList = {"CC", "CD", "DT", "EX", "FW", "IN", "JJ", "JJR", "JJS", "LS",
                                    "MD", "NN", "NNS", "NNP", "NNPS", "PDT", "POS", "PRP", "PRP$", "RB",
                                    "RBR", "RBS", "RP", "SYM", "TO", "UH", "VB", "VBD", "VBG", "VBN",
                                    "VBP", "VBZ", "WDT", "WP", "WP$", "WRB", "$", "#", ".", ",",
                                    "``", "''", ":", "-LRB-", "-RRB-", "AUX:VB", "AUX:VBP", "AUX:VBZ", "AUX:VBD", "AUX:VBG",
                                    "AUX:VBN"};

    public SentencePosTaggerPanel(String currentPath, String fileName){
        super(currentPath, fileName, ViewLayerType.POS_TAG);
        setLayout(new BorderLayout());
    }

    public void autoDetect(HashMap<String, String> priorTags){
        boolean autoFilled = false;
        for (int i = 0; i < sentence.wordCount(); i++){
            AnnotatedWord word = (AnnotatedWord) sentence.getWord(i);
            String name = word.getName().toLowerCase();
            if (word.getPosTag() == null && priorTags.containsKey(name)){
                word.setPosTag(priorTags.get(name));
                autoFilled = true;
            }
        }
        if (autoFilled){
            sentence.save();
        }
        this.repaint();
    }

    public int populateLeaf(AnnotatedSentence sentence, int wordIndex){
        int selectedIndex = -1;
        AnnotatedWord word = (AnnotatedWord) sentence.getWord(wordIndex);
        listModel.clear();
        for (int i = 0; i < pennTagList.length; i++){
            if (word.getPosTag() != null && word.getPosTag().equals(pennTagList[i])){
                selectedIndex = i;
            }
            listModel.addElement(pennTagList[i]);
        }
        return selectedIndex;
    }

}
