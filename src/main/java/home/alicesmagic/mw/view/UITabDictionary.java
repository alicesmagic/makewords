package home.alicesmagic.mw.view;

import home.alicesmagic.mw.logic.CorrectTermination;

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

    UITabDictionary() {
        this.setLayout(new GridBagLayout());

        tfWord = new JTextField(15);
        tfWord.setFont(new Font("Arial", Font.PLAIN, 20));
        tfWord.setHorizontalAlignment(JTextField.CENTER);
        // Слушатель на ввод символов
        tfWord.getDocument().addDocumentListener(new UIGeneral.DocListener());
        // Слушатель нажатия клавиш
        tfWord.addKeyListener(new KeyWordL());
        this.add(tfWord, new GridBagConstraints(0, 0,
                1, 1, 0.1, 0.01,
                10, 1, UIGeneral.ins, 0, 0));

        bAdd = new JButton("Добавить слово");
        bAdd.setFont(new Font("Arial", Font.PLAIN, 18));
        bAdd.addActionListener(new AddListener()); // слушатель на клик
        this.add(bAdd, new GridBagConstraints(0, 1,
                1, 1, 0.1, 0.01,
                10, 1, UIGeneral.ins, 0, 0));

        tpDictionary = new JTextPane();
        StyledDocument doc = tpDictionary.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
        tpDictionary.setFont(new Font("Arial", Font.PLAIN, 20));
        tpDictionary.setEditable(false);
        JScrollPane spDictionary = new JScrollPane(tpDictionary);
        this.add(spDictionary, new GridBagConstraints(0, 2,
                1, 1, 0.1, 1.0,
                10, 1, UIGeneral.ins, 0, 0));

        lNumber = new JLabel("В словаре " + new CorrectTermination(
                "слово", "слова", "слов").getNumAndWord(number));

        lNumber.setFont(new Font("Arial", Font.PLAIN, 16));
        lNumber.setHorizontalAlignment(JTextField.CENTER);
        this.add(lNumber, new GridBagConstraints(0, 3,
                1, 1, 0.1, 0.01,
                10, 1, UIGeneral.ins, 0, 0));

        pBar = new JProgressBar();
        pBar.setStringPainted(true);
        pBar.setMinimum(0);
        pBar.setMaximum(100);
        pBar.setFont(new Font("Arial", Font.PLAIN, 14));
        this.add(pBar, new GridBagConstraints(0, 4,
                1, 1, 0.1, 0.01,
                10, 1, UIGeneral.ins, 0, 0));

        bShow = new JButton("Показать весь словарь");
        bShow.setFont(new Font("Arial", Font.PLAIN, 18));
        bShow.addActionListener(new ShowListener()); // слушатель на клик
        this.add(bShow, new GridBagConstraints(0, 5,
                1, 1, 0.1, 0.01,
                10, 1, UIGeneral.ins, 0, 0));

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
     * Внутренний класс - слушатель изменений в текстовом поле tfWord
     */
    class KeyWordL extends KeyAdapter {
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
     * Внутренний класс - слушатель клика кнопки "Добавить слово"
     */
    class AddListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String s = tfWord.getText().toLowerCase();
            if (!UIGeneral.getRepository().getAll().contains(s)) {
                UIGeneral.getRepository().addWord(s);
                UIGeneral.getRepository().saveDictionary();
                if (s.length() < 13) {
                    bAdd.setText("Добавлено слово '" + s + "'");
                } else {
                    bAdd.setText("Слово добавлено");
                }
                number++;
                lNumber.setText("В словаре " + new CorrectTermination(
                        "слово", "слова", "слов").getNumAndWord(number));
            } else {
                bAdd.setText("Такое слово уже есть");
            }
        }
    }

    /**
     * Внутренний класс - слушатель клика кнопки "Показать весь словарь"
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
     * Класс - наследник SwingWorker для загрузки словаря в текстовую панель
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
            bShow.setText("Словарь загружен");
            tfWord.requestFocus();
        }
    }
}
