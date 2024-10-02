package commands;

import utility.Console;
import managers.CollectionManager;

import java.time.LocalDateTime;

import utility.ExecutionResponse;

/**
 * Класс команды для получения информации о коллекции
 */
public class Info extends Command {
    /**
     * Консоль
     */
    private final Console console;
    /**
     * Менеджер комманд
     */
    private final CollectionManager collectionManager;

    /**
     * Конструктор
     *
     * @param console           консоль
     * @param collectionManager менеджер коллекции
     */
    public Info(Console console, CollectionManager collectionManager) {
        super("info", "вывести информацию о коллекции");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    /**
     * Исполнение команды
     *
     * @param arguments массив с аргументами
     * @return возвращает ответ о выполнении команды
     */
    @Override
    public ExecutionResponse apply(String[] arguments) {
        if (!arguments[1].isEmpty())
            return new ExecutionResponse(false, "Неправильное количество аргументов!\nИспользование: '" + getName() + "'");
        var s = "Сведения о коллекции:\n";
        s += " Тип: " + collectionManager.getCollection().getClass().toString() + "\n";
        s += " Количество элементов: " + collectionManager.getCollection().size() + "\n";
        return new ExecutionResponse(s);
    }
}