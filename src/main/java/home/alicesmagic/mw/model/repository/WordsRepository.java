package home.alicesmagic.mw.model.repository;

import home.alicesmagic.mw.model.storage.IDataStorage;

import java.util.List;
import java.util.TreeSet;

public class WordsRepository implements IWordsRepository {
    private TreeSet<String> dictionary;
    private final IDataStorage dataStorage;

    public WordsRepository(IDataStorage dataStorage) {
        this.dataStorage = dataStorage;
        this.dictionary = this.dataStorage.load();
    }

    public String findByInd(int n) {
        List<String> ls = List.copyOf(dictionary);
        return ls.get(n);
    }

    @Override
    public String findBySub(String sub) {
        for (String s : dictionary) {
            if (s.indexOf(sub) == 0) return s;
        }
        return null;
    }

    @Override
    public void addWord(String word) {
        dictionary.add(word);
    }

    @Override
    public void removeWord(String word) {
        dictionary.remove(word);
    }

    @Override
    public TreeSet<String> getAll() {
        return dictionary;
    }

    @Override
    public void saveDictionary() {
        this.dataStorage.save(dictionary);
    }

    @Override
    public String toColString() {
        StringBuilder sb = new StringBuilder();
        for (String s : dictionary) {
            sb.append(s).append("\n");
        }
        return sb.toString();
    }
}
