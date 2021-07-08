package Annotation;

import AnnotatedSentence.AnnotatedCorpus;
import DataCollector.ParseTree.TreeEditorPanel;
import DataCollector.Sentence.SentenceAnnotatorFrame;
import DataCollector.Sentence.SentenceAnnotatorPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

public class SentencePosTaggerFrame extends SentenceAnnotatorFrame  {

    public SentencePosTaggerFrame(){
        super();
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
}
