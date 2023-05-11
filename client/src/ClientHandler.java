import commandLine.CommandReader;
import exceptions.NoUserInputException;
import clientUtil.ClientSocketHandler;
import commonUtil.OutputUtil;
import commonUtil.Validators;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ClientHandler {
    private final Scanner scanner = new Scanner(System.in);
    private final CommandReader commandReader = new CommandReader();

    private static final int MIN_PORT = 1;
    private final int MAX_PORT = 2^16;

    private ClientSocketHandler clientSocketHandler;
    private boolean working = true;

    public void start() {
        inputAddress();
        inputPort();
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
        } catch (NoUserInputException |NoSuchElementException e) {
            OutputUtil.printErrorMessage(e.getMessage());
            toggleStatus();
        } catch (SocketException e) {
            OutputUtil.printErrorMessage("Problem with opening server port, try again");
        } catch (UnknownHostException e) {
            OutputUtil.printErrorMessage("Unknown address, try again.");
            inputAddress();
        }
    }

    private void inputPort() {
        try {
            boolean useDefaultPort = Validators.validateBooleanInput("Do you want to use a default server address", scanner);
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
}
