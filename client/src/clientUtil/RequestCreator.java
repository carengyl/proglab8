package clientUtil;

import UDPutil.Request;

public class RequestCreator {
    public Request createRequestOfCommand(CommandToSend command) {
        return new Request(command.commandName(), command.commandArgument());
    }
}
