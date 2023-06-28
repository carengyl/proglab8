package common.util.udp;

import common.commands.CommandArgument;
import common.util.UserData;

import java.io.Serializable;
import java.time.LocalTime;

public class Request implements Serializable {
    private boolean requestCommand = false;
    private String commandName;
    private CommandArgument commandArgument;
    private String clientInfo;
    private LocalTime sendTime;
    private UserData userData;
    private final RequestType requestType;

    /**
     * Constructs dummy-instance to receive DatagramPacket with available server.commands
     */
    public Request() {
        this.requestCommand = true;
        requestType = RequestType.INIT_COMMANDS;
    }

    public Request(String commandName, CommandArgument commandArgument) {
        this.commandName = commandName;
        this.commandArgument = commandArgument;
        this.requestType = RequestType.COMMAND;
    }

    public Request(UserData userData, boolean registered) {
        this.userData = userData;
        if (registered) {
            this.requestType = RequestType.LOGIN;
        } else {
            this.requestType = RequestType.REGISTER;
        }
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

    public String getClientInfo() {
        return clientInfo;
    }

    public boolean isRequestCommand() {
        return requestCommand;
    }

    public UserData getUserData() {
        return userData;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public LocalTime getSendTime() {
        return sendTime;
    }
}
