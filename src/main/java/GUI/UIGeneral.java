package GUI;

import javax.swing.*;
import java.awt.*;

public class UIGeneral extends JFrame {
    static final Insets ins = new Insets(3, 3, 3, 3);

    public UIGeneral() {
        super("Subworder");
//        this.setResizable(false);
//        this.setLocationRelativeTo(null);
        this.setLocation(900,150);
        this.setSize(320, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTabbedPane tp = new JTabbedPane(JTabbedPane.BOTTOM);
        tp.setFont(new Font("Arial", Font.PLAIN, 18));
        tp.addTab("Поиск слов", new UITabWords());
        tp.addTab("Словарь", new UITabDictionary());
        add(tp);

        this.setVisible(true);
    }
}
