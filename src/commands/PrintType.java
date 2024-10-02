package commands;

import managers.CollectionManager;
import utility.Console;
import utility.ExecutionResponse;

public class PrintType extends Command {

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
    public PrintType(Console console, CollectionManager collectionManager) {
        super("print_unique_fuel_type", "вывести уникальные значения поля fuelType всех элементов в коллекции");
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
        return new ExecutionResponse(collectionManager.uniqueType());
    }
}
