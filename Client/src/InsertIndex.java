package commands;

import managers.AskManager;
import managers.CollectionManager;
import models.Vehicle;
import utility.Console;
import utility.Request;

/**
 * Класс команды для добавления нового элемента в коллекцию
 */
public class InsertIndex extends Command {
    /**
     * Консоль
     */
    private final Console console;

    /**
     * Конструктор
     *
     * @param console           консоль
     * @param collectionManager менеджер коллекции
     */
    public InsertIndex(Console console) {
        super("insert_index {id}", "добавить новый элемент с заданным ключом");
        this.console = console;
    }

    /**
     * Исполнение команды
     *
     * @param arguments массив с аргументами команды
     * @return возвращает ответ о выполнении команды
     */
    @Override
    public Request apply(String[] arguments) {
        try {
            if (arguments[1].isEmpty())
                return new Request(Type.INSERT, false, "Неправильное количество аргументов!\nИспользование: '" + getName() + "'\n");
            long key = -1;
            try {
                key = Long.parseLong(arguments[1].trim());
            } catch (NumberFormatException e) {
                return new Request(Type.INSERT, false, "Ключ не распознан");
            }
            if (collectionManager.getById(collectionManager.getFreeId()) != null) {
                return new ExecutionResponse(false, "Элемент с таким ключем уже существует");
            }
            console.println("* Создание нового Vehicle:");
            Vehicle v = AskManager.askVehicle(console, collectionManager, key);

            if (v != null && v.validate()) {
                collectionManager.add(v);
                return new ExecutionResponse("Vehicle успешно добавлен!");
            } else return new ExecutionResponse(false, "Поля Vehicle не валидны! Vehicle не создан!");
        } catch (AskManager.AskBreak e) {
            return new Request(Type.INSERT, false, "Отмена...");
        }
    }
}