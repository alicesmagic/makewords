package home.alicesmagic.mw.model.repository;

import java.util.TreeSet;
/**
 * Интерфейс обеспечивает доступ к словарю
 */
public interface IWordsRepository {
    /**
     * Метод обеспечивает поиск слова в словаре по начальным буквам
     * @param sub - начальная часть слова для поиска
     * @return - найденное слово
     */
    String findBySub(String sub);

    /**
     * Метод добавляет слово в словарь
     * @param word - добавляемое слово
     */
    void addWord(String word);

    /**
     * Метод удаляет слово из словаря
     * @param word - удаляемое слово
     */
    void removeWord(String word);

    /**
     * Метод возвращает весь словарь целиком
     * @return - найденное слово
     */
    TreeSet<String> getAll();

    /**
     * Метод сохраняет словарь в текущем виде
     */
    void saveDictionary();
}
