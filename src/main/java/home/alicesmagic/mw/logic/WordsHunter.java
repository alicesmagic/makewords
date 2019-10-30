package home.alicesmagic.mw.logic;

import home.alicesmagic.mw.model.repository.WordsRepository;

import java.util.*;

public class WordsHunter {
    private static String[] dict;
    private static StringBuilder removeBox = new StringBuilder();

    public WordsHunter(WordsRepository repository) {
        dict = getDictAsArray(repository);
    }

    private String[] getDictAsArray(WordsRepository repository) {
        List<String> stringList = new LinkedList<>(repository.getAll());
        stringList.sort((o1, o2) -> Integer.compare(o2.length(), o1.length()));
        return stringList.toArray(new String[0]);
    }

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
                }
            } else break;
        }
        return res.toString();
    }

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
}
