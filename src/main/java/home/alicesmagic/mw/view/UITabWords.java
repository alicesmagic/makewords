package home.alicesmagic.mw.view;

import home.alicesmagic.mw.logic.WordsHunter;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;

class UITabWords extends JPanel {
    private JTextField tfWord;
    private JButton bRun;
    private JTextPane tpResult;
    private JSpinner sLength;
    private int maxLetters = 4;

    UITabWords() {
        this.setLayout(new GridBagLayout());
        AListener al = new AListener();

        tfWord = new JTextField(15);
        tfWord.setFont(new Font("Arial", Font.PLAIN, 25));
        tfWord.setHorizontalAlignment(JTextField.CENTER);
        tfWord.addActionListener(al);
        tfWord.addKeyListener(new KListener());
        this.add(tfWord, new GridBagConstraints(0, 0,
                3, 1, 0.1, 0.01,
                10, 1, UIGeneral.ins, 0, 0));

        bRun = new JButton("Поиск слов");
        bRun.setFont(new Font("Arial", Font.PLAIN, 18));
        bRun.addActionListener(al);
        this.add(bRun, new GridBagConstraints(0, 1,
                3, 1, 0.1, 0.01,
                10, 1, UIGeneral.ins, 0, 0));

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

        JLabel lMinLength = new JLabel("Длина слов - минимум");
        lMinLength.setFont(new Font("Arial", Font.PLAIN, 16));
        lMinLength.setHorizontalAlignment(JTextField.RIGHT);
        this.add(lMinLength, new GridBagConstraints(0, 3,
                1, 1, 0.9, 0.01,
                10, 1, UIGeneral.ins, 0, 0));

        sLength = new JSpinner(new SpinnerNumberModel(
                maxLetters, 2, 24, 1));
        sLength.setFont(new Font("Arial", Font.PLAIN, 16));
        sLength.addChangeListener(new ChListener());
        this.add(sLength, new GridBagConstraints(1, 3,
                1, 1, 0.1, 0.01,
                10, 1, UIGeneral.ins, 0, 0));

        JLabel lLetters = new JLabel("буквы");
        lLetters.setFont(new Font("Arial", Font.PLAIN, 16));
        lLetters.setHorizontalAlignment(JTextField.LEFT);
        this.add(lLetters, new GridBagConstraints(2, 3,
                1, 1, 0.4, 0.01,
                10, 1, UIGeneral.ins, 0, 0));
    }

    private void hunting() {
        WordsHunter wordsHunter = new WordsHunter(UIGeneral.getRepository());
        String result = wordsHunter.getSubWords(maxLetters,
                tfWord.getText().toLowerCase());
        tpResult.setText(result);
        tpResult.setCaretPosition(0);
        bRun.requestFocus();
    }

    class AListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            hunting();
        }
    }

    class ChListener implements ChangeListener {
        public void stateChanged(ChangeEvent e) {
            maxLetters = (int)sLength.getValue();
            hunting();
        }
    }

    class KListener extends KeyAdapter {
        final String eng = "QWERTYUIOP{}ASDFGHJKL:\"ZXCVBNM<>qwertyuiop[]asdfghjkl;'zxcvbnm,.";
        final String rus = "ЙЦУКЕНГШЩЗХЪФЫВАПРОЛДЖЭЯЧСМИТЬБЮйцукенгшщзхъфывапролджэячсмитьбю";
        @Override
        public void keyReleased(KeyEvent e) {
            if (tfWord.getText().isEmpty()) return;
            char ch = tfWord.getText().toLowerCase().charAt(tfWord.getText().length() - 1);
            if (eng.indexOf(ch) != -1) {
                ch = rus.charAt(eng.indexOf(ch));
            }
            tfWord.setText(tfWord.getText().substring(0, tfWord.getText().length() - 1) + ch);
        }
    }
}
