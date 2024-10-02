package utility;

/**
 * Интерфейс проверки правильности хранимых данных
 */
public interface Validatable {
    /**
     * @return true, если данные имеют правильный формат
     */
    boolean validate();
}
