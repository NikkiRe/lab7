package commands;

import managers.CollectionManager;
import models.Vehicle;
import utility.Console;
import utility.ExecutionResponse;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Класс команды для вывода конкретной коллекции по имени
 */

public class FilterName extends Command {
    /**
     * Консоль
     */
    private final Console console;
    /**
     * Менеджер коллекции
     */
    private final CollectionManager collectionManager;

    public FilterName(Console console, CollectionManager collectionManager) {
        super("filter_contains_name name", "вывести элементы, значение поля name которых содержит");
        this.console = console;
        this.collectionManager = collectionManager;
    }
    @Override
    public ExecutionResponse apply(String[] arguments) {
        if (arguments[1].isEmpty())
            return new ExecutionResponse(false, "Неправильное количество аргументов!\nИспользование: '" + getName() + "'");
        StringBuilder name = new StringBuilder();
        for (var s : Arrays.copyOfRange(arguments, 1, arguments.length))
            name.append(s);

        return new ExecutionResponse(collectionManager.toString(x -> Objects.equals(x.getName(), name.toString())));
    }
}
