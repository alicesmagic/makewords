package home.alicesmagic.mw.logic;

import home.alicesmagic.mw.model.repository.WordsRepository;

import java.util.*;
/**
 * Класс, осуществляющий поиск слов, состоящих из заданных букв
 */
public class WordsHunter {
    private static String[] dict;
    private static StringBuilder removeBox = new StringBuilder();

    private int numberWords = 0;

    public WordsHunter(WordsRepository repository) {
        dict = getDictAsArray(repository);
    }

    /**
     * Метод, формирующий массив словаря отсортированный по убыванию длины слов
     * @param repository - репозиторий словаря
     * @return - отсортированный массив словаря
     */
    private String[] getDictAsArray(WordsRepository repository) {
        List<String> stringList = new LinkedList<>(repository.getAll());
        stringList.sort((o1, o2) -> Integer.compare(o2.length(), o1.length()));
        return stringList.toArray(new String[0]);
    }

    /**
     * Метод, формирующий массив искомых слов, разделенных на группы по длине слов
     * @param minLengthWord - минимальная длина искомых слов
     * @param patLetters - набор символов, из которых формируютеся искомые слова
     * @return - строка искомых слов, сформатированная для вывода в текствовую панель
     */
    public String getSubWords(int minLengthWord, String patLetters) {
        int oldLength = patLetters.length() + 1;
        int curLength;
        StringBuilder res = new StringBuilder();
        for (String wordDict : dict) {
            if (wordDict.length() > patLetters.length()) continue;
            if (wordDict.length() >= minLengthWord) {
                if (pseudoEquails(patLetters, wordDict)) {
                    curLength = wordDict.length();
                    if (curLength != oldLength) {
                        if (res.length() != 0) {
                            res.append("\n");
                        }
                        res.append("--- ").append(curLength).append(" ---\n");
                        oldLength = wordDict.length();
                    }
                    res.append(wordDict).append("\n");
                    numberWords++;
                }
            } else break;
        }
        return res.toString();
    }

    /**
     * Служебный метод, проверяющий соответствие текущего слова из словаря условию
     * задачи.
     * @param letters - исходный набор символов
     * @param word - текущее слово из словаря
     * @return - true, если слово удовлетворяет условию
     */
    private boolean pseudoEquails(String letters, String word) {
        if (letters.equals(word)) return false;
        for (int i = 0; i < word.length(); i++) {
            if (letters.indexOf(word.charAt(i)) == -1) return false;
        }
        removeBox.delete(0, removeBox.length());
        removeBox.append(word);
        for (int i = 0; i < letters.length(); i++) {
            int remIndex = removeBox.indexOf("" + letters.charAt(i));
            if (remIndex != -1) {
                removeBox.delete(remIndex, remIndex + 1);
            }
        }
        return removeBox.length() == 0;
    }

    /**
     * Геттер возвращающий количество найденных слов
     * @return - количество найденных слов
     */
    public int getNumberWords() {
        return numberWords;
    }

}
