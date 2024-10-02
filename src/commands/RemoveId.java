package commands;

import managers.CollectionManager;
import utility.Console;
import utility.ExecutionResponse;

/**
 * Класс команды для удаления элемента из коллекции по его ключу
 */
public class Remove_Id extends Command {
    /**
     * Консоль
     */
    private final Console console;
    /**
     * Менеджер коллекции
     */
    private final CollectionManager collectionManager;

    /**
     * Конструктор
     *
     * @param console           консоль
     * @param collectionManager менеджер коллекции
     */
    public Remove_Id(Console console, CollectionManager collectionManager) {
        super("remove_id <ID>", "удалить элемент из коллекции по ID");
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
        if (arguments[1].isEmpty())
            return new ExecutionResponse(false, "Неправильное количество аргументов!\nИспользование: '" + getName() + "'");
        try {
            if (!collectionManager.remove(Integer.parseInt(arguments[1])))
                return new ExecutionResponse(false, "Элемент с таким id не найден");
        } catch (NumberFormatException e) {
            return new ExecutionResponse(false, "Ключ не распознан");
        }
        return new ExecutionResponse("Vehicle успешно удалена!");
    }
}