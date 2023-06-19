package serverUtil;

import commands.clientCommands.*;
import commonUtil.OutputUtil;
import commonUtil.Validators;
import dataBaseUtil.DBManager;
import dataBaseUtil.DBSSHConnector;
import dataBaseUtil.interfaces.DBConnectable;
import entities.CollectionManager;
import exceptions.DatabaseException;
import exceptions.NoUserInputException;
import serverCommandLine.CommandManager;
import serverCommandLine.ServerCommandReader;
import serverCommands.ServerExitCommand;
import serverCommands.ServerHelpCommand;
import serverCommands.ServerSaveCommand;
import threads.ConsoleThread;
import threads.RequestThread;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ServerHandler {
    private final Scanner scanner = new Scanner(System.in);
    private final int MAX_PORT = 65535;
    private final int MIN_PORT = 1;
    private final DBConnectable dbConnector;
    private final DBManager dbManager;
    private final UsersManager usersManager;
    private ServerSocketHandler serverSocketHandler;
    private CommandProcessor commandProcessor;
    private final ServerCommandReader commandReader;
    private final serverCommandLine.CommandManager commandManager;
    private final CollectionManager collectionManager;
    private static boolean running = true;

    {
        dbConnector = new DBSSHConnector();
        collectionManager = new CollectionManager();
        dbManager = new DBManager(dbConnector);
        usersManager = new UsersManager(dbManager);
        commandProcessor = new CommandProcessor(dbManager, collectionManager);

        commandManager = new CommandManager(commandProcessor);

        this.commandReader = new ServerCommandReader(commandManager, scanner);

        try {
            collectionManager.setHumanBeings(dbManager.loadCollection());
        } catch (DatabaseException e) {
            OutputUtil.printErrorMessage(e.getMessage());
            System.exit(1);
        }
    }

    public static boolean isRunning() {
        return running;
    }

    public static void turnOff() {
        ServerHandler.running = false;
    }

    private void initCommands() {
        commandManager.addClientCommand(new ClearCommand(collectionManager));
        commandManager.addClientCommand(new CountGreaterThanMoodCommand(collectionManager));
        commandManager.addClientCommand(new CountLessThanMinutesOfWaitingCommand(collectionManager));
        commandManager.addClientCommand(new FilterByCarCommand(collectionManager));

        commandManager.addClientCommand(new InfoCommand(collectionManager));
        commandManager.addClientCommand(new InsertCommand(collectionManager));
        commandManager.addClientCommand(new RemoveGreaterCommand(collectionManager));
        commandManager.addClientCommand(new RemoveKeyCommand(collectionManager));

        commandManager.addClientCommand(new RemoveLowerKeyCommand(collectionManager));
        commandManager.addClientCommand(new ShowCommand(collectionManager));
        commandManager.addClientCommand(new UpdateCommand(collectionManager));
        commandManager.addClientCommand(new ExecuteScriptCommand());

        commandManager.addServerCommand(new ServerExitCommand());
        commandManager.addServerCommand(new ServerSaveCommand(collectionManager));
        commandManager.addServerCommand(new ServerHelpCommand(commandManager.getSERVER_AVAILABLE_COMMAND()));
    }

    public void startServerHandler() {
        try {
            inputPort();
            Thread requestThread = new Thread(new RequestThread(commandManager, serverSocketHandler, usersManager));
            Thread consoleThread = new Thread(new ConsoleThread(commandReader));
            requestThread.start();
            consoleThread.start();
        } catch (IOException e) {
            OutputUtil.printErrorMessage(e.getMessage());
            System.exit(1);
        }
    }

    private void inputPort() throws IOException {
        try {
            boolean useDefaultPort = Validators.validateBooleanInput("Do you want to use a default server port", scanner);
            serverSocketHandler = new ServerSocketHandler();
            if (!useDefaultPort) {
                Integer port = Validators.validateInput(arg -> (((int) arg) <= MAX_PORT) && (((int) arg) >= MIN_PORT),
                        "Enter remote host port, it should be in [" + MIN_PORT + ";" + MAX_PORT + "]",
                        "Expected integer value",
                        "Remote host port should be in [" + MIN_PORT + ";" + MAX_PORT + "]",
                        Integer::parseInt,
                        false,
                        scanner);
                //noinspection ConstantConditions, NullPointer handled in Validators class
                serverSocketHandler.setSelfPort(port);
            }
        } catch (NoUserInputException | NoSuchElementException e) {
            OutputUtil.printErrorMessage(e.getMessage());
            System.exit(1);
        }
    }
}
