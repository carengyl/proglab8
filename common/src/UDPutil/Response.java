package UDPutil;

import commands.AbstractCommand;
import entities.HumanBeing;

import java.io.Serializable;
import java.util.HashMap;

public class Response implements Serializable {
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

    public Response(HumanBeing humanBeing) {
        this.responseHumanBeing = humanBeing;
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

    @Override
    public String toString() {
        return super.toString();
    }
}
