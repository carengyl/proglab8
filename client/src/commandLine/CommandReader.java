package commandLine;

import UDPutil.Request;
import clientUtil.CommandToSend;
import commands.AbstractCommand;
import commands.CommandArgument;
import commands.CommandData;
import commonUtil.OutputUtil;
import exceptions.InvalidNumberOfArgsException;
import exceptions.NoUserInputException;
import exceptions.ValidationException;

import java.util.*;

/**
 * Class, responsible for reading commands from console and executing them
 */
public class CommandReader {

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
        //In case of name coreference client commands have higher priority
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
            OutputUtil.printErrorMessage("Command " + commandName + " not found. Type \"help\" to see available commands.");
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
