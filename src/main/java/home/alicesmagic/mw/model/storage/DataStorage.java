package home.alicesmagic.mw.model.storage;

import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.TreeSet;
/**
 * Класс реализующий интерфейс IDataStorage и обеспечивающий хранение
 * словаря в файле на жестком диске
 */
public class DataStorage implements IDataStorage {
    private final String fileName;

    public DataStorage(String fileName) {
        this.fileName = fileName + ".txt";
    }

    /**
     * Метод сохраняет словарь в файле
     * @param words словарь
     */
    @Override
    public void save(TreeSet<String> words) {
        try (PrintWriter pw = new PrintWriter(fileName)) {
            for  (String w : words) {
                pw.println(w);
            }
        }
        catch(IOException exc) {
            System.out.println("Ошибка ввода-вывода: " + exc);
        }
    }

    /**
     * Метод загружает словарь из файла
     * @return словарь
     */
    @Override
    public TreeSet<String> load() {
        TreeSet<String> words = new TreeSet<>();
        try (Scanner in = new Scanner(new FileReader(fileName))) {
            // В первой позиции в файле может оказаться служебный
            // символ. Следующее две строки сохраняют слово в чистом виде
            String first = in.nextLine();
            words.add(first.substring(first.indexOf('а')));
            while (in.hasNextLine()) {
                words.add(in.nextLine());
            }
        }
        catch(IOException exc) {
            System.out.println("Ошибка ввода-вывода: " + exc);
        }
        return words;
    }
}
