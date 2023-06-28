package client.util;

import common.util.udp.Request;
import common.util.udp.Response;
import common.commands.AbstractCommand;
import common.commands.CommandArgument;
import common.commands.CommandData;
import common.util.OutputUtil;
import common.util.UserData;
import common.util.Validators;
import common.exceptions.InvalidNumberOfArgsException;
import common.exceptions.NoUserInputException;
import common.exceptions.ValidationException;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.*;

public class ClientHandler {
    private final Scanner scanner = new Scanner(System.in);
    private final AuthManager authManager = new AuthManager(scanner);
    private ClientSocketHandler clientSocketHandler;
    private static final int MIN_PORT = 1;
    private static final int MAX_PORT = 65536;

    public void startClientHandler() {
        try {
            inputAddress();
            inputPort();
            boolean registered = askIfRegistered();
            UserData userData = authManager.getUserData();
            Optional<UserData> user;
            if (registered) {
                user = loginUser(userData);
            } else {
                user = registerUser(userData);
            }
            if (user.isPresent()) {
                Session session = new Session(user.get(), scanner, clientSocketHandler);
                session.startSession();
            } else {
                OutputUtil.printErrorMessage("You can't work with client unless you are not logged in. Shutting down...");
            }
        } catch (NoUserInputException e) {
            OutputUtil.printErrorMessage(e);
        }
    }

    private Optional<UserData> loginUser(UserData userData) {
        try {
            clientSocketHandler.sendRequest(new Request(userData, true));
            Response response = clientSocketHandler.receiveResponse();
            if (response.isSuccess()) {
                OutputUtil.printSuccessfulMessage(response);
                return Optional.of(userData);
            } else {
                OutputUtil.printErrorMessage(response);
                return Optional.empty();
            }
        } catch (IOException | ClassNotFoundException e) {
            OutputUtil.printErrorMessage(e.getMessage());
            return Optional.empty();
        }
    }

    private Optional<UserData> registerUser(UserData userData) {
        try {
            clientSocketHandler.sendRequest(new Request(userData, false));
            Response response = clientSocketHandler.receiveResponse();
            if (response.isSuccess()) {
                OutputUtil.printSuccessfulMessage(response);
                return Optional.of(userData);
            } else {
                OutputUtil.printErrorMessage(response);
                return Optional.empty();
            }
        } catch (IOException | ClassNotFoundException e) {
            OutputUtil.printErrorMessage(e.getMessage());
            return Optional.empty();
        }
    }

    private void inputPort() throws NoUserInputException {
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
    }

    private void inputAddress() throws NoUserInputException {
        try {
            boolean useDefaultServer = Validators.validateBooleanInput("Do you want to use a default server address", scanner);
            clientSocketHandler = new ClientSocketHandler();
            if (!useDefaultServer) {
                OutputUtil.printSuccessfulMessage("Please enter the server's internet address");
                String address = scanner.nextLine();
                clientSocketHandler.setAddress(address);
            }
        } catch (NoSuchElementException e) {
            throw new NoUserInputException();
        } catch (SocketException e) {
            OutputUtil.printErrorMessage("Problem with opening server port, try again");
            inputAddress();
        } catch (UnknownHostException e) {
            OutputUtil.printErrorMessage("Unknown address, try again.");
            inputAddress();
        }
    }

    private boolean askIfRegistered() throws NoUserInputException {
        return Validators.validateBooleanInput("Do you have an account", scanner);
    }

    /**
     * Class, responsible for reading server.commands from console and executing them
     */
    public static class CommandReader {

        private final HashMap<String, CommandData> availableServerCommands;
        private final HashMap<String, AbstractCommand> availableClientCommands;

        public CommandReader(HashMap<String, CommandData> commandsFromServer) {
            this.availableServerCommands = commandsFromServer;
            this.availableClientCommands = new HashMap<>();
        }

        public void addClientCommand(AbstractCommand command) {
            this.availableClientCommands.put(command.getCommandData().commandName(), command);
        }

        public Optional<CommandToSend> readCommandsFromScript(String string, ArrayList<String> elementArg) {
            String[] splitString = string.replaceAll("\s{2,}", " ").strip().split(" ");
            String commandName = splitString[0];
            CommandArgument argument = new CommandArgument(Arrays.copyOfRange(splitString, 1, splitString.length));
            argument.setElementArgument(elementArg);
            if (availableServerCommands.containsKey(commandName)) {
                Optional<CommandToSend> commandToSend = getCommandToSend(commandName, argument);
                if (commandToSend.isPresent()) return commandToSend;
            }
            return Optional.empty();
        }

        private Optional<CommandToSend> getCommandToSend(String commandName, CommandArgument argument) {
            try {
                CommandData executableCommandData = availableServerCommands.get(commandName);
                CommandArgument validatedArg = executableCommandData.validationFunction().apply(argument, executableCommandData);
                CommandToSend commandToSend = new CommandToSend(commandName, validatedArg);
                return Optional.of(commandToSend);
            } catch (ValidationException | InvalidNumberOfArgsException e) {
                OutputUtil.printErrorMessage(e.getMessage());
            }
            return Optional.empty();
        }

        public Optional<CommandToSend> readCommandsFromConsole(Scanner scanner) throws NoUserInputException {
            OutputUtil.printSuccessfulMessageOneStrip("> ");
            String[] splitString = scanner.nextLine().replaceAll("\s{2,}", " ").strip().split(" ");
            String commandName = splitString[0];
            CommandArgument argument = new CommandArgument(Arrays.copyOfRange(splitString, 1, splitString.length));
            //In case of name coreference client server.commands have higher priority
            if (availableClientCommands.containsKey(commandName)) {
                try {
                    AbstractCommand executableCommand = availableClientCommands.get(commandName);
                    CommandArgument validatedArgs = executableCommand.validateArguments(argument, executableCommand.getCommandData());
                    executableCommand.executeCommand(new Request(commandName, validatedArgs));
                    return Optional.empty();
                } catch (ValidationException | InvalidNumberOfArgsException e) {
                    OutputUtil.printErrorMessage(e.getMessage());
                }
            } else if (availableServerCommands.containsKey(commandName)) {
                Optional<CommandToSend> commandToSend = getCommandToSend(commandName, argument);
                if (commandToSend.isPresent()) return commandToSend;
            } else {
                OutputUtil.printErrorMessage("Command " + commandName + " not found. Type \"help\" to see available server.commands.");
            }
            return Optional.empty();
        }

        public HashMap<String, AbstractCommand> getAvailableClientCommands() {
            return availableClientCommands;
        }

        public HashMap<String, CommandData> getAvailableServerCommands() {
            return availableServerCommands;
        }
    }
}
