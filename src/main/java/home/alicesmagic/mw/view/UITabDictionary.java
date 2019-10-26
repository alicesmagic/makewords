package home.alicesmagic.mw.view;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;

class UITabDictionary extends JPanel {
    UITabDictionary() {
        this.setLayout(new GridBagLayout());
        int n = 0;

        JTextField tfSearch = new JTextField(15);
        tfSearch.setFont(new Font("Arial", Font.PLAIN, 20));
        tfSearch.setHorizontalAlignment(JTextField.CENTER);
        this.add(tfSearch, new GridBagConstraints(0, 0,
                1, 1, 0.1, 0.01,
                10, 1, UIGeneral.ins, 0, 0));

        JButton bAdd = new JButton("Добавить в словарь");
        bAdd.setFont(new Font("Arial", Font.PLAIN, 18));
        this.add(bAdd, new GridBagConstraints(0, 1,
                1, 1, 0.1, 0.01,
                10, 1, UIGeneral.ins, 0, 0));

        JTextPane tpDictionary = new JTextPane();
        StyledDocument doc = tpDictionary.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
        tpDictionary.setFont(new Font("Arial", Font.PLAIN, 20));
        JScrollPane spDictionary = new JScrollPane(tpDictionary);
        this.add(spDictionary, new GridBagConstraints(0, 2,
                1, 1, 0.1, 1.0,
                10, 1, UIGeneral.ins, 0, 0));

        JLabel lQuantity = new JLabel("Количество слов в словаре: " + n);
        lQuantity.setFont(new Font("Arial", Font.PLAIN, 16));
        lQuantity.setHorizontalAlignment(JTextField.CENTER);
        this.add(lQuantity, new GridBagConstraints(0, 3,
                1, 1, 0.1, 0.01,
                10, 1, UIGeneral.ins, 0, 0));
    }
}
