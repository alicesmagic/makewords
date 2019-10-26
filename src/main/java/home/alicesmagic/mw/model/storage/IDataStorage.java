package home.alicesmagic.mw.model.storage;

import java.util.TreeSet;
/**
 * Интерфейс IDataStorage, обеспечивающий
 * хранение словаря в файле на жестком диске
 */
public interface IDataStorage {
    /**
     * Метод сохраняет словарь в файле
     * @param words словарь
     */
    void save(TreeSet<String> words);

    /**
     * Метод загружает словарь из файла
     * @return словарь
     */
    TreeSet<String> load();
}
