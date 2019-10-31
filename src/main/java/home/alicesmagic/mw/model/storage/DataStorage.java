package home.alicesmagic.mw.model.storage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
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
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Метод загружает словарь из файла
     * @return словарь
     */
    @Override
    public TreeSet<String> load() {
        TreeSet<String> words = new TreeSet<>();
        try {
            BufferedReader bufferedReader = Files.newBufferedReader(
                    Paths.get(fileName), StandardCharsets.UTF_8);
            String strFile;
            while ((strFile = bufferedReader.readLine()) != null)
                words.add(strFile);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return words;
    }
}
