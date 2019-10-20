import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;

class UIGeneral extends JFrame {
    UIGeneral() {
        super("Subworder");
//        this.setResizable(false);
//        this.setLocationRelativeTo(null);
        this.setLocation(815,150);
        this.setSize(320, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new GridBagLayout());
        int gap = 3;
        Insets ins = new Insets(gap, gap, gap, gap);

        JTextField tfWord = new JTextField("генератор", 15);
        tfWord.setFont(new Font("Arial", Font.PLAIN, 25));
        tfWord.setHorizontalAlignment(JTextField.CENTER);
        this.add(tfWord, new GridBagConstraints(0, 0,
                2, 1, 0.1, 0.05,
                10, 1, ins, 0, 0));

        JLabel lMinLength = new JLabel("Минимальная длина слов: ");
        lMinLength.setFont(new Font("Arial", Font.PLAIN, 16));
        lMinLength.setHorizontalAlignment(JTextField.RIGHT);
        this.add(lMinLength, new GridBagConstraints(0, 1,
                1, 1, 0.9, 0.02,
                10, 1, ins, 0, 0));

        JSpinner sLength = new JSpinner(new SpinnerNumberModel(
                4, 1, 20, 1));
        sLength.setFont(new Font("Arial", Font.PLAIN, 16));
        this.add(sLength, new GridBagConstraints(1, 1,
                1, 1, 0.1, 0.02,
                10, 1, ins, 0, 0));

        JButton bRun = new JButton("Поиск");
        bRun.setFont(new Font("Arial", Font.PLAIN, 18));
        this.add(bRun, new GridBagConstraints(0, 2,
                2, 1, 0.1, 0.05,
                10, 1, ins, 0, 0));

        JTextPane tpResult = new JTextPane();
        StyledDocument doc1 = tpResult.getStyledDocument();
        SimpleAttributeSet center1 = new SimpleAttributeSet();
        StyleConstants.setAlignment(center1, StyleConstants.ALIGN_CENTER);
        doc1.setParagraphAttributes(0, doc1.getLength(), center1, false);
        tpResult.setFont(new Font("Arial", Font.PLAIN, 20));
        JScrollPane spResult = new JScrollPane(tpResult);
        this.add(spResult, new GridBagConstraints(0, 3,
                2, 1, 0.1, 0.9,
                10, 1, ins, 0, 0));

        JSeparator sep = new JSeparator(SwingConstants.HORIZONTAL);
        this.add(sep, new GridBagConstraints(0, 4,
                2, 1, 0.1, 0.01,
                10, 1, ins, 0, 0));

        JTextPane tpDictionary = new JTextPane();
        StyledDocument doc2 = tpDictionary.getStyledDocument();
        SimpleAttributeSet center2 = new SimpleAttributeSet();
        StyleConstants.setAlignment(center2, StyleConstants.ALIGN_CENTER);
        doc2.setParagraphAttributes(0, doc2.getLength(), center2, false);
        tpDictionary.setFont(new Font("Arial", Font.PLAIN, 20));
        JScrollPane spDictionary = new JScrollPane(tpDictionary);
        this.add(spDictionary, new GridBagConstraints(0, 5,
                2, 1, 0.1, 0.4,
                10, 1, ins, 0, 0));

        JTextField tfSearch = new JTextField(15);
        tfSearch.setFont(new Font("Arial", Font.PLAIN, 20));
        tfSearch.setHorizontalAlignment(JTextField.CENTER);
        this.add(tfSearch, new GridBagConstraints(0, 6,
                2, 1, 0.1, 0.05,
                10, 1, ins, 0, 0));

        JButton bAdd = new JButton("Добавить в словарь");
        bAdd.setFont(new Font("Arial", Font.PLAIN, 18));
        this.add(bAdd, new GridBagConstraints(0, 7,
                2, 1, 0.1, 0.05,
                10, 1, ins, 0, 0));



        this.setVisible(true);
    }
}
