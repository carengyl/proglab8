package UDPutil;

import commands.CommandArgument;
import commands.CommandData;
import entities.HumanBeing;

import java.io.Serializable;
import java.util.HashMap;

public class Response implements Serializable {
    private boolean success;
    private CommandData command;
    private String responseMessage;
    private HashMap<Long, HumanBeing> responseCollection;
    private HumanBeing responseHumanBeing;
    private HashMap<String, CommandData> availableCommands;
    private boolean requestHumanBeing = false;
    private boolean requestScriptCommands = false;
    private CommandArgument commandArgument;

    public Response(HashMap<String, CommandData> availableCommands) {
        this.availableCommands = availableCommands;
    }

    public Response(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public Response(String responseMessage, boolean success) {
        this.responseMessage = responseMessage;
        this.success = success;
    }

    public Response(String responseMessage, HumanBeing humanBeing) {
        this.responseMessage = responseMessage;
        this.responseHumanBeing = humanBeing;
    }

    public Response(CommandData command, CommandArgument commandArgument) {
        this.requestHumanBeing = true;
        this.command = command;
        this.commandArgument = commandArgument;
    }

    public Response(CommandArgument commandArgument) {
        this.commandArgument = commandArgument;
        this.requestScriptCommands = true;
    }

    public HashMap<String, CommandData> getAvailableCommands() {
        return availableCommands;
    }

    public boolean hasRequestHumanBeing() {
        return requestHumanBeing;
    }

    public CommandData getCommandData() {
        return command;
    }

    public CommandArgument getCommandArgument() {
        return commandArgument;
    }

    @Override
    public String toString() {
        StringBuilder collection = new StringBuilder();
        if (responseCollection != null) {
            for (long key: responseCollection.keySet()) {
                collection.append(responseCollection.get(key).toString()).append("\n");
            }
            collection = new StringBuilder(collection.substring(0, collection.length() - 1));
        }
        return (responseMessage == null ? "" : responseMessage)
                + (responseHumanBeing == null ? "" : "\n" + responseHumanBeing)
                + ((responseCollection == null) ? "" : "\n"
                + collection);
    }

    public void setResponseCollection(HashMap<Long, HumanBeing> responseCollection) {
        this.responseCollection = responseCollection;
    }

    public boolean isRequestScriptCommands() {
        return requestScriptCommands;
    }

    public boolean isSuccess() {
        return success;
    }
}
