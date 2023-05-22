package UDPutil;

import commands.AbstractCommand;
import entities.HumanBeing;

import java.io.Serializable;
import java.util.HashMap;

public class Response implements Serializable {
    private AbstractCommand command;
    private String responseMessage;
    private HashMap<Long, HumanBeing> responseCollection;
    private HumanBeing responseHumanBeing;
    private HashMap<String, AbstractCommand> availableCommands;
    private boolean requestHumanBeing = false;

    public Response(HashMap<String, AbstractCommand> availableCommands) {
        this.availableCommands = availableCommands;
    }

    public Response(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public Response(String responseMessage, HumanBeing humanBeing) {
        this.responseMessage = responseMessage;
        this.responseHumanBeing = humanBeing;
    }

    public Response(AbstractCommand command) {
        requestHumanBeing = true;
        this.command = command;
    }

    public HumanBeing getResponseHumanBeing() {
        return responseHumanBeing;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public HashMap<Long, HumanBeing> getResponseCollection() {
        return responseCollection;
    }

    public HashMap<String, AbstractCommand> getAvailableCommands() {
        return availableCommands;
    }

    public boolean hasRequestHumanBeing() {
        return requestHumanBeing;
    }

    public void setRequestHumanBeing() {
        this.requestHumanBeing = true;
    }

    public AbstractCommand getCommand() {
        return command;
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
}
