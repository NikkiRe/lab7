package tasks;

import commands.CommandType;
import managers.DataManager;
import message.Message;
import message.MessageType;
import message.Serializer;

import java.io.IOException;

public class Login extends Task {
    private DataManager dataManager;

    public Login(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public Message apply(String[] arguments) throws IOException {
        boolean ans = dataManager.login(arguments);
        return new Message(MessageType.RESPONSE, CommandType.CHECK_USERNAME,
                new String[]{new Serializer<Boolean>().serialize(ans)}, ans);
    }
}
