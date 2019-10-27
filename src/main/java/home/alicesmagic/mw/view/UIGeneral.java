package home.alicesmagic.mw.view;

import home.alicesmagic.mw.model.repository.WordsRepository;
import home.alicesmagic.mw.model.storage.DataStorage;

import javax.swing.*;
import java.awt.*;

public class UIGeneral extends JFrame {
    private static WordsRepository repository;

    static final Insets ins = new Insets(3, 3, 3, 3);

    public UIGeneral() {
        super("Subworder");
        this.setResizable(false);
        this.setLocation(640,150);
//        this.setLocationRelativeTo(null);
        this.setSize(320, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        DataStorage dataStorage = new DataStorage("rus");
        repository = new WordsRepository(dataStorage);

        JTabbedPane tp = new JTabbedPane(JTabbedPane.BOTTOM);
        tp.setFont(new Font("Arial", Font.PLAIN, 18));
        tp.addTab("Поиск слов", new UITabWords());
        tp.addTab("Словарь", new UITabDictionary());
        this.add(tp);
        this.setVisible(true);

    }

    static WordsRepository getRepository() {
        return repository;
    }

}
