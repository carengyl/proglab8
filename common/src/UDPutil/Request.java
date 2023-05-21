package UDPutil;

import entities.HumanBeing;

import java.io.Serializable;

public class Request implements Serializable {

    private final String commandName;
    private long arg;

    /**
     * Constructs dummy-instance to receive DatagramPacket with available commands
     */
    public Request() {
        commandName = "";
    }

    public Request(String commandName, long arg) {
        this.commandName = commandName;
    }

    public Request(String commandName) {
        this.commandName = commandName;
    }

    public Request(String commandName, HumanBeing humanBeing) {
        this.commandName = commandName;
    }
}
