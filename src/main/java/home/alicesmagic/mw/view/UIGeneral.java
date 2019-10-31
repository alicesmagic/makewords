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
        this.setSize(320, 600);
        this.setLocationRelativeTo(null);
//        this.setLocation(1000,150); // для удобства отладки
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        DataStorage dataStorage = new DataStorage("rus");
        repository = new WordsRepository(dataStorage);

        JTabbedPane tp = new JTabbedPane(JTabbedPane.BOTTOM);
        tp.setFont(new Font("Arial", Font.PLAIN, 18));
        UITabWords tabWords = new UITabWords();
        tp.addTab("Поиск слов", tabWords);
        UITabDictionary tabDictionary = new UITabDictionary();
        tp.addTab("Словарь", tabDictionary);
        this.add(tp);
        this.setVisible(true);
        tabDictionary.tfSearchFocus();
        tabWords.tfWordFocus();
    }

    static WordsRepository getRepository() {
        return repository;
    }

}
