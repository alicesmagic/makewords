package home.alicesmagic.mw.view;

import home.alicesmagic.mw.logic.CorrectTermination;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

class UITabDictionary extends JPanel {
    private int number = UIGeneral.getRepository().getAll().size();
    private JTextField tfWord;
    private JButton bAdd;
    private JTextPane tpDictionary;
    private JLabel lNumber;
    private JProgressBar pBar;
    private JButton bShow;
    private SimpleAttributeSet selectOld;
    private SimpleAttributeSet selectNew;
    private int positionSelected = 0;
    private int lengthSelected = 0;

    // Формирование вкладки "Словарь" интерфейса
    UITabDictionary() {
        this.setLayout(new MigLayout());

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
        tfWord.setToolTipText(
                "<html><center>При вводе символов в это поле, будет осуществляться<br>" +
                        "поиск подходящего слова в словаре. Если ваше слово<br>" +
                        "в словаре отсутствует, вы сможете его добавить,<br>" +
                        "нажав кнопку 'Добавить слово'");
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
        bAdd.setToolTipText(
                "<html><center>Эта кнопка поможет вам добавить слово в словарь,<br>" +
                        "только если это слово в словаре отсутствует.<br>" +
                        "Пожалуйста, добавляйте слово, будучи абсолютно<br>" +
                        "уверенным в правильности его написания.");
        this.add(bAdd, "span, growx");

        // Текстовая панель словаря
        tpDictionary = new JTextPane();
        StyledDocument doc = tpDictionary.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
        tpDictionary.setFont(new Font("Arial", Font.PLAIN, 20));
        tpDictionary.setEditable(false);
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
        bShow.setToolTipText(
                "<html><center>Словарь загружен в скрытом режиме.<br>" +
                "Чтобы его увидеть, нужно нажать на эту кнопку.<br>" +
                "Загрузка займет определенное время.<br>" +
                "Придется немного подождать.");
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
            String s = tpDictionary.getText();
            String w = UIGeneral.getRepository().findBySub(
                    tfWord.getText().toLowerCase());
            if (w != null) {
                positionSelected = s.indexOf("\n" + w + "\n") + 1;
                lengthSelected = w.length();
                tpDictionary.setCaretPosition(positionSelected);
                tpDictionary.getStyledDocument().setCharacterAttributes(
                        positionSelected, lengthSelected, selectNew, false);
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
            tpDictionary.setCaretPosition(0);
            pBar.setValue(100);
            bShow.setText("Словарь показан");
            bShow.setEnabled(false);
            pBar.setEnabled(false);
            tfWord.requestFocus();
        }
    }
}
