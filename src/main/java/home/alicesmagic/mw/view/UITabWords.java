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
    private SimpleAttributeSet selectOld;
    private SimpleAttributeSet selectNew;
    private int positionSelected = 0;
    private int lengthSelected = 0;
    private PopupMenu pMenu;

    // Формирование вкладки "Поиск слов" интерфейса
    UITabWords() {
        this.setLayout(new MigLayout());
        ToolTips toolTips = new ToolTips();
        pMenu = new PopupMenu();
        pMenu.getGoogle().addActionListener(new SearchListener());
        pMenu.getYandex().addActionListener(new SearchListener());
        pMenu.getWiki().addActionListener(new SearchListener());
        pMenu.getWidi().addActionListener(new SearchListener());
        pMenu.getDel().addActionListener(new DelListener());

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
        tpResult.addMouseListener(new ClickListener());
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

        // Панель объединяющая три предыдущих элемента
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

        // Установка атрибутов для выделения найденных слов
        selectOld = new SimpleAttributeSet();
        StyleConstants.setFontSize(selectOld, 20);
        StyleConstants.setForeground(selectOld, Color.lightGray);
        selectNew = new SimpleAttributeSet();
        StyleConstants.setFontSize(selectNew, 25);
        StyleConstants.setForeground(selectNew, Color.white);
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
        tpResult.getStyledDocument().setCharacterAttributes(
                0, result.length(), selectOld, false);
        tpResult.setComponentPopupMenu(null);
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

    /**
     * Внутренний класс - слушатель мыши в текстовой панели.
     * Устанавливает позицию слова по клику мыши, вычисляет его длину и
     * подсвечивает это слово.
     */
    class ClickListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            String s = tpResult.getText();
            if (!s.isEmpty()) {
                tpResult.getStyledDocument().setCharacterAttributes(
                        positionSelected, lengthSelected, selectOld, false);
                tpResult.setComponentPopupMenu(null);
                if (tpResult.getCaretPosition() <= s.indexOf('\n')) {
                    tpResult.setCaretPosition(0);
                } else {
                    while (s.charAt(tpResult.getCaretPosition() - 1) != '\n') {
                        tpResult.setCaretPosition(tpResult.getCaretPosition() - 1);
                    }
                }
                positionSelected = tpResult.getCaretPosition();
                int temp = positionSelected;
                while (s.charAt(temp) != '\n') {
                    temp++;
                }
                lengthSelected = temp - positionSelected;
                if (s.charAt(positionSelected) != '-') {
                    tpResult.getStyledDocument().setCharacterAttributes(
                            positionSelected, lengthSelected, selectNew, false);
                    tpResult.setComponentPopupMenu(pMenu.getPopupMenu());
                }
            }
        }
    }


    /**
     * Внутренний класс - слушатель клика кнопки "Показать весь словарь".
     * Запускает отдельный поток в суб-классе SwingWorker для загрузки словаря.
     */
    public class SearchListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String w = tpResult.getText().substring(positionSelected,
                    positionSelected + lengthSelected);
            switch (e.getActionCommand()) {
                case "Google": pMenu.searchNet(w, "g"); break;
                case "Яндекс": pMenu.searchNet(w, "y"); break;
                case "Википедия": pMenu.searchNet(w, "w"); break;
                case "Викисловарь": pMenu.searchNet(w, "d"); break;
            }
        }
    }

    /**
     * Внутренний класс - слушатель клика кнопки "Показать весь словарь".
     * Запускает отдельный поток в суб-классе SwingWorker для загрузки словаря.
     */
    public class DelListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String w = tpResult.getText().substring(positionSelected,
                    positionSelected + lengthSelected);
            if (UIGeneral.getRepository().getAll().contains(w)) {
                UIGeneral.getRepository().removeWord(w);
                UIGeneral.getRepository().saveDictionary();
                // Удаление слова из текстовой панели
                try {
                    tpResult.getDocument().remove(tpResult
                            .getCaretPosition(), lengthSelected + 1);
                } catch (BadLocationException ex) {
                    ex.printStackTrace();
                }
                tpResult.getStyledDocument().setCharacterAttributes(
                        positionSelected, lengthSelected, selectOld, false);
                tpResult.setComponentPopupMenu(null);
            }
        }
    }
}
