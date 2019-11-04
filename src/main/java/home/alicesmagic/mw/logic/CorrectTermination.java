package home.alicesmagic.mw.logic;

import java.util.Arrays;

public class CorrectTermination {
    private String one;
    private String three;
    private String five;

    public CorrectTermination(String one, String three, String five) {
        this.one = one;
        this.three = three;
        this.five = five;
    }

    public String getWord(int num) {
        int[] arr = new int[2];
        arr[1] = num % 10;
        num /= 10;
        arr[0] = num % 10;
        if (arr[0] != 1 && arr[1] > 1 && arr[1] < 5) return three;
        if (arr[0] != 1 && arr[1] == 1) return one;
        return five;
    }

    public String getNumAndWord(int num) {
        return num + " " + getWord(num);
    }

}
