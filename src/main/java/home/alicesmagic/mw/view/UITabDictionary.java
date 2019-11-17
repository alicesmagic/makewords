package home.alicesmagic.mw.view;

import home.alicesmagic.mw.logic.CorrectTermination;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

class UITabDictionary extends JPanel {
    private JTextField tfWord;
    private int number = UIGeneral.getRepository().getAll().size();
    private JButton bAdd;
    private JTextPane tpDictionary;
    private JLabel lNumber;
    private JProgressBar pBar;
    private JButton bShow;
    private SimpleAttributeSet selectOld;
    private SimpleAttributeSet selectNew;
    private int positionSelected = 0;
    private int lengthSelected = 0;
    private PopupMenu pMenu;

    // Формирование вкладки "Словарь" интерфейса
    UITabDictionary() {
        this.setLayout(new MigLayout());
        ToolTips toolTips = new ToolTips();
        pMenu = new PopupMenu();
        pMenu.getGoogle().addActionListener(new SearchListener());
        pMenu.getYandex().addActionListener(new SearchListener());
        pMenu.getWiki().addActionListener(new SearchListener());
        pMenu.getWidi().addActionListener(new SearchListener());
        pMenu.getDel().addActionListener(new DelListener());

        // Текстовое поле для ввода искомого слова в словаре
        tfWord = new JTextField(15) {
            @Override
            public Point getToolTipLocation(MouseEvent e) {
                return new Point(1, 40);
            }
        };
        tfWord.setFont(new Font("Arial", Font.PLAIN, 20));
        tfWord.setHorizontalAlignment(JTextField.CENTER);
        // Слушатель на ввод символов
        tfWord.getDocument().addDocumentListener(new UIGeneral.DocListener());
        // Слушатель нажатия клавиш
        tfWord.addKeyListener(new KeyTfWordListener());
        tfWord.setToolTipText(toolTips.forTfWord);
        this.add(tfWord, "span, growx");

        // Кнопка добавления набранного слова в словарь
        bAdd = new JButton("Добавить слово") {
            @Override
            public Point getToolTipLocation(MouseEvent e) {
                return new Point(5, 35);
            }
        };
        bAdd.setFont(new Font("Arial", Font.PLAIN, 18));
        bAdd.addActionListener(new bAddListener()); // слушатель на клик
        bAdd.setToolTipText(toolTips.forBAdd);
        this.add(bAdd, "span, growx");

        // Текстовая панель словаря
        tpDictionary = new JTextPane();
        StyledDocument doc = tpDictionary.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
        tpDictionary.setFont(new Font("Arial", Font.PLAIN, 20));
        tpDictionary.setEditable(false);
        tpDictionary.addMouseListener(new ClickListener());
        JScrollPane spDictionary = new JScrollPane(tpDictionary);
        this.add(spDictionary, "span, push, grow");

        // Декларация количества слов в словаре
        lNumber = new JLabel("В словаре " + new CorrectTermination(
                "слово", "слова", "слов").getNumAndWord(number));
        lNumber.setFont(new Font("Arial", Font.PLAIN, 16));
        lNumber.setHorizontalAlignment(JTextField.CENTER);
        this.add(lNumber, "span, growx");

        // Прогресс-бар загрузки словаря
        pBar = new JProgressBar();
        pBar.setStringPainted(true);
        pBar.setMinimum(0);
        pBar.setMaximum(100);
        pBar.setFont(new Font("Arial", Font.PLAIN, 14));
        this.add(pBar, "span, growx");

        // Кнопка загрузки словаря в текстовую панель
        bShow = new JButton("Показать весь словарь") {
            @Override
            public Point getToolTipLocation(MouseEvent e) {
                return new Point(15, -68);
            }
        };
        bShow.setFont(new Font("Arial", Font.PLAIN, 18));
        bShow.addActionListener(new ShowListener()); // слушатель на клик
        bShow.setToolTipText(toolTips.forBShow);
        this.add(bShow, "span, growx");

        // Установка атрибутов для выделения найденных слов
        selectOld = new SimpleAttributeSet();
        StyleConstants.setFontSize(selectOld, 20);
        StyleConstants.setForeground(selectOld, Color.lightGray);
        selectNew = new SimpleAttributeSet();
        StyleConstants.setFontSize(selectNew, 25);
        StyleConstants.setForeground(selectNew, Color.white);
    }

    /**
     * Метод, устанавливающий фокус в текствое поле ввода
     */
    void tfFocus() {
        tfWord.requestFocus();
    }

