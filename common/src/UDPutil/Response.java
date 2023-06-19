package UDPutil;

import commands.CommandArgument;
import commands.CommandData;
import entities.HumanBeing;

import java.io.Serializable;
import java.util.*;

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
    private List<Long> listOfIds;
    private Set<HumanBeing> usersElements;
    private Set<HumanBeing> alienElements;

    public Response(HashMap<String, CommandData> availableCommands) {
        this.availableCommands = availableCommands;
    }

    public Response(String responseMessage, List<Long> listOfIds) {
        this.responseMessage = responseMessage;
        this.listOfIds = listOfIds;
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

    public Response(String responseMessage, Set<HumanBeing> usersElements, Set<HumanBeing> alienElements) {
        this.responseMessage = responseMessage;
        this.usersElements = usersElements;
        this.alienElements = alienElements;
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
        StringBuilder ids = new StringBuilder();
        if (usersElements != null) {
            List<HumanBeing> sortedHumans = new ArrayList<>(usersElements);
            collection.append("Your elements:\n");
            for (HumanBeing h: sortedHumans) {
                collection.append(h.toString());
                collection.append("\n");
            }
        } else {
            collection.append("You don't have elements in this collection!\n");
        }
        if (alienElements != null) {
            List<HumanBeing> sortedHumans = new ArrayList<>(alienElements);
            collection.append("Your elements:\n");
            for (HumanBeing h: sortedHumans) {
                collection.append(h.toString());
                collection.append("\n");
            }
            collection = new StringBuilder(collection.substring(0, collection.length() - 1));
        } else {
            collection.append("Another users don't have elements in this collection!");
        }
        if (listOfIds != null) {
            for (Long id : listOfIds) {
                ids.append(id).append(", ");
            }
            ids = new StringBuilder(ids.substring(0, ids.length() - 2));
        }

        return (responseMessage == null ? "" : responseMessage)
                + (responseHumanBeing == null ? "" : "\n" + responseHumanBeing)
                + ((usersElements == null && alienElements == null) ? "" : "\n" + collection)
                + ((listOfIds == null) ? "" : "\n" + ids);
    }

    public boolean isRequestScriptCommands() {
        return requestScriptCommands;
    }

    public boolean isSuccess() {
        return success;
    }
}
