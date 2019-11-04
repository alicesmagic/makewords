package home.alicesmagic.mw.view;

import home.alicesmagic.mw.logic.WordsHunter;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;

class UITabWords extends JPanel {
    private JTextField tfLetters;
    private JButton bRun;
    private JTextPane tpResult;
    private JSpinner sLength;
    private JLabel lLetters;
    private int maxLetters = 4;

    // Формирование вкладки "Поиск слов" интерфейса
    UITabWords() {
        this.setLayout(new GridBagLayout());

        // Текстовое поле для ввода набора исходных символов
        tfLetters = new JTextField(15);
        tfLetters.setFont(new Font("Arial", Font.PLAIN, 25));
        tfLetters.setHorizontalAlignment(JTextField.CENTER);
        SearchListener al = new SearchListener();
        tfLetters.addActionListener(al); // слушатель на <ENTER>
        // Слушатель на ввод символов
        tfLetters.getDocument().addDocumentListener(new UIGeneral.DocListener());
        tfLetters.addKeyListener(new KeySearchL());
        this.add(tfLetters, new GridBagConstraints(0, 0,
                3, 1, 0.1, 0.01,
                10, 1, UIGeneral.ins, 0, 0));

        // Кнопка выполнения поиска слов
        bRun = new JButton("Поиск слов");
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
        tpResult.setEditable(false);
        JScrollPane spResult = new JScrollPane(tpResult);
        this.add(spResult, new GridBagConstraints(0, 2,
                3, 1, 0.1, 1.0,
                10, 1, UIGeneral.ins, 0, 0));

        // Текстовая строка "Длина слов - минимум"
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
        sLength.addChangeListener(new ChangeL()); // слушатель на изменение
        this.add(sLength, new GridBagConstraints(1, 3,
                1, 1, 0.1, 0.01,
                10, 1, UIGeneral.ins, 0, 0));

        // Текстовая строка "букв(-а;-ы)"
        lLetters = new JLabel(mimicry(maxLetters, "letters"));
        lLetters.setFont(new Font("Arial", Font.PLAIN, 16));
        lLetters.setHorizontalAlignment(JTextField.LEFT);
        this.add(lLetters, new GridBagConstraints(2, 3,
                1, 1, 0.4, 0.01,
                10, 1, UIGeneral.ins, 0, 0));
    }

    /**
     * Метод устанавливает фокус в текствое поле ввода
     */
    void tfLettersFocus() {
        tfLetters.requestFocus();
    }

    /**
     * Метод устанавливает правильную форму слова
     * во множественном числе
     */
    private String mimicry(int n, String word) {
        switch (word) {
            case "letters":
                if (n > 1 && n < 5 || n > 21 && n < 25) return "буквы";
                else if (n == 21) return "буква ";
                return "букв   ";
            case "words":
                int[] arr = new int[2];
                arr[1] = n % 10;
                n /= 10;
                arr[0] = n % 10;
                if (arr[0] != 1 && arr[1] > 1 && arr[1] < 5) return "слова";
                else if (arr[1] == 1) return "слово";
                return "слов";
            default: return word;
        }
    }

    /**
     * Внутренний класс - слушатель изменений в текстовом поле tfWord
     */
    class KeySearchL extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            bRun.setText("Поиск слов");
        }
    }

    /**
     * Метод, обращающийся к логическому блоку с целью получить искомые слова.
     * Получает слова и помещает их в текстовую панель
     */
    private void hunting() {
        WordsHunter wordsHunter = new WordsHunter(UIGeneral.getRepository());
        String result = wordsHunter.getSubWords(maxLetters,
                tfLetters.getText().toLowerCase());
        int num = wordsHunter.getNumberWords();
        if (wordsHunter.getNumberWords() > 0) {
            bRun.setText("Найдено " + num + " " + mimicry(num, "words"));
        } else {
            bRun.setText("Ничего не найдено...");
        }
        tpResult.setText(result);
        tpResult.setCaretPosition(0);
    }

    /**
     * Внутренний класс - слушатель клика кнопки "Поиск слов"
     * и нажатия <ENTER> в текстовом поле tfLetters
     */
    class SearchListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            hunting();
        }
    }

    /**
     * Внутренний класс - слушатель изменения
     * минимальной длины искомых слов в спиннере sLength
     */
    class ChangeL implements ChangeListener {
        public void stateChanged(ChangeEvent e) {
            maxLetters = (int) sLength.getValue();
            lLetters.setText(mimicry(maxLetters, "letters"));
            hunting();
        }
    }
}
