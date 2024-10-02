package tasks;

import commands.CommandType;
import managers.CollectionManager;
import message.Message;
import message.MessageType;
import message.Serializer;
import models.Vehicle;

import java.io.IOException;
import java.util.List;

public class ShowTask extends Task {
    private CollectionManager collectionManager;

    public ShowTask(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public Message apply(String[] arguments) throws IOException {
        List<Vehicle> list = collectionManager.filter(x -> true);
        return new Message(MessageType.RESPONSE, CommandType.SHOW, new String[]{new Serializer<List<Vehicle>>().serialize(list)}, true);
    }
}
