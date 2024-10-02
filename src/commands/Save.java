package commands;

import managers.CollectionManager;
import managers.DumpManager;
import models.Vehicle;
import utility.Console;
import utility.ExecutionResponse;

import java.time.LocalDateTime;
import java.util.LinkedList;

/**
 * Класс команды для сохранения коллекции в файл
 */
public class Save extends Command {
    /**
     * Менеджер коллекции
     */
    private final CollectionManager collectionManager;
    private final DumpManager dumpManager;
    private final LinkedList<Vehicle> collection = new LinkedList<>();


    public Save(Console console, CollectionManager collectionManager, DumpManager dumpManager) {
        super("save", "сохранить коллекцию в файл");
        /*
         * Консоль
         */
        this.collectionManager = collectionManager;
        this.dumpManager = dumpManager;
    }



    /**
     * Функция сохранения коллекции в файл
     */
    public void saveCollection() {
        dumpManager.writeCollection(collection);
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
        collectionManager.saveCollection();
        return new ExecutionResponse(true, "");
    }
}