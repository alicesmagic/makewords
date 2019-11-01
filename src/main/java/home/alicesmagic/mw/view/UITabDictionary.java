package home.alicesmagic.mw.view;

import home.alicesmagic.mw.model.repository.WordsRepository;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class UITabDictionary extends JPanel {
    private JTextField tfSearch;
    private JTextPane tpDictionary;
    private JProgressBar pBar;

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
//        bAdd.addActionListener(new ShowListener()); // слушатель на клик
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
        this.add(pBar, new GridBagConstraints(0, 4,
                1, 1, 0.1, 0.01,
                10, 1, UIGeneral.ins, 0, 0));

        JButton bShow = new JButton("Показать весь словарь");
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
            showDictionary(UIGeneral.getRepository());
            tpDictionary.setCaretPosition(0);
        }
    }

    /**
     * Метод загружает словарь в текстовую панель
     */
    private void showDictionary(WordsRepository repository) {
        new Thread(() -> {
            int count = 0;
            for (String s : repository.getAll()) {
                try {
                    tpDictionary.getDocument().insertString(tpDictionary
                            .getDocument().getLength(), s + "\n", null);

                } catch (BadLocationException e) {
                    e.printStackTrace();
                }
                count++;
                int finalCount = count;
                SwingUtilities.invokeLater(() -> {
                    pBar.setValue(finalCount * 100 / repository.getAll().size());
                    repaint();
                });
            }
        }).start();
    }
}
