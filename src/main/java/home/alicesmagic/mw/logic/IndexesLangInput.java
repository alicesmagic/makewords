package home.alicesmagic.mw.logic;
/////////  БЛОК ОРГАНИЦАЦИИ РУССКОГО ТЕКСТА В ПОЛЕ ВВОДА,  //////////
/////////////  НЕЗАВИСИМО ОТ РАСКЛАДКИ КЛАВИАТУРЫ  //////////////////
/**
 * Класс, проверяющий язык вводимых в текстовое поле данных.
 * (Чужой код, который я пока не совсем понимаю)
 */
public class IndexesLangInput {
    private int lastOffset;
    private int lastLength;
    private String lastText;
    private boolean isPair = false;

    public boolean isEqual(int lastOffset, int lastLength, String lastText) {
        boolean result = isPair &&
                this.lastOffset == lastOffset &&
                this.lastLength == lastLength &&
                lastText.equals(this.lastText);
        isPair = !isPair;

        if (!result) {
            this.lastOffset = lastOffset;
            this.lastLength = lastLength;
            this.lastText = lastText;
        }
        return result;
    }
}
