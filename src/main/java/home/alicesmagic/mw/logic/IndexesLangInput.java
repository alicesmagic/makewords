package home.alicesmagic.mw.logic;
/**
 * Класс, проверяющий язык вводимых в текстовое поле данных.
 * (Чужой код, который я пока не совсем понимаю)
 */
public class IndexesLangInput {
    private int lastOffset;
    private int lastLength;
    private String lastText;

    public boolean isEqual(int lastOffset, int lastLength, String lastText) {
        boolean result = this.lastOffset == lastOffset &&
                this.lastLength == lastLength &&
                lastText.equals(this.lastText);
        if (!result) {
            this.lastOffset = lastOffset;
            this.lastLength = lastLength;
            this.lastText = lastText;
        }
        return result;
    }
}
