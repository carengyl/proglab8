package commandLine;

import clientUtil.ClientSocketHandler;
import clientUtil.CommandToSend;
import commands.AbstractCommand;
import commands.CommandArgument;
import commonUtil.OutputUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;
import java.util.Scanner;

/**
 * Class, responsible for reading commands from console and executing them
 */
public class CommandReader {
    private final HashMap<String, AbstractCommand> availableServerCommands;
    private final HashMap<String, AbstractCommand> availableClientCommands;

    public CommandReader(HashMap<String, AbstractCommand> commandsFromServer) {
        this.availableServerCommands = commandsFromServer;
        this.availableClientCommands = new HashMap<>();
    }

    public void addClientCommand(AbstractCommand command) {
        this.availableClientCommands.put(command.getCommandName(), command);
    }

    public Optional<CommandToSend> readCommandsFromConsole(Scanner scanner, ClientSocketHandler socketHandler) {
        OutputUtil.printSuccessfulMessage(">");
        String[] splitString = scanner.nextLine().replaceAll("\s{2,}", " ").strip().split(" ");
        String commandName = splitString[0];
        CommandArgument argument = new CommandArgument(Arrays.copyOfRange(splitString, 1, splitString.length));
        //In case of name coreference client commands have higher priority
        if (availableClientCommands.containsKey(commandName)) {
            AbstractCommand executableCommand = availableClientCommands.get(commandName);
            executableCommand.executeCommand(argument);
            return Optional.empty();
        } else if (availableServerCommands.containsKey(commandName)) {
            CommandToSend commandToSend = new CommandToSend(commandName, argument);
            return Optional.of(commandToSend);
        } else {
            OutputUtil.printErrorMessage("Command " + commandName + " not found. Type \"help\" to see available commands.");
            return Optional.empty();
        }
    }
}
