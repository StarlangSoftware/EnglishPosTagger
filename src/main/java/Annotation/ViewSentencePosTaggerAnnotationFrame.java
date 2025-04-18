package Annotation;

import AnnotatedSentence.*;
import DataCollector.ParseTree.TreeEditorPanel;
import DataCollector.Sentence.ViewSentenceAnnotationFrame;
import Dictionary.Word;
import WordNet.WordNet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ViewSentencePosTaggerAnnotationFrame extends ViewSentenceAnnotationFrame implements ActionListener  {
    protected int ROOT_INDEX;
    private final WordNet english;

    /**
     * Updates the pos tag for the selected sentences.
     * @param e Action event to be processed.
     */
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        if (PASTE.equals(e.getActionCommand())) {
            if (selectedRow != -1) {
                for (int rowNo : dataTable.getSelectedRows()) {
                    updatePosTag(rowNo, data.get(selectedRow).get(TAG_INDEX));
                }
                for (int rowNo : dataTable.getSelectedRows()) {
                    updateRoot(rowNo, data.get(selectedRow).get(ROOT_INDEX));
                }
            }
        }
        dataTable.invalidate();
    }

    public class PosTaggerTableDataModel extends TableDataModel {

        /**
         * Returns the name of the given column.
         * @param col  the column being queried
         * @return Name of the given column
         */
        public String getColumnName(int col) {
            switch (col) {
                case FILENAME_INDEX:
                    return "FileName";
                case WORD_POS_INDEX:
                    return "Index";
                case WORD_INDEX:
                    return "Word";
                case 3:
                    return "Pos Tag";
                case 4:
                    return "Root";
                case 5:
                    return "Sentence";
                default:
                    return "";
            }
        }

        /**
         * Updates the pos tag for the sentence in the given cell.
         * @param value   value to assign to cell
         * @param row   row of cell
         * @param col  column of cell
         */
        public void setValueAt(Object value, int row, int col) {
            if (col == TAG_INDEX && !data.get(row).get(TAG_INDEX).equals(value)) {
                updatePosTag(row, (String) value);
            }
            if (col == ROOT_INDEX && !data.get(row).get(ROOT_INDEX).equals(value)) {
                updateRoot(row, (String) value);
            }
        }

        public boolean isCellEditable(int row, int col) {
            return col == TAG_INDEX || col == ROOT_INDEX;
        }
    }

    /**
     * Sets the value in the data table. After finding the corresponding sentence in that row, updates the pos tag of
     * that word associated with that row.
     * @param row Index of the row
     * @param newValue Pos tag to be assigned.
     */
    private void updatePosTag(int row, String newValue){
        data.get(row).set(TAG_INDEX, newValue);
        AnnotatedSentence sentence = (AnnotatedSentence) corpus.getSentence(Integer.parseInt(data.get(row).get(COLOR_COLUMN_INDEX - 1)));
        AnnotatedWord word = (AnnotatedWord) sentence.getWord(Integer.parseInt(data.get(row).get(WORD_POS_INDEX)) - 1);
        word.setPosTag(newValue);
        sentence.save();
    }

    /**
     * Sets the value in the data table. After finding the corresponding sentence in that row, updates the root form of
     * that word associated with that row.
     * @param row Index of the row
     * @param newValue Root form to be assigned.
     */
    private void updateRoot(int row, String newValue){
        data.get(row).set(ROOT_INDEX, newValue);
        AnnotatedSentence sentence = (AnnotatedSentence) corpus.getSentence(Integer.parseInt(data.get(row).get(COLOR_COLUMN_INDEX - 1)));
        AnnotatedWord word = (AnnotatedWord) sentence.getWord(Integer.parseInt(data.get(row).get(WORD_POS_INDEX)) - 1);
        word.setMetamorphicParse(newValue);
        sentence.save();
    }

    /**
     * Constructs the data table. For every sentence, the columns are:
     * <ol>
     *     <li>Annotated sentence file name</li>
     *     <li>Index of the word</li>
     *     <li>Word itself</li>
     *     <li>Pos tag of the word</li>
     *     <li>Root of the word</li>
     *     <li>Annotated sentence itself</li>
     *     <li>Sentence index</li>
     * </ol>
     * @param corpus Annotated corpus
     */
    protected void prepareData(AnnotatedCorpus corpus){
        data = new ArrayList<>();
        for (int i = 0; i < corpus.sentenceCount(); i++){
            AnnotatedSentence sentence = (AnnotatedSentence) corpus.getSentence(i);
            for (int j = 0; j < corpus.getSentence(i).wordCount(); j++){
                AnnotatedWord word = (AnnotatedWord) sentence.getWord(j);
                ArrayList<String> row = new ArrayList<>();
                row.add(sentence.getFileName());
                row.add("" + (j + 1));
                row.add(word.getName());
                row.add(word.getPosTag());
                if (word.getMetamorphicParse() != null){
                    String root = word.getMetamorphicParse().toString();
                    if (!root.isEmpty()){
                        String capital = (root.charAt(0) + "").toUpperCase() + root.toLowerCase().substring(1);
                        if (Word.isPunctuation(root) || Word.isEnglishStopWord(root) || root.equalsIgnoreCase("what") ||
                                root.equalsIgnoreCase("that") || root.equalsIgnoreCase("this") || root.equalsIgnoreCase("when") ||
                                root.equalsIgnoreCase("which") || root.equalsIgnoreCase("how") || root.equalsIgnoreCase("where") ||
                                root.equalsIgnoreCase("whose") || root.equalsIgnoreCase("whom") || root.matches("\\d+") ||
                                !english.getSynSetsWithLiteral(root).isEmpty() || !english.getSynSetsWithLiteral(capital).isEmpty()){
                            row.add(root);
                        } else {
                            row.add("<html><b><font color=\"red\">" + root + "</html>");
                        }
                    } else {
                        row.add("");
                    }
                } else {
                    row.add("");
                }
                row.add(sentence.toWords());
                row.add("" + i);
                row.add("0");
                data.add(row);
            }
        }
    }

    /**
     * Constructs Pos tag annotation frame viewer. Arranges the minimum width, maximum width or with of every column. If
     * the user double-clicks any row, the method automatically creates a new panel showing associated annotated
     * sentence.
     * @param corpus Annotated corpus
     * @param sentencePosTaggerFrame Frame in which new panels will be created, when the user double-clicks a row.
     */
    public ViewSentencePosTaggerAnnotationFrame(AnnotatedCorpus corpus, SentencePosTaggerFrame sentencePosTaggerFrame){
        super(corpus);
        english = new WordNet("english_wordnet_version_31.xml");
        COLOR_COLUMN_INDEX = 7;
        TAG_INDEX = 3;
        ROOT_INDEX = 4;
        prepareData(corpus);
        dataTable = new JTable(new PosTaggerTableDataModel());
        dataTable.getColumnModel().getColumn(FILENAME_INDEX).setMinWidth(150);
        dataTable.getColumnModel().getColumn(FILENAME_INDEX).setMaxWidth(150);
        dataTable.getColumnModel().getColumn(WORD_POS_INDEX).setMinWidth(60);
        dataTable.getColumnModel().getColumn(WORD_POS_INDEX).setMaxWidth(60);
        dataTable.getColumnModel().getColumn(WORD_INDEX).setWidth(200);
        dataTable.getColumnModel().getColumn(TAG_INDEX).setWidth(200);
        dataTable.setDefaultRenderer(Object.class, new CellRenderer());
        dataTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2){
                    int row = dataTable.rowAtPoint(evt.getPoint());
                    if (row >= 0) {
                        String fileName = data.get(row).get(0);
                        sentencePosTaggerFrame.addPanelToFrame(sentencePosTaggerFrame.generatePanel(TreeEditorPanel.phrasePath, fileName), fileName);
                    }
                }
            }
        });
        JScrollPane tablePane = new JScrollPane(dataTable);
        add(tablePane, BorderLayout.CENTER);
    }

}