    /**
     * Внутренний класс - слушатель клика кнопки "Добавить слово".
     * Добавляет и выделяет цветом новое слово в словаре,
     * корректирует отображаемый размер словаря.
     */
    class bAddListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            tpDictionary.getStyledDocument().setCharacterAttributes(
                    positionSelected, lengthSelected, selectOld, false);
            tpDictionary.setComponentPopupMenu(null);
            // Добавление слова в репозиторий, если его там нет
            String s = tfWord.getText().toLowerCase();
            if (s.isEmpty()) return;
            if (UIGeneral.getRepository().getAll().contains(s)) {
                bAdd.setText("Такое слово уже есть");
            } else {
                UIGeneral.getRepository().addWord(s);
                UIGeneral.getRepository().saveDictionary();
                if (s.length() < 13) {
                    bAdd.setText("Добавлено слово '" + s + "'");
                } else {
                    bAdd.setText("Слово добавлено");
                }
                // Вставка слова в текстовую панель и подсветка этого слова
                try {
                    tpDictionary.getDocument().insertString(tpDictionary
                            .getCaretPosition(), s + "\n", null);
                } catch (BadLocationException ex) {
                    ex.printStackTrace();
                }
                lengthSelected = s.length();
                tpDictionary.getStyledDocument().setCharacterAttributes(
                        positionSelected, lengthSelected, selectNew, false);
                tpDictionary.setComponentPopupMenu(pMenu.getPopupMenu());
                // Коррекция отображаемого размера словаря
                number++;
                lNumber.setText("В словаре " + new CorrectTermination(
                        "слово", "слова", "слов")
                        .getNumAndWord(number));
            }
        }
    }

    /**
     * Внутренний класс - слушатель изменений в текстовом поле tfWord
     * Возвращает изначальный текст кнопки "Добавить слово". Находит совпадения
     * в отображаемом словаре и подсвечивает подходящее слово, устанавливая при
     * этом каретку в нужное положение.
     */
    class KeyTfWordListener extends KeyAdapter {
        @Override
        public void keyReleased(KeyEvent e) {
            bAdd.setText("Добавить слово");
            tpDictionary.getStyledDocument().setCharacterAttributes(
                    positionSelected, lengthSelected, selectOld, false);
            tpDictionary.setComponentPopupMenu(null);
            String s = tpDictionary.getText();
            String w = UIGeneral.getRepository().findBySub(
                    tfWord.getText().toLowerCase());
            if (w != null) {
                positionSelected = s.indexOf("\n" + w + "\n") + 1;
                lengthSelected = w.length();
                tpDictionary.setCaretPosition(positionSelected);
                tpDictionary.getStyledDocument().setCharacterAttributes(
                        positionSelected, lengthSelected, selectNew, false);
                tpDictionary.setComponentPopupMenu(pMenu.getPopupMenu());
            }
        }
    }

    /**
     * Внутренний класс - слушатель клика кнопки "Показать весь словарь".
     * Запускает отдельный поток в суб-классе SwingWorker для загрузки словаря.
     */
    class ShowListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (bShow.getText().equals("Показать весь словарь")) {
                bShow.setText("Словарь загружается...");
                new LoadsThread().execute();
            }
        }
    }

    /**
     * Суб-класс SwingWorker для загрузки словаря в текстовую панель
     * в отдельном потоке
     */
    class LoadsThread extends SwingWorker<String, Integer> {
        // Формирование строки для последующего ее вывода в текстовую панель
        // Выполняется в отдельном потоке
        @Override
        public String doInBackground() {
            int count = 0;
            StringBuilder sb = new StringBuilder();
            for (String s : UIGeneral.getRepository().getAll()) {
                // Притормаживание загрузки в эстетических целях
                try {
                    if (count % 20 == 0) {
                        Thread.sleep(1);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                sb.append(s).append("\n");
                publish(count++);
            }
            SwingUtilities.invokeLater(() -> bShow.setText("Формирование списка..."));
            return sb.toString();
        }

        // Отражение процесса загрузки в прогресс-баре
        // Выполняется в потоке диспетчеризации событий
        @Override
        public void process(List<Integer> chunks) {
            for (Integer chunk : chunks) {
                pBar.setValue(chunk * 100 / UIGeneral.getRepository()
                        .getAll().size());
            }
        }

        // Вывод итоговой строки в текстовую панель
        // Выполняется в потоке диспетчеризации событий
        @Override
        public void done() {
            try {
                tpDictionary.setText(get());
            } catch (Exception e) {
                e.printStackTrace();
            }
            tpDictionary.setCaretPosition(1);
            pBar.setValue(100);
            bShow.setText("Словарь показан");
            bShow.setEnabled(false);
            pBar.setEnabled(false);
            tpDictionary.setComponentPopupMenu(pMenu.getPopupMenu());
            tfWord.requestFocus();
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
            String s = tpDictionary.getText();
            if (!s.isEmpty()) {
                tpDictionary.getStyledDocument().setCharacterAttributes(
                        positionSelected, lengthSelected, selectOld, false);
                tpDictionary.setComponentPopupMenu(null);
                if (tpDictionary.getCaretPosition() <= s.indexOf('\n')) {
                    tpDictionary.setCaretPosition(0);
                } else {
                    while (s.charAt(tpDictionary.getCaretPosition() - 1) != '\n') {
                        tpDictionary.setCaretPosition(
                                tpDictionary.getCaretPosition() - 1);
                    }
                }
                positionSelected = tpDictionary.getCaretPosition();
                int temp = positionSelected;
                while (s.charAt(temp) != '\n') {
                    temp++;
                }
                lengthSelected = temp - positionSelected;
                tpDictionary.getStyledDocument().setCharacterAttributes(
                        positionSelected, lengthSelected, selectNew, false);
                tpDictionary.setComponentPopupMenu(pMenu.getPopupMenu());
            }
        }
    }

    /**
     * Внутренний класс - слушатель клика кнопки "Показать весь словарь".
     * Запускает отдельный поток в суб-классе SwingWorker для загрузки словаря.
     */
    public class SearchListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String w = tpDictionary.getText().substring(positionSelected,
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
            String w = tpDictionary.getText().substring(positionSelected,
                    positionSelected + lengthSelected);
            UIGeneral.getRepository().removeWord(w);
            UIGeneral.getRepository().saveDictionary();
            // Удаление слова из текстовой панели
            try {
                tpDictionary.getDocument().remove(tpDictionary
                        .getCaretPosition(), lengthSelected + 1);
            } catch (BadLocationException ex) {
                ex.printStackTrace();
            }
            tpDictionary.getStyledDocument().setCharacterAttributes(
                    positionSelected, lengthSelected, selectOld, false);
            tpDictionary.setComponentPopupMenu(null);
            tfWord.setText("");
            bAdd.setText("Слово удалено");
            // Коррекция отображаемого размера словаря
            number--;
            lNumber.setText("В словаре " + new CorrectTermination(
                    "слово", "слова", "слов")
                    .getNumAndWord(number));
        }
    }
}
