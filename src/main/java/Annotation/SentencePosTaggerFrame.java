package Annotation;

import AnnotatedSentence.AnnotatedCorpus;
import DataCollector.ParseTree.TreeEditorPanel;
import DataCollector.Sentence.SentenceAnnotatorFrame;
import DataCollector.Sentence.SentenceAnnotatorPanel;
import Dictionary.ExceptionalWord;
import Dictionary.Pos;
import Util.FileUtils;
import Xml.XmlDocument;
import Xml.XmlElement;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class SentencePosTaggerFrame extends SentenceAnnotatorFrame  {
    private final JCheckBox autoPosDetectionOption;
    private final HashMap<String, String> priorTags = new HashMap<>();
    private HashMap<String, ArrayList<ExceptionalWord>> exceptionList;
    private HashSet<String> literalList;

    /**
     * Reads distinct Penn pos tags used for every word in the Penn-Treebank corpus. The file consist of multiple lines,
     * where each line starts with the word, then following words are tags assigned to that word in Penn-Treebank
     * corpus. The tags are separated via tab character.
     */
    private void readPennDefaultTags(){
        ClassLoader classLoader = getClass().getClassLoader();
        Random random = new Random();
        BufferedReader br = new BufferedReader(new InputStreamReader(classLoader.getResourceAsStream("penn-postags.txt"), StandardCharsets.UTF_8));
        String line;
        try {
            line = br.readLine();
            while (line != null) {
                String[] items = line.split("\\t");
                priorTags.put(items[0], items[1 + random.nextInt(items.length - 1)]);
                line = br.readLine();
            }
        } catch (IOException ignored) {
        }
    }

    /**
     * Reads distinct possible root words for this corpus to assign. Each line stores a distinct word.
     * @param fileName File name
     */
    private void readLiteralList(String fileName){
        literalList = new HashSet<>();
        ClassLoader classLoader = getClass().getClassLoader();
        BufferedReader br = new BufferedReader(new InputStreamReader(classLoader.getResourceAsStream(fileName), StandardCharsets.UTF_8));
        String line;
        try {
            line = br.readLine();
            while (line != null) {
                literalList.add(line);
                line = br.readLine();
            }
        } catch (IOException ignored) {
        }
    }

    /**
     * Constructor of the Pos tagger frame for annotated sentence. It reads the annotated sentence corpus and adds
     * automatic pos tag detection button. It also reads exception word list in English, default tag set for every
     * word, and root word list.
     */
    public SentencePosTaggerFrame(){
        super();
        autoPosDetectionOption = new JCheckBox("Auto Part Of Speech Detection", false);
        toolBar.add(autoPosDetectionOption);
        readExceptionFile("english_exception.xml");
        readLiteralList("english_literals.txt");
        readPennDefaultTags();
        AnnotatedCorpus corpus;
        corpus = new AnnotatedCorpus(new File(TreeEditorPanel.phrasePath));
        JMenuItem itemViewAnnotated = addMenuItem(projectMenu, "View Annotations", KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
        itemViewAnnotated.addActionListener(e -> new ViewSentencePosTaggerAnnotationFrame(corpus, this));
        JOptionPane.showMessageDialog(this, "Annotated corpus is loaded!", "Shallow Parse Annotation", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    protected SentenceAnnotatorPanel generatePanel(String currentPath, String rawFileName) {
        return new SentencePosTaggerPanel(currentPath, rawFileName, exceptionList, literalList);
    }

    /**
     * Method constructs a DOM parser using the dtd/xml schema parser configuration and using this parser it
     * reads exceptions from file and puts to exceptionList HashMap.
     *
     * @param exceptionFileName exception file to be read
     */
    public void readExceptionFile(String exceptionFileName) {
        String wordName, rootForm;
        Pos pos;
        XmlElement wordNode, rootNode;
        XmlDocument doc = new XmlDocument(FileUtils.getInputStream(exceptionFileName));
        doc.parse();
        rootNode = doc.getFirstChild();
        wordNode = rootNode.getFirstChild();
        exceptionList = new HashMap<>();
        while (wordNode != null) {
            if (wordNode.hasAttributes()) {
                wordName = wordNode.getAttributeValue("name");
                rootForm = wordNode.getAttributeValue("root");
                switch (wordNode.getAttributeValue("pos")) {
                    case "Adj":
                        pos = Pos.ADJECTIVE;
                        break;
                    case "Adv":
                        pos = Pos.ADVERB;
                        break;
                    case "Verb":
                        pos = Pos.VERB;
                        break;
                    default:
                        pos = Pos.NOUN;
                        break;
                }
                ArrayList<ExceptionalWord> rootList;
                if (exceptionList.containsKey(wordName)){
                    rootList = exceptionList.get(wordName);
                } else {
                    rootList = new ArrayList<>();
                }
                rootList.add(new ExceptionalWord(wordName, rootForm, pos));
                exceptionList.put(wordName, rootList);
            }
            wordNode = wordNode.getNextSibling();
        }
    }

    /**
     * The next method takes an int count as input and moves forward along the SentencePosTaggerPanels as much as the
     * count. If the autoPosDetectionOption is selected, it automatically assigns pos tags to words.
     * @param count Integer count is used to move forward.
     */
    public void next(int count){
        super.next(count);
        SentencePosTaggerPanel current;
        current = (SentencePosTaggerPanel) ((JScrollPane) projectPane.getSelectedComponent()).getViewport().getView();
        if (autoPosDetectionOption.isSelected()){
            current.autoDetect(priorTags);
        }
    }

    /**
     * The previous method takes an int count as input and moves backward along the SentencePosTaggerPanels as much as
     * the count. If the autoPosDetectionOption is selected, it automatically assigns pos tags to words.
     * @param count Integer count is used to move backward.
     */
    public void previous(int count){
        super.previous(count);
        SentencePosTaggerPanel current;
        current = (SentencePosTaggerPanel) ((JScrollPane) projectPane.getSelectedComponent()).getViewport().getView();
        if (autoPosDetectionOption.isSelected()){
            current.autoDetect(priorTags);
        }
    }

}
