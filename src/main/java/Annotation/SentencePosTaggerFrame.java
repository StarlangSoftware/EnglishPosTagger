package Annotation;

import AnnotatedSentence.AnnotatedCorpus;
import DataCollector.ParseTree.TreeEditorPanel;
import DataCollector.Sentence.SentenceAnnotatorFrame;
import DataCollector.Sentence.SentenceAnnotatorPanel;
import Dictionary.TxtWord;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Random;

public class SentencePosTaggerFrame extends SentenceAnnotatorFrame  {
    private JCheckBox autoPosDetectionOption;
    private HashMap<String, String> priorTags = new HashMap<>();

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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SentencePosTaggerFrame(){
        super();
        autoPosDetectionOption = new JCheckBox("Auto Part Of Speech Detection", false);
        toolBar.add(autoPosDetectionOption);
        readPennDefaultTags();
        AnnotatedCorpus corpus;
        corpus = new AnnotatedCorpus(new File(TreeEditorPanel.phrasePath));
        JMenuItem itemViewAnnotated = addMenuItem(projectMenu, "View Annotations", KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        itemViewAnnotated.addActionListener(e -> {
            new ViewSentencePosTaggerAnnotationFrame(corpus, this);
        });
        JOptionPane.showMessageDialog(this, "Annotated corpus is loaded!", "Shallow Parse Annotation", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    protected SentenceAnnotatorPanel generatePanel(String currentPath, String rawFileName) {
        return new SentencePosTaggerPanel(currentPath, rawFileName);
    }

    public void next(int count){
        super.next(count);
        SentencePosTaggerPanel current;
        current = (SentencePosTaggerPanel) ((JScrollPane) projectPane.getSelectedComponent()).getViewport().getView();
        if (autoPosDetectionOption.isSelected()){
            current.autoDetect(priorTags);
        }
    }

    public void previous(int count){
        super.previous(count);
        SentencePosTaggerPanel current;
        current = (SentencePosTaggerPanel) ((JScrollPane) projectPane.getSelectedComponent()).getViewport().getView();
        if (autoPosDetectionOption.isSelected()){
            current.autoDetect(priorTags);
        }
    }

}
