package clientUtil;

import UDPutil.Request;
import UDPutil.Response;
import commandLine.CommandReader;
import commands.CommandArgument;
import commands.CommandData;
import commonUtil.HumanBeingFactory;
import commonUtil.OutputUtil;
import commonUtil.Validators;
import exceptions.NoUserInputException;
import localCommands.ExitCommand;
import localCommands.HelpCommand;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.time.LocalTime;
import java.util.*;

public class ClientHandler {
    private static final Set<Path> scriptHistory = new HashSet<>();
    private final ScriptReader scriptReader = new ScriptReader();
    private final Scanner scanner = new Scanner(System.in);
    private CommandReader commandReader;
    private ClientSocketHandler clientSocketHandler;
    private final RequestCreator requestCreator = new RequestCreator();
    private static final int MIN_PORT = 1;
    private static final int MAX_PORT = 65536;
    private boolean working = true;

    public void start() {
        this.inputAddress();
        this.inputPort();
        commandReader = new CommandReader(getCommandsFromServer());
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
                            this.toggleStatus();
                        }
                    }
                }
            } catch (NoUserInputException e) {
                OutputUtil.printErrorMessage(e.getMessage());
                toggleStatus();
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
                Path scriptFileName = response.getCommandArgument().getFileName();
                readCommandsFromScript(scriptFileName);
            }
            if (response.hasRequestHumanBeing()) {
                if (sendHumanBeingRequest(response.getCommandData(), response.getCommandArgument())) {
                    receiveResponse();
                }
            }
            OutputUtil.printSuccessfulMessage(response.toString());
        } catch (SocketTimeoutException socketTimeoutException) {
            OutputUtil.printErrorMessage("The waiting time for a response from the server has been exceeded, try again later");
        } catch (IOException ioException) {
            OutputUtil.printErrorMessage("An error occurred while receiving a response from the server:");
            ioException.printStackTrace();
        } catch (ClassNotFoundException classNotFoundException) {
            OutputUtil.printErrorMessage("The response came damaged");
        }
    }

    private void readCommandsFromScript(Path scriptFileName) throws IOException {
        scriptHistory.add(scriptFileName);
        if (scriptHistory.contains(scriptFileName)) {
            OutputUtil.printErrorMessage("Loop possible");
        }
        else {
            scriptHistory.add(scriptFileName);
            scriptReader.readCommandsFromFile(scriptFileName);

        }
    }

    private void initClientCommands() {
        commandReader.addClientCommand(new ExitCommand(this));
        commandReader.addClientCommand(new HelpCommand(commandReader.getAvailableClientCommands(), commandReader.getAvailableServerCommands()));
    }

    public void toggleStatus() {
        this.working = !working;
    }

    private void inputAddress() {
        try {
            boolean useDefaultServer = Validators.validateBooleanInput("Do you want to use a default server address", scanner);
            clientSocketHandler = new ClientSocketHandler();
            if (!useDefaultServer) {
                OutputUtil.printSuccessfulMessage("Please enter the server's internet address");
                String address = scanner.nextLine();
                clientSocketHandler.setAddress(address);
            }
        } catch (NoUserInputException | NoSuchElementException e) {
            OutputUtil.printErrorMessage(e.getMessage());
            toggleStatus();
        } catch (SocketException e) {
            OutputUtil.printErrorMessage("Problem with opening server port, try again");
            inputAddress();
        } catch (UnknownHostException e) {
            OutputUtil.printErrorMessage("Unknown address, try again.");
            inputAddress();
        }
    }

    private void inputPort() {
        try {
            boolean useDefaultPort = Validators.validateBooleanInput("Do you want to use a default server port", scanner);
            if (!useDefaultPort) {
                Integer port = Validators.validateInput(arg -> (((int) arg) <= MAX_PORT) && (((int) arg) >= MIN_PORT),
                "Enter remote host port, it should be in [" + MIN_PORT + ";" + MAX_PORT + "]",
                        "Expected integer value",
                        "Remote host port should be in [" + MIN_PORT + ";" + MAX_PORT + "]",
                        Integer::parseInt,
                        false,
                        scanner);
                //noinspection ConstantConditions, NullPointer handled in Validators class
                clientSocketHandler.setPort(port);
            }
        } catch (NoUserInputException |NoSuchElementException e) {
            OutputUtil.printErrorMessage(e.getMessage());
            toggleStatus();
        }
    }

    private HashMap<String, CommandData> getCommandsFromServer() {
        try {
            clientSocketHandler.sendRequest(new Request());
            return clientSocketHandler.receiveResponse().getAvailableCommands();
        } catch (IOException | ClassNotFoundException e) {
            OutputUtil.printErrorMessage("Unable to get commands from server.");
            try {
                if (Validators.validateBooleanInput("Wait", scanner)) {
                    getCommandsFromServer();
                } else {
                    OutputUtil.printSuccessfulMessage("Shutting down...");
                    this.toggleStatus();
                }
            } catch (NoUserInputException ex) {
                throw new RuntimeException(ex);
            }
        }
        return null;
    }
}
