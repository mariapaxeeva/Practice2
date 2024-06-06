package logic;

public class Protection {
    // Метод преобразует выходящее за рамки указанного диапазона значение
    // к max, если значение больше максимума
    // к min, если значение меньше минимума
    public static int getValueInRange(int value, int min, int max) {
        return Math.max(Math.min(value, max), min);
    }
}
