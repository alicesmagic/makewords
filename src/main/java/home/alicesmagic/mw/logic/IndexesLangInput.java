package home.alicesmagic.mw.logic;
/////////  БЛОК ОРГАНИЦАЦИИ РУССКОГО ТЕКСТА В ПОЛЕ ВВОДА,  //////////
/////////////  НЕЗАВИСИМО ОТ РАСКЛАДКИ КЛАВИАТУРЫ  //////////////////
/**
 * Класс, проверяющий язык вводимых в текстовое поле данных
 */
public class IndexesLangInput {
    private int lastOffset;
    private int lastLength;
    private String lastText;
    private boolean isPair = false;

    /**
     * Метод обеспечивает получение существительного соответствующего числу
     * @param lastOffset - предыдущая позиция изменения
     * @param lastLength - предыдущая длина изменения
     * @param lastText - предыдущий текст изменения
     * @return - true, если все три параметра совпадают с текущими,
     * и происходили только парные изменения (использование BackSpace - это
     * непарное изменение)
     */
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
