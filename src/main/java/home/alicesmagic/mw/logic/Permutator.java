package home.alicesmagic.mw.logic;

import java.util.*;

public class Permutator {
    private final HashSet<ArrayList<String>> all;
    private final List<String> source;
    private int index = 0;

    public Permutator(List<String> in) {
        this.source = in;
        this.all = permutation(in);
    }

    public Permutator(Set<String> in) {
        this.source = new ArrayList<>(in);
        this.all = permutation(source);
    }

    public Permutator(String[] in) {
        this.source = new ArrayList<>(Arrays.asList(in));
        this.all = permutation(source);
    }

    private HashSet<ArrayList<String>> permutation(List<String> alist) {
        ArrayList<ArrayList<String>> aal = new ArrayList<>();
        aal.add(new ArrayList<>());
        for (String s : alist) {
            aal = levelUp(aal, s);
        }
        return new HashSet<>(aal);
    }

    private ArrayList<ArrayList<String>> levelUp(ArrayList<ArrayList<String>> aalist, String up) {
        ArrayList<ArrayList<String>> res = new ArrayList<>();
        int index = 0;
        for (ArrayList<String> el : aalist) {
            for (int i = 0; i < aalist.get(0).size() + 1; i++) {
                res.add(new ArrayList<>());
                for (int j = 0; j < aalist.get(0).size(); j++) {
                    res.get(index).add(el.get(j));
                }
                res.get(index++).add(i, up);
            }
        }
        return res;
    }

    private HashSet<ArrayList<String>> getAll() {
        return all;
    }

    public String[] getNext() {
        ArrayList<ArrayList<String>> aal = new ArrayList<>(getAll());
        String[] res = (String[]) aal.get(index).toArray();
        index++;
        return res;
    }

    public boolean hasNext() {
        return index < getAll().size();
    }
}
