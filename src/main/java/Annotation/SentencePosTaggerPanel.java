package Annotation;

import AnnotatedSentence.AnnotatedSentence;
import AnnotatedSentence.ViewLayerType;
import AnnotatedSentence.AnnotatedWord;
import DataCollector.Sentence.SentenceAnnotatorPanel;

import java.awt.*;
import java.util.ArrayList;
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

    @Override
    protected void setWordLayer() {
        clickedWord.setPosTag((String) list.getSelectedValue());
    }

    @Override
    protected void setBounds() {
        pane.setBounds(((AnnotatedWord)sentence.getWord(selectedWordIndex)).getArea().getX(), ((AnnotatedWord)sentence.getWord(selectedWordIndex)).getArea().getY() + 20, 240, (int) (Toolkit.getDefaultToolkit().getScreenSize().height * 0.4));
    }

    @Override
    protected void setLineSpace() {
        lineSpace = 80;
    }

    @Override
    protected void drawLayer(AnnotatedWord word, Graphics g, int currentLeft, int lineIndex, int wordIndex, int maxSize, ArrayList<Integer> wordSize, ArrayList<Integer> wordTotal) {
        if (word.getPosTag() != null){
            String correct = word.getPosTag();
            g.drawString(correct, currentLeft, (lineIndex + 1) * lineSpace + 30);
        }
    }

    @Override
    protected int getMaxLayerLength(AnnotatedWord word, Graphics g) {
        int maxSize = g.getFontMetrics().stringWidth(word.getName());
        if (word.getPosTag() != null){
            int size = g.getFontMetrics().stringWidth(word.getPosTag());
            if (size > maxSize){
                maxSize = size;
            }
        }
        return maxSize;
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
