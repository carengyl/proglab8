package clientUtil;

import UDPutil.Request;
import UDPutil.Response;
import commonUtil.OutputUtil;
import commonUtil.UserData;
import commonUtil.Validators;
import exceptions.NoUserInputException;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Scanner;

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
}
