package client.util;

import common.util.udp.Request;
import common.util.udp.Response;
import common.commands.CommandArgument;
import common.commands.CommandData;
import common.util.HumanBeingFactory;
import common.util.OutputUtil;
import common.util.UserData;
import common.util.Validators;
import common.entities.HumanBeing;
import common.exceptions.NoUserInputException;
import client.commands.ExitCommand;
import client.commands.HelpCommand;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.nio.file.Path;
import java.time.LocalTime;
import java.util.*;

public class Session {
    private static final Set<Path> scriptHistory = new HashSet<>();
    private final ScriptReader scriptReader = new ScriptReader();
    private final Scanner scanner;
    private ClientHandler.CommandReader commandReader;
    private final ClientSocketHandler clientSocketHandler;
    private final RequestCreator requestCreator = new RequestCreator();
    private boolean working = true;
    private final UserData userData;

    public Session(UserData userData, Scanner scanner, ClientSocketHandler socketHandler) {
        this.scanner = scanner;
        this.userData = userData;
        this.clientSocketHandler = socketHandler;
    }


    public void startSession() {
        commandReader = new ClientHandler.CommandReader(getCommandsFromServer());
        this.initClientCommands();

        while (working) {
            try {
                Optional<CommandToSend> optionalCommand = commandReader.readCommandsFromConsole(scanner);
                if (optionalCommand.isPresent()) {
                    CommandToSend commandToSend = optionalCommand.get();
                    if (this.sendCommandRequest(commandToSend)) {
                        try {
                            this.receiveResponse();
                        } catch (NoUserInputException e) {
                            OutputUtil.printErrorMessage(e.getMessage());
                            this.turnOff();
                        }
                    }
                }
            } catch (NoUserInputException e) {
                OutputUtil.printErrorMessage(e.getMessage());
                turnOff();
            }
        }
    }

    private boolean sendCommandRequest(CommandToSend command) {
        Request request = requestCreator.createRequestOfCommand(command);
        return sendRequest(request);
    }

    private boolean sendHumanBeingRequest(CommandData commandData, CommandArgument commandArgument) throws NoUserInputException {
        HumanBeingFactory humanBeingFactory = new HumanBeingFactory();
        humanBeingFactory.setVariables();
        commandArgument.setHumanBeingArgument(humanBeingFactory.getCreatedHumanBeing());
        Request request = new Request(commandData.commandName(), commandArgument);
        return sendRequest(request);
    }

    private boolean sendRequest(Request request) {
        try {
            request.setUserData(userData);
            request.setClientInfo(clientSocketHandler.getAddress() + ":" + clientSocketHandler.getPort());
            request.setSendTime(LocalTime.now());
            clientSocketHandler.sendRequest(request);
            return true;
        } catch (IOException e) {
            OutputUtil.printErrorMessage("An error occurred while serializing the request, try again");
            e.printStackTrace();
            return false;
        }
    }

    private void receiveResponse() throws NoUserInputException {
        try {
            Response response = clientSocketHandler.receiveResponse();
            if (response.isRequestScriptCommands()) {
                String scriptFileName = String.valueOf(response.getCommandArgument().getFileName());
                readCommandsFromScript(scriptFileName);
            }
            else {
                if (response.hasRequestHumanBeing()) {
                    if (sendHumanBeingRequest(response.getCommandData(), response.getCommandArgument())) {
                        receiveResponse();
                    }
                }
                OutputUtil.printSuccessfulMessage(response.toString());
            }
        } catch (SocketTimeoutException socketTimeoutException) {
            OutputUtil.printErrorMessage("The waiting time for a response from the server has been exceeded, try again later");
        } catch (IOException ioException) {
            OutputUtil.printErrorMessage("An error occurred while receiving a response from the server:");
            ioException.printStackTrace();
        } catch (ClassNotFoundException classNotFoundException) {
            OutputUtil.printErrorMessage("The response came damaged");
        }
    }

    private void readCommandsFromScript(String scriptFileName) {
        Path script = Path.of(scriptFileName);
        scriptHistory.add(script);
        if (scriptHistory.contains(script)) {
            OutputUtil.printErrorMessage("Loop possible");
        }
        else {
            scriptHistory.add(script);
            try {
                scriptReader.readCommandsFromFile(scriptFileName);
            } catch (IOException e) {
                OutputUtil.printErrorMessage(e.getMessage());
            }
            ArrayList<String> commandsFromScript = scriptReader.getCommandsFromScript();
            if (commandsFromScript.contains("execute_script " + scriptFileName)) {
                OutputUtil.printErrorMessage("Script calls itself from inside. Loop possible");
            } else {
                for (int i=0; i < commandsFromScript.size(); i++) {
                    String command = commandsFromScript.get(i);
                    OutputUtil.printSuccessfulMessage(command);
                    ArrayList<String> elementArg;
                    if (i <= commandsFromScript.size()- HumanBeing.getNumberOfFields()-2) {
                        elementArg = new ArrayList<>(commandsFromScript.subList(i+1, i + 2 + HumanBeing.getNumberOfFields()));
                    } else {
                        elementArg = new ArrayList<>(commandsFromScript.subList(i+1, commandsFromScript.size()));
                    }
                    Optional<CommandToSend> optionalCommandToSend = commandReader.readCommandsFromScript(command, elementArg);
                    optionalCommandToSend.ifPresent(this::sendCommandRequest);
                }
                scriptHistory.remove(script);
            }
        }
    }

    private void initClientCommands() {
        commandReader.addClientCommand(new ExitCommand(this));
        commandReader.addClientCommand(new HelpCommand(commandReader.getAvailableClientCommands(), commandReader.getAvailableServerCommands()));
    }

    public void turnOff() {
        this.working = false;
    }


    private HashMap<String, CommandData> getCommandsFromServer() {
        try {
            clientSocketHandler.sendRequest(new Request());
            return clientSocketHandler.receiveResponse().getAvailableCommands();
        } catch (IOException | ClassNotFoundException e) {
            OutputUtil.printErrorMessage("Unable to get server.commands from server.");
            e.printStackTrace();
            try {
                if (Validators.validateBooleanInput("Wait", scanner)) {
                    getCommandsFromServer();
                } else {
                    this.turnOff();
                }
            } catch (NoUserInputException ex) {
                this.turnOff();
            }
        }
        return null;
    }

    private boolean authorize(UserData userData, boolean registered) {
        try {
            clientSocketHandler.sendRequest(new Request(userData, registered));
            return clientSocketHandler.receiveResponse().isSuccess();
        } catch (IOException | ClassNotFoundException e) {
            OutputUtil.printErrorMessage("Unable to get response from server.");
            try {
                if (Validators.validateBooleanInput("Wait", scanner)) {
                    getCommandsFromServer();
                } else {
                    this.turnOff();
                }
            } catch (NoUserInputException ex) {
                this.turnOff();
            }
        }
        return true;
    }
}
