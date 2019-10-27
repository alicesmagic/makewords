package home.alicesmagic.mw.view;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

public class UITabDictionary extends JPanel {
    UITabDictionary() {
        this.setLayout(new GridBagLayout());

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
//        tpDictionary.setText(UIGeneral.getRepository().toColString());
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
    }
}
