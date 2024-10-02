package commands;

import managers.AskManager;
import managers.CollectionManager;
import models.Vehicle;
import utility.Console;
import utility.ExecutionResponse;

import java.util.Objects;

public class Add extends Command  {
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
    public Add(Console console, CollectionManager collectionManager) {
        super("add", "добавить новый элемент");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    /**
     * Исполнение команды
     *
     * @param arguments массив с аргументами команды
     * @return возвращает ответ о выполнении команды
     */
    @Override
    public ExecutionResponse apply(String[] arguments) {
        try {
            // Подбор свободного ID
            long freeId = collectionManager.getFreeId();
            if (collectionManager.getById(freeId) != null) {
                return new ExecutionResponse(false, "Не удалось найти свободный ID");
            }
            if (!Objects.equals(arguments[1], "") || arguments.length > 2) {return new ExecutionResponse("Тут не указывается Id");}
            console.println("* Создание нового Vehicle:");
            Vehicle v = AskManager.askVehicle(console, collectionManager, freeId);

            if (v != null && v.validate()) {
                collectionManager.add(v);
                return new ExecutionResponse("Vehicle успешно добавлен!");
            } else {
                return new ExecutionResponse(false, "Поля Vehicle не валидны! Vehicle не создан!");
            }
        } catch (AskManager.AskBreak e) {
            return new ExecutionResponse(false, "Отмена...");
        }
    }
}
