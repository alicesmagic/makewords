package home.alicesmagic.mw.view;

import home.alicesmagic.mw.logic.IndexesLangInput;
import home.alicesmagic.mw.logic.WordsHunter;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;

class UITabWords extends JPanel {
    private JTextField tfWord;
    private JTextPane tpResult;
    private JSpinner sLength;
    private int maxLetters = 4;

    private static IndexesLangInput indexes;
    static {
        indexes = new IndexesLangInput();
    }

    // Формирование вкладки "Поиск слов" интерфейса
    UITabWords() {
        this.setLayout(new GridBagLayout());

        // Текстовое поле для ввода набора исходных символов
        tfWord = new JTextField(15);
        tfWord.setFont(new Font("Arial", Font.PLAIN, 25));
        tfWord.setHorizontalAlignment(JTextField.CENTER);
        AListener al = new AListener();
        tfWord.addActionListener(al); // слушатель на <ENTER>
        // Слушатель на ввод символов
        tfWord.getDocument().addDocumentListener(new DocListener());
        this.add(tfWord, new GridBagConstraints(0, 0,
                3, 1, 0.1, 0.01,
                10, 1, UIGeneral.ins, 0, 0));

        // Кнопка выполнения поиска слов
        JButton bRun = new JButton("Поиск слов");
        bRun.setFont(new Font("Arial", Font.PLAIN, 18));
        bRun.addActionListener(al); // слушатель на клик
        this.add(bRun, new GridBagConstraints(0, 1,
                3, 1, 0.1, 0.01,
                10, 1, UIGeneral.ins, 0, 0));

        // Текстовая панель для вывода результата поиска
        tpResult = new JTextPane();
        StyledDocument doc = tpResult.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
        tpResult.setFont(new Font("Arial", Font.PLAIN, 20));
        JScrollPane spResult = new JScrollPane(tpResult);
        this.add(spResult, new GridBagConstraints(0, 2,
                3, 1, 0.1, 1.0,
                10, 1, UIGeneral.ins, 0, 0));

        // Текстовая строка
        JLabel lMinLength = new JLabel("Длина слов - минимум");
        lMinLength.setFont(new Font("Arial", Font.PLAIN, 16));
        lMinLength.setHorizontalAlignment(JTextField.RIGHT);
        this.add(lMinLength, new GridBagConstraints(0, 3,
                1, 1, 0.9, 0.01,
                10, 1, UIGeneral.ins, 0, 0));

        // Спинер для установки минимальной длины искомых слов
        sLength = new JSpinner(new SpinnerNumberModel(
                maxLetters, 2, 24, 1));
        sLength.setFont(new Font("Arial", Font.PLAIN, 16));
        sLength.addChangeListener(new ChListener()); // слушатель на изменение
        this.add(sLength, new GridBagConstraints(1, 3,
                1, 1, 0.1, 0.01,
                10, 1, UIGeneral.ins, 0, 0));

        // Текстовая строка
        JLabel lLetters = new JLabel("буквы");
        lLetters.setFont(new Font("Arial", Font.PLAIN, 16));
        lLetters.setHorizontalAlignment(JTextField.LEFT);
        this.add(lLetters, new GridBagConstraints(2, 3,
                1, 1, 0.4, 0.01,
                10, 1, UIGeneral.ins, 0, 0));
    }

    /**
     * Метод, устанавливающий фокус в текствое поле ввода
     */
    void tfWordFocus() {
        tfWord.requestFocus();
    }

    /**
     * Метод, обращающийся к логическому блоку с целью получить искомые слова.
     * Получает слова и помещает их в текстовую панель
     */
    private void hunting() {
        WordsHunter wordsHunter = new WordsHunter(UIGeneral.getRepository());
        String result = wordsHunter.getSubWords(maxLetters,
                tfWord.getText().toLowerCase());
        tpResult.setText(result);
        tpResult.setCaretPosition(0);
    }

    /**
     * Внутренний класс - слушатель клика кнопки
     * и нажатия <ENTER> в текстовом поле
     */
    class AListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            hunting();
        }
    }

    /**
     * Внутренний класс - слушатель изменения
     * минимальной длины искомых слов
     */
    class ChListener implements ChangeListener {
        public void stateChanged(ChangeEvent e) {
            maxLetters = (int)sLength.getValue();
            hunting();
        }
    }

    /**
     * Внутренний класс - слушатель изменений в тексте
     * текстового поля.
     * Заменяет английские символы на русские при вводе.
     */
    class DocListener implements DocumentListener {
        @Override
        public void insertUpdate(DocumentEvent e) {
            try {
                textFilterToRussian(e);
            } catch (BadLocationException e1) { e1.printStackTrace(); }
        }

        @Override
        public void removeUpdate(DocumentEvent e) {

        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            try {
                textFilterToRussian(e);
            } catch (BadLocationException e1) { e1.printStackTrace(); }
        }
    }

    /**
     * Внутренний enum-класс - для сопоставления английских и русских символов
     */
    enum Alphabet {
        ENG ("QWERTYUIOP{}ASDFGHJKL:\"ZXCVBNM<>qwertyuiop[]asdfghjkl;'zxcvbnm,.");
        String type;
        static String CORE =
                "ЙЦУКЕНГШЩЗХЪФЫВАПРОЛДЖЭЯЧСМИТЬБЮйцукенгшщзхъфывапролджэячсмитьбю";
        Alphabet(String type) {
            this.type = type;
        }
    }

    /**
     * Метод, осуществляющий замену английских символов на русские.
     * @param e - событие DocumentEvent
     */
    private void textFilterToRussian(DocumentEvent e) throws BadLocationException {
        char[] temp = e.getDocument().getText(e.getOffset(), e.getLength())
                .toCharArray();
        int tempIndex;

        for (int i = 0; i < temp.length; i++)
            for (Alphabet alphabet : Alphabet.values()) {
                if ((tempIndex = alphabet.type.indexOf(temp[i])) != -1) {
                    temp[i] = Alphabet.CORE.charAt(tempIndex);
                    break;
                }
            }
        if (!indexes.isEqual(e.getOffset(), e.getLength(), new String(temp))) {
            SwingUtilities.invokeLater(() -> {
                try {
                    e.getDocument().remove(e.getOffset(), e.getLength());
                    e.getDocument().insertString(e.getOffset(), new String(temp), null);
                } catch (BadLocationException e1) { e1.printStackTrace(); }
            });
        }
    }
}
