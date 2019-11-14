package home.alicesmagic.mw.view;

import home.alicesmagic.mw.logic.CorrectTermination;
import home.alicesmagic.mw.logic.WordsHunter;
import net.miginfocom.swing.MigLayout;

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
        this.setLayout(new MigLayout());
        ToolTips toolTips = new ToolTips();

        // Текстовое поле для ввода исходных символов
        tfLetters = new JTextField(10) {
            @Override
            public Point getToolTipLocation(MouseEvent e) {
                return new Point(16, 50);
            }
        };
        tfLetters.setFont(new Font("Arial", Font.PLAIN, 25));
        tfLetters.setHorizontalAlignment(JTextField.CENTER);
        tfLettersBRunListener al = new tfLettersBRunListener();
        tfLetters.addActionListener(al); // слушатель на <ENTER>
        // Слушатель на ввод символов
        tfLetters.getDocument().addDocumentListener(new UIGeneral.DocListener());
        tfLetters.addKeyListener(new KeyTfLettersListener());
        tfLetters.setToolTipText(toolTips.forTfLetters);
        this.add(tfLetters, "span, growx");

        // Кнопка выполнения поиска слов
        bRun = new JButton("Поиск слов") {
            @Override
            public Point getToolTipLocation(MouseEvent e) {
                return new Point(19, 35);
            }
        };
        bRun.setFont(new Font("Arial", Font.PLAIN, 18));
        bRun.addActionListener(al); // слушатель на клик
        bRun.setToolTipText(toolTips.forBRun);
        this.add(bRun, "span, growx");

        // Текстовая панель для вывода результата поиска
        tpResult = new JTextPane();
        StyledDocument doc = tpResult.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
        tpResult.setFont(new Font("Arial", Font.PLAIN, 20));
        tpResult.setEditable(false);
        JScrollPane spResult = new JScrollPane(tpResult);
        this.add(spResult, "span, push, grow");

        // Текстовая строка "Длина слов - минимум"
        JLabel lMinLength = new JLabel("Длина слов - минимум");
        lMinLength.setFont(new Font("Arial", Font.PLAIN, 16));
        lMinLength.setHorizontalAlignment(JTextField.RIGHT);

        // Спинер для установки минимальной длины искомых слов
        sLength = new JSpinner(new SpinnerNumberModel(
                maxLetters, 2, 24, 1));
        sLength.setFont(new Font("Arial", Font.PLAIN, 16));
        sLength.addChangeListener(new sLengthListener()); // слушатель на изменение

        // Текстовая строка "букв(-а;-ы)"
        lLetters = new JLabel(new CorrectTermination(
                "буква", "буквы", "букв").getWord(maxLetters));
        lLetters.setFont(new Font("Arial", Font.PLAIN, 16));
        lLetters.setHorizontalAlignment(JTextField.LEFT);

        JPanel panSpin = new JPanel() {
            @Override
            public Point getToolTipLocation(MouseEvent e) {
                return new Point(5, -80);
            }
        };
        panSpin.add(lMinLength);
        panSpin.add(sLength);
        panSpin.add(lLetters);
        panSpin.setToolTipText(toolTips.forPanSpin);
        this.add(panSpin, "gapleft 10");
    }

    /**
     * Метод устанавливает фокус в текствое поле ввода
     */
    void tfLettersFocus() {
        tfLetters.requestFocus();
    }

    /**
     * Внутренний класс - слушатель изменений в текстовом поле tfLetters
     * Служит только для возврата изначального текста кнопки "Поиск слов"
     */
    class KeyTfLettersListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            bRun.setText("Поиск слов");
        }
    }

    /**
     * Метод, обращающийся к логическому блоку с целью получить искомые слова.
     * Получает слова и помещает их в текстовую панель
     */
    private void getResult() {
        WordsHunter wordsHunter = new WordsHunter(UIGeneral.getRepository());
        String result = wordsHunter.getSubWords(maxLetters,
                tfLetters.getText().toLowerCase());
        int num = wordsHunter.getNumberWords();
        if (wordsHunter.getNumberWords() > 0) {
            bRun.setText("Найдено " + new CorrectTermination(
                    "слово", "слова", "слов").getNumAndWord(num));
        } else {
            bRun.setText("Ничего не найдено...");
        }
        tpResult.setText(result);
        tpResult.setCaretPosition(0);
    }

    /**
     * Внутренний класс - слушатель клика кнопки "Поиск слов"
     * и нажатия <ENTER> в текстовом поле tfLetters
     * Вызвывает метод getResult()
     */
    class tfLettersBRunListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            getResult();
        }
    }

    /**
     * Внутренний класс - слушатель изменения
     * минимальной длины искомых слов в спиннере sLength
     */
    class sLengthListener implements ChangeListener {
        public void stateChanged(ChangeEvent e) {
            maxLetters = (int) sLength.getValue();
            lLetters.setText(new CorrectTermination(
                    "буква", "буквы", "букв").getWord(maxLetters));
            getResult();
        }
    }
}
