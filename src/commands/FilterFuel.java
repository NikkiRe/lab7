package commands;

import managers.CollectionManager;
import models.FuelType;
import models.Vehicle;
import utility.Console;
import utility.ExecutionResponse;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Класс команды для вывода конкретной коллекции по имени
 */

public class FilterFuel extends Command {
    /**
     * Консоль
     */
    private final Console console;

    /**
     * Менеджер коллекции
     */
    private final CollectionManager collectionManager;

    public FilterFuel(Console console, CollectionManager collectionManager) {
        super("filter_consumption fuelConsumption", "вывести элементы, значение поля fuelConsumption которых больше заданного");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    @Override
    public ExecutionResponse apply(String[] arguments) {

        if (arguments[1].isEmpty() || arguments.length > 2)
            return new ExecutionResponse(false, "Неправильное количество аргументов!\nИспользование: '" + getName() + "'");
        int fuelConsuption;
        try {
            fuelConsuption = Integer.parseInt(arguments[1]);
        }
        catch (Exception e){return new ExecutionResponse(false, "Аргумент не является числом! '" );}
        return new ExecutionResponse(collectionManager.toString(x -> x.getFuelConsumption() > fuelConsuption));

    }
}
