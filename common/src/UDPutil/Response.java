package UDPutil;

import commands.AbstractCommand;
import entities.HumanBeing;

import java.io.Serializable;
import java.util.HashMap;

public class Response implements Serializable {
    private String responseMessage;
    private HumanBeing responseHumanBeing;
    private HashMap<String, AbstractCommand> availableCommands;
    private boolean requestHumanBeing = false;

    public Response(HashMap<String, AbstractCommand> availableCommands) {
        this.availableCommands = availableCommands;
    }

    public Response(HumanBeing humanBeing) {
        this.responseHumanBeing = humanBeing;
    }

    public HumanBeing getResponseHumanBeing() {
        return responseHumanBeing;
    }

    public HashMap<String, AbstractCommand> getAvailableCommands() {
        return availableCommands;
    }

    public void setRequestHumanBeing(boolean requestHumanBeing) {
        this.requestHumanBeing = requestHumanBeing;
    }

    public boolean hasRequestHumanBeing() {
        return requestHumanBeing;
    }
}
