package tasks;

import commands.CommandType;
import managers.CollectionManager;
import message.Message;
import message.MessageType;
import message.Serializer;
import models.Vehicle;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class FilterNameTask extends Task {
    private CollectionManager collectionManager;

    public FilterNameTask(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public Message apply(String[] arguments) throws IOException {
        List<Vehicle> list = collectionManager.filter(x -> Objects.equals(x.getName(), arguments[2]));
        return new Message(MessageType.RESPONSE, CommandType.FILTER_NAME, new String[]{new Serializer<List<Vehicle>>().serialize(list)}, true);
    }
}
