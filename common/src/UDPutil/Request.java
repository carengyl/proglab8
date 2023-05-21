package UDPutil;

import commands.CommandArgument;
import entities.HumanBeing;

import java.io.Serializable;
import java.time.LocalTime;

public class Request implements Serializable {
    private String commandName;
    private CommandArgument commandArgument;
    private String clientInfo;
    private HumanBeing humanBeing;
    private LocalTime sendTime;

    /**
     * Constructs dummy-instance to receive DatagramPacket with available commands
     */
    public Request() {}

    public Request(String commandName, CommandArgument commandArgument) {
        this.commandName = commandName;
        this.commandArgument = commandArgument;
    }

    public Request(HumanBeing humanBeing) {
        this.humanBeing = humanBeing;
    }

    public void setSendTime(LocalTime sendTime) {
        this.sendTime = sendTime;
    }

    public void setClientInfo(String clientInfo) {
        this.clientInfo = clientInfo;
    }

    public String getCommandName() {
        return commandName;
    }

    public CommandArgument getCommandArgument() {
        return commandArgument;
    }

    public HumanBeing getHumanBeing() {
        return humanBeing;
    }

    public String getClientInfo() {
        return clientInfo;
    }
}
