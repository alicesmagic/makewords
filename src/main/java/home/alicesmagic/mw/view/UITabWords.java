package home.alicesmagic.mw.view;

import home.alicesmagic.mw.logic.WordsListener;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;

public class UITabWords extends JPanel {
    private static JTextPane tpResult;
    private static JSpinner sLength;
    private static int maxLetters = 4;

    UITabWords() {
        this.setLayout(new GridBagLayout());

        JTextField tfWord = new JTextField(15);
        tfWord.setFont(new Font("Arial", Font.PLAIN, 25));
        tfWord.setHorizontalAlignment(JTextField.CENTER);
        tfWord.addActionListener(new WordsListener());
        this.add(tfWord, new GridBagConstraints(0, 0,
                3, 1, 0.1, 0.01,
                10, 1, UIGeneral.ins, 0, 0));

        JButton bRun = new JButton("Поиск слов");
        bRun.setFont(new Font("Arial", Font.PLAIN, 18));
        bRun.addActionListener(new WordsListener());
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
                maxLetters, 2, 20, 1));
        sLength.setFont(new Font("Arial", Font.PLAIN, 16));
        sLength.addChangeListener(new WordsListener());
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

    public static JTextPane getTpResult() {
        return tpResult;
    }

    public static JSpinner getsLength() {
        return sLength;
    }

    public static int getMaxLetters() {
        return maxLetters;
    }

    public static void setMaxLetters(int maxLetters) {
        UITabWords.maxLetters = maxLetters;
    }

}
