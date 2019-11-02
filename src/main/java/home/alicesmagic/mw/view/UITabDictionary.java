package home.alicesmagic.mw.view;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

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
                bShow.setText("Словарь загружается...");
                new LoadThread().execute();
            }
        }
    }

    /**
     * Класс - наследник SwingWorker для загрузки словаря в текстовую панель
     */
    class LoadThread extends SwingWorker<String, Integer> {
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
        // в потоке диспетчеризации событий
        @Override
        public void process(List<Integer> chunks) {
            for (Integer chunk : chunks) {
                pBar.setValue(chunk * 100 / UIGeneral.getRepository()
                        .getAll().size());
            }
        }

        // Вывод итоговой строки в текстовую панель
        // в потоке диспетчеризации событий
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
        }
    }
}
