package home.alicesmagic.mw.view;

import home.alicesmagic.mw.logic.IndexesLangInput;
import home.alicesmagic.mw.model.repository.WordsRepository;
import home.alicesmagic.mw.model.storage.DataStorage;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import java.awt.*;

public class UIGeneral extends JFrame {
    private static WordsRepository repository;

    static final Insets ins = new Insets(3, 3, 3, 3);

    /////////  БЛОК ОРГАНИЦАЦИИ РУССКОГО ТЕКСТА В ПОЛЕ ВВОДА,  //////////
    /////////////  НЕЗАВИСИМО ОТ РАСКЛАДКИ КЛАВИАТУРЫ  //////////////////
    private static IndexesLangInput indexes;
    static { indexes = new IndexesLangInput(); }

    /**
     * Внутренний класс - слушатель изменений в тексте
     * текстового поля.
     * Заменяет английские символы на русские при вводе.
     */
    static class DocListener implements DocumentListener {
        @Override
        public void insertUpdate(DocumentEvent e) {
            try {
                textFilterToRussian(e);
            } catch (BadLocationException e1) { e1.printStackTrace(); }
        }

        @Override
        public void removeUpdate(DocumentEvent e) {}

        @Override
        public void changedUpdate(DocumentEvent e) {
            try {
                textFilterToRussian(e);
            } catch (BadLocationException e1) { e1.printStackTrace(); }
        }
    }

    /**
     * Внутренний enum-класс - для сопоставления английских и русских символов
     */
    enum Alphabet {
        ENG ("QWERTYUIOP{}ASDFGHJKL:\"ZXCVBNM<>qwertyuiop[]asdfghjkl;'zxcvbnm,.");
        String type;
        static String CORE =
                "ЙЦУКЕНГШЩЗХЪФЫВАПРОЛДЖЭЯЧСМИТЬБЮйцукенгшщзхъфывапролджэячсмитьбю";
        Alphabet(String type) {
            this.type = type;
        }
    }

    /**
     * Метод, осуществляющий замену в поле ввода английских символов на русские.
     * @param e - событие DocumentEvent
     */
    private static void textFilterToRussian(DocumentEvent e) throws BadLocationException {
        char[] temp = e.getDocument().getText(e.getOffset(), e.getLength())
                .toCharArray();
        int tempIndex;
        for (int i = 0; i < temp.length; i++) {
            for (Alphabet alphabet : Alphabet.values()) {
                if ((tempIndex = alphabet.type.indexOf(temp[i])) != -1) {
                    temp[i] = Alphabet.CORE.charAt(tempIndex);
                    break;
                }
            }
        }
        if (!indexes.isEqual(e.getOffset(), e.getLength(), new String(temp))) {
            SwingUtilities.invokeLater(() -> {
                try {
                    e.getDocument().remove(e.getOffset(), e.getLength());
                    e.getDocument().insertString(e.getOffset(), new String(temp), null);
                } catch (BadLocationException e1) { e1.printStackTrace(); }
            });
        }
    }
    //////////////////////////  КОНЕЦ БЛОКА  ////////////////////////////

    public UIGeneral() {
        super("Subworder");
        this.setResizable(false);
        this.setSize(320, 600);
        this.setLocationRelativeTo(null);
//        this.setLocation(1000,150); // для удобства отладки
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setIconImage(getToolkit().getImage("icon.png"));

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
        tabDictionary.tfFocus();
        tabWords.tfLettersFocus();
    }

    static WordsRepository getRepository() {
        return repository;
    }
}
