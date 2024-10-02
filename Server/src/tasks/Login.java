package tasks;

import commands.CommandType;
import managers.DataManager;
import message.Message;
import message.MessageType;
import message.Serializer;

import java.io.IOException;

public class CheckUsername extends Task {
    private DataManager dataManager;

    public CheckUsername(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public Message apply(String[] arguments) throws IOException {
        return new Message(MessageType.RESPONSE, CommandType.CHECK_USERNAME,
                new String[]{new Serializer<Boolean>().serialize(dataManager.checkUsername(arguments[0]))}, true);
    }
}
