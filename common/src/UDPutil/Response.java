package UDPutil;

import commands.AbstractCommand;
import entities.HumanBeing;

import java.io.Serializable;
import java.util.HashMap;

public class Response implements Serializable {
    private String responseMessage;
    private HumanBeing responseHumanBeing;
    private final HashMap<String, AbstractCommand> availableCommands;

    public Response(HashMap<String, AbstractCommand> availableCommands) {
        this.availableCommands = availableCommands;
    }

    public HashMap<String, AbstractCommand> getAvailableCommands() {
        return availableCommands;
    }
}
