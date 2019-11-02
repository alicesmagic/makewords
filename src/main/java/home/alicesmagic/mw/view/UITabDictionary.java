package home.alicesmagic.mw.view;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class UITabDictionary extends JPanel {
    private JTextField tfSearch;
    private JTextPane tpDictionary;
    private JProgressBar pBar;
    private JButton bShow;

    UITabDictionary() {
        this.setLayout(new GridBagLayout());

        tfSearch = new JTextField(15);
        tfSearch.setFont(new Font("Arial", Font.PLAIN, 20));
        tfSearch.setHorizontalAlignment(JTextField.CENTER);
        // Слушатель на ввод символов
        tfSearch.getDocument().addDocumentListener(new UIGeneral.DocListener());
        this.add(tfSearch, new GridBagConstraints(0, 0,
                1, 1, 0.1, 0.01,
                10, 1, UIGeneral.ins, 0, 0));

        JButton bAdd = new JButton("Добавить в словарь");
        bAdd.setFont(new Font("Arial", Font.PLAIN, 18));
        this.add(bAdd, new GridBagConstraints(0, 1,
                1, 1, 0.1, 0.01,
                10, 1, UIGeneral.ins, 0, 0));

        tpDictionary = new JTextPane();
        StyledDocument doc = tpDictionary.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
        tpDictionary.setFont(new Font("Arial", Font.PLAIN, 20));
        JScrollPane spDictionary = new JScrollPane(tpDictionary);
        this.add(spDictionary, new GridBagConstraints(0, 2,
                1, 1, 0.1, 1.0,
                10, 1, UIGeneral.ins, 0, 0));

        JLabel lQuantity = new JLabel("Количество слов в словаре: "
                + UIGeneral.getRepository().getAll().size());
        lQuantity.setFont(new Font("Arial", Font.PLAIN, 16));
        lQuantity.setHorizontalAlignment(JTextField.CENTER);
        this.add(lQuantity, new GridBagConstraints(0, 3,
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
    }

    /**
     * Метод, устанавливающий фокус в текствое поле ввода
     */
    void tfSearchFocus() {
        tfSearch.requestFocus();
    }

    /**
     * Внутренний класс - слушатель клика кнопки "Показать весь словарь"
     */
    class ShowListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (bShow.getText().equals("Показать весь словарь")) {
                new LoadThread().start();
            }
        }
    }

    /**
     * Класс отдельного потока загрузки словаря в текстовую панель
     */
    class LoadThread extends Thread {
        public void run() {
            bShow.setText("Словарь загружается");
            int count = 0;
            StringBuilder sb = new StringBuilder();
            for (String s : UIGeneral.getRepository().getAll()) {
                try {
                    if (count % 20 == 0) sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                sb.append(s).append("\n");
                int finalCount = count++;
                SwingUtilities.invokeLater(() -> pBar.setValue(finalCount
                        * 100 / UIGeneral.getRepository().getAll().size()));
            }
            bShow.setText("Формирование списка...");
            SwingUtilities.invokeLater(() -> {
                tpDictionary.setText(sb.toString());
                tpDictionary.setCaretPosition(0);
                bShow.setText("Словарь загружен");
                pBar.setValue(100);
            });
        }
    }
}
