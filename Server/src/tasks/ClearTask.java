package tasks;

import commands.CommandType;
import managers.CollectionManager;
import message.Message;
import message.MessageType;

import java.io.IOException;

public class Clear extends Task {
    private CollectionManager collectionManager;

    public Clear(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public Message apply(String[] arguments) throws IOException {
        collectionManager.clearCollection(arguments[0]);
        return new Message(MessageType.RESPONSE, CommandType.CLEAR, new String[]{"Коллекция очищена!"}, true);
    }
}
