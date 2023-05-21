import UDPutil.Request;
import clientUtil.ClientSocketHandler;
import clientUtil.CommandToSend;
import commandLine.CommandReader;
import commands.AbstractCommand;
import commonUtil.OutputUtil;
import commonUtil.Validators;
import exceptions.NoUserInputException;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Scanner;

public class ClientHandler {
    private final Scanner scanner = new Scanner(System.in);
    private CommandReader commandReader;
    private ClientSocketHandler clientSocketHandler;
    private static final int MIN_PORT = 1;
    private final int MAX_PORT = 65536;

    private boolean working = true;

    public void start() {
        inputAddress();
        inputPort();
        try {
            commandReader = new CommandReader(getCommandsFromServer());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        while (working) {
            Optional<CommandToSend> optionalCommand = commandReader.readCommandsFromConsole(scanner, clientSocketHandler);
            if (optionalCommand.isPresent()) {

            }
        }
    }

    private void initClientCommands() {
        //commands
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
                clientSocketHandler.setPort(port);
            }
        } catch (NoUserInputException |NoSuchElementException e) {
            OutputUtil.printErrorMessage(e.getMessage());
            toggleStatus();
        }
    }

    private HashMap<String, AbstractCommand> getCommandsFromServer() throws IOException {
        clientSocketHandler.sendRequest(new Request());
        return clientSocketHandler.receiveResponse().getAvailableCommands();
    }
}
