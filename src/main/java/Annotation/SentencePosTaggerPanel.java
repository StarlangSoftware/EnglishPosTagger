package Annotation;

import AnnotatedSentence.AnnotatedSentence;
import AnnotatedSentence.ViewLayerType;
import AnnotatedSentence.AnnotatedWord;
import DataCollector.Sentence.SentenceAnnotatorPanel;
import Dictionary.ExceptionalWord;
import Dictionary.Pos;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class SentencePosTaggerPanel extends SentenceAnnotatorPanel {
    private final String[] pennTagList = {"CC", "CD", "DT", "EX", "FW", "IN", "JJ", "JJR", "JJS", "LS",
                                    "MD", "NN", "NNS", "NNP", "NNPS", "PDT", "POS", "PRP", "PRP$", "RB",
                                    "RBR", "RBS", "RP", "SYM", "TO", "UH", "VB", "VBD", "VBG", "VBN",
                                    "VBP", "VBZ", "WDT", "WP", "WP$", "WRB", "$", "#", ".", ",",
                                    "``", "''", ":", "-LRB-", "-RRB-", "AUX:VB", "AUX:VBP", "AUX:VBZ", "AUX:VBD", "AUX:VBG",
                                    "AUX:VBN"};
    private final HashMap<String, ArrayList<ExceptionalWord>> exceptionList;
    private final HashSet<String> literalList;

    /**
     * Constructor for the pos tag panel for an annotated sentence. Sets the attributes.
     * @param currentPath The absolute path of the annotated file.
     * @param fileName The raw file name of the annotated file.
     * @param exceptionList Enlists exceptional cases in English language.
     * @param literalList Enlists all possible root word for this annotation.
     */
    public SentencePosTaggerPanel(String currentPath, String fileName, HashMap<String, ArrayList<ExceptionalWord>> exceptionList, HashSet<String> literalList){
        super(currentPath, fileName, ViewLayerType.POS_TAG);
        this.exceptionList = exceptionList;
        this.literalList = literalList;
        setLayout(new BorderLayout());
    }

    /**
     * Updates the Pos tag layer of the annotated word.
     */
    @Override
    protected void setWordLayer() {
        clickedWord.setPosTag((String) list.getSelectedValue());
    }

    /**
     * Sets the width and height of the JList that displays the pos tags.
     */
    @Override
    protected void setBounds() {
        pane.setBounds(((AnnotatedWord)sentence.getWord(selectedWordIndex)).getArea().getX(), ((AnnotatedWord)sentence.getWord(selectedWordIndex)).getArea().getY() + 20, 240, (int) (Toolkit.getDefaultToolkit().getScreenSize().height * 0.4));
    }

    /**
     * Sets the space between displayed lines in the sentence.
     */
    @Override
    protected void setLineSpace() {
        lineSpace = 80;
    }

    /**
     * Draws the pos tag and root of the word.
     * @param word Annotated word itself.
     * @param g Graphics on which pos tag and root form is drawn.
     * @param currentLeft Current position on the x-axis, where the pos tag and root form will be aligned.
     * @param lineIndex Current line of the word, if the sentence resides in multiple lines on the screen.
     * @param wordIndex Index of the word in the annotated sentence.
     * @param maxSize Maximum size in pixels of anything drawn in the screen.
     * @param wordSize Array storing the sizes of all words in pixels in the annotated sentence.
     * @param wordTotal Array storing the total size until that word of all words in the annotated sentence.
     */
    @Override
    protected void drawLayer(AnnotatedWord word, Graphics g, int currentLeft, int lineIndex, int wordIndex, int maxSize, ArrayList<Integer> wordSize, ArrayList<Integer> wordTotal) {
        if (word.getMetamorphicParse() != null){
            g.setColor(Color.BLUE);
            String rootForm = word.getMetamorphicParse().getWord().getName();
            g.drawString(rootForm, currentLeft, (lineIndex + 1) * lineSpace + 20);
        }
        if (word.getPosTag() != null){
            g.setColor(Color.RED);
            String correct = word.getPosTag();
            g.drawString(correct, currentLeft, (lineIndex + 1) * lineSpace + 40);
        }
        g.setColor(Color.RED);
    }

    /**
     * Compares the size of the word and the size of the pos tag in pixels and returns the maximum of them.
     * @param word Word annotated.
     * @param g Graphics on which pos tag is drawn.
     * @return Maximum of the graphic sizes of word and its pos tag.
     */
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

    /**
     * Detects the root of the word given its tag. First checks the exception list, if the word is in the exception
     * list, it automatically assigns that root form. After, it checks pronouns and ordinal numbers, and if the word is
     * one of them, assigns the root form accordingly. As a last step, according to the pos tag, checks alternatives
     * such as:
     * <ul>
     *     <li>For plural forms, removes 's', 'es', or 'ies' from the end.</li>
     *     <li>For third person singular verbs, removes 's' from the end.</li>
     *     <li>For past or present perfect tenses, removes 'ed' from the end.</li>
     *     <li>For continuous tenses, removes 'ing' from the end.</li>
     *     <li>For comparative adjectives, removes 'er' from the end.</li>
     *     <li>For superlative adjectives, removes 'esr' from the end.</li>
     * </ul>
     * @param word Word for which root form will be determined.
     * @param tag Pos tag of the word.
     * @return Possible root form of the word.
     */
    private String autoRoot(String word, String tag){
        if (exceptionList.containsKey(word.toLowerCase())) {
            for (ExceptionalWord exceptionalWord : exceptionList.get(word.toLowerCase())){
                if (tag.startsWith("VB") && exceptionalWord.getPos().equals(Pos.VERB)){
                    return exceptionalWord.getRoot();
                }
                if (tag.startsWith("JJ") && exceptionalWord.getPos().equals(Pos.ADJECTIVE)){
                    return exceptionalWord.getRoot();
                }
                if (tag.startsWith("NN") && exceptionalWord.getPos().equals(Pos.NOUN)){
                    return exceptionalWord.getRoot();
                }
                if (tag.startsWith("RB") && exceptionalWord.getPos().equals(Pos.ADVERB)){
                    return exceptionalWord.getRoot();
                }
            }
        }
        switch (word){
            case "'m":
            case "was":
            case "were":
            case "are":
            case "been":
            case "'s":
            case "is":
                return "be";
            case "me":
            case "my":
            case "i":
                return "I";
            case "them":
            case "their":
                return "they";
            case "your":
            case "yours":
                return "you";
            case "her":
                return "she";
            case "him":
            case "his":
                return "he";
            case "am":
                if (tag.endsWith("VBP")){
                    return "be";
                }
                break;
            case "did":
            case "does":
                return "do";
            case "has":
            case "had":
                return "have";
            case "first":
                return "one";
            case "second":
                return "two";
            case "third":
                return "three";
            case "fourth":
                return "four";
            case "fifth":
                return "five";
            case "sixth":
                return "six";
            case "seventh":
                return "seven";
            case "eighth":
                return "eight";
            case "ninth":
                return "nine";
        }
        switch (tag){
            case "NNP":
                return word;
            case "NNPS":
                if (word.endsWith("s")){
                    return word.substring(0, word.length() - 1);
                } else {
                    if (word.endsWith("es")){
                        return word.substring(0, word.length() - 2);
                    } else {
                        return word;
                    }
                }
            case "NNS":
                if (word.endsWith("s") && literalList.contains(word.toLowerCase().substring(0, word.length() - 1))){
                    return word.toLowerCase().substring(0, word.length() - 1);
                } else {
                    if (word.endsWith("es") && literalList.contains(word.toLowerCase().substring(0, word.length() - 2))){
                        return word.toLowerCase().substring(0, word.length() - 2);
                    } else {
                        if (word.endsWith("ies") && literalList.contains(word.toLowerCase().substring(0, word.length() - 3) + "y")) {
                            return word.toLowerCase().substring(0, word.length() - 3) + "y";
                        } else {
                            return word.toLowerCase();
                        }
                    }
                }
            case "VBZ":
                if (word.endsWith("s") && literalList.contains(word.toLowerCase().substring(0, word.length() - 1))){
                    return word.toLowerCase().substring(0, word.length() - 1);
                } else {
                    return word.toLowerCase();
                }
            case "VBN":
            case "VBD":
                if (word.endsWith("ed")){
                    if (literalList.contains(word.toLowerCase().substring(0, word.length() - 1))){
                        return word.toLowerCase().substring(0, word.length() - 1);
                    }
                    if (literalList.contains(word.toLowerCase().substring(0, word.length() - 2))){
                        return word.toLowerCase().substring(0, word.length() - 2);
                    }
                } else {
                    return word.toLowerCase();
                }
            case "JJR":
            case "RBR":
                if (word.endsWith("er") && literalList.contains(word.toLowerCase().substring(0, word.length() - 2))){
                    return word.toLowerCase().substring(0, word.length() - 2);
                } else {
                    return word.toLowerCase();
                }
            case "JJS":
            case "RBS":
                if (word.endsWith("est") && literalList.contains(word.toLowerCase().substring(0, word.length() - 3))){
                    return word.toLowerCase().substring(0, word.length() - 3);
                } else {
                    return word.toLowerCase();
                }
            case "VBG":
                if (word.endsWith("ing")){
                    if (literalList.contains(word.toLowerCase().substring(0, word.length() - 3))){
                        return word.toLowerCase().substring(0, word.length() - 3);
                    } else {
                        if (literalList.contains(word.toLowerCase().substring(0, word.length() - 3) + "e")){
                            return word.toLowerCase().substring(0, word.length() - 3) + "e";
                        }
                    }
                    return word.toLowerCase().substring(0, word.length() - 3);
                } else {
                    return word.toLowerCase();
                }
            default:
                return word.toLowerCase();
        }
    }

    /**
     * Given the frequently used tag list for every root word, the method automatically determines the pos tags of
     * the words in the sentence.
     * @param priorTags Frequently used tag list.
     */
    public void autoDetect(HashMap<String, String> priorTags){
        boolean autoFilled = false;
        for (int i = 0; i < sentence.wordCount(); i++){
            AnnotatedWord word = (AnnotatedWord) sentence.getWord(i);
            String name = word.getName().toLowerCase();
            if (word.getPosTag() == null && priorTags.containsKey(name)){
                word.setPosTag(priorTags.get(name));
                autoFilled = true;
            }
            if (word.getMetamorphicParse() == null && word.getPosTag() != null){
                String root = autoRoot(word.getName(), word.getPosTag());
                if (root != null){
                    word.setMetamorphicParse(root);
                    autoFilled = true;
                }
            }
        }
        if (autoFilled){
            sentence.save();
        }
        this.repaint();
    }

    /**
     * Fills the JList that contains all possible pos tags.
     * @param sentence Sentence used to populate for the current word.
     * @param wordIndex Index of the selected word.
     * @return The index of the selected tag, -1 if nothing selected.
     */
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
