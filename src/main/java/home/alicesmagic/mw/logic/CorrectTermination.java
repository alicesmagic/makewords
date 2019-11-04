package home.alicesmagic.mw.logic;

/**
 * Класс, подставляющий к числительному существительное в правильной форме
 */
public class CorrectTermination {
    private String one; // один стул
    private String three; // три стулА
    private String five; // пять стулЬЕВ

    public CorrectTermination(String one, String three, String five) {
        this.one = one;
        this.three = three;
        this.five = five;
    }

    /**
     * Метод обеспечивает получение существительного соответствующего числу
     * @param num - входное число
     * @return - только слово в правильной форме
     */
    public String getWord(int num) {
        int[] arr = new int[2];
        arr[1] = num % 10;
        num /= 10;
        arr[0] = num % 10;
        if (arr[0] != 1 && arr[1] > 1 && arr[1] < 5) return three;
        if (arr[0] != 1 && arr[1] == 1) return one;
        return five;
    }

    /**
     * Метод обеспечивает получение существительного соответствующего числу
     * @param num - входное число
     * @return - само число + слово в правильной форме
     */
    public String getNumAndWord(int num) {
        return num + " " + getWord(num);
    }

}
