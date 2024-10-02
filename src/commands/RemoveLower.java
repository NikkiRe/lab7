package commands;

import managers.CollectionManager;
import models.Vehicle;
import utility.Console;
import utility.ExecutionResponse;

import java.util.*;

public class RemoveLower extends Command {

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
    public RemoveLower(Console console, CollectionManager collectionManager) {
        super("remove_lower id", "удалить из коллекции все элементы, id которых меньше, чем заданный");
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
        if (arguments.length < 2 || arguments[1].isEmpty()) {
            return new ExecutionResponse(false, "Неправильное количество аргументов!\nИспользование: '" + getName() + " <key>'");
        }

        long key;
        try {
            key = Long.parseLong(arguments[1].trim());
        } catch (NumberFormatException e) {
            return new ExecutionResponse(false, "Id не распознан");
        }

        List<Long> rmList = new LinkedList<>();
        for (Vehicle vehicle : collectionManager.getCollection()) {
            if (vehicle.getId() < key) {
                rmList.add(vehicle.getId());
            }
        }

        for (Long id : rmList) {
            collectionManager.remove(id);
        }

        return new ExecutionResponse("Элементы с ID меньше " + key + " успешно удалены!");
    }
}
