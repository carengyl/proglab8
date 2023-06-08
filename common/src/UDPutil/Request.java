package UDPutil;

import commands.CommandArgument;

import java.io.Serializable;
import java.time.LocalTime;

public class Request implements Serializable {
    private boolean requestCommand = false;
    private String commandName;
    private CommandArgument commandArgument;
    private String clientInfo;
    private LocalTime sendTime;
    private String userLogin;
    private String userPassword;
    private final RequestType requestType;

    /**
     * Constructs dummy-instance to receive DatagramPacket with available commands
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

    public Request(String login, String password, boolean logged) {
        this.userLogin = login;
        this.userPassword = password;
        if (logged) {
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

    public String getUserPassword() {
        return userPassword;
    }

    public String getUserLogin() {
        return userLogin;
    }
}
