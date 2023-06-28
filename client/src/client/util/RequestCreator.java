package client.util;

import common.util.udp.Request;
import common.commands.AbstractCommand;

import java.util.HashMap;

public class RequestCreator {
    public Request createRequestOfCommand(CommandToSend command) {
        return new Request(command.commandName(), command.commandArgument());
    }

    public static class ClientInvoker {
        public ClientInvoker(HashMap<String, AbstractCommand> availableLocalCommands) {

        }

        public void performCommand() {

        }
    }
}
