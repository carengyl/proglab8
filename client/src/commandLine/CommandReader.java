package commandLine;

import clientUtil.CommandToSend;
import commands.AbstractCommand;
import commands.CommandArgument;
import commonUtil.OutputUtil;
import exceptions.InvalidNumberOfArgsException;
import exceptions.NoUserInputException;
import exceptions.ValidationException;

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

    public Optional<CommandToSend> readCommandsFromConsole(Scanner scanner) throws NoUserInputException {
        OutputUtil.printSuccessfulMessageOneStrip("> ");
        String[] splitString = scanner.nextLine().replaceAll("\s{2,}", " ").strip().split(" ");
        String commandName = splitString[0];
        CommandArgument argument = new CommandArgument(Arrays.copyOfRange(splitString, 1, splitString.length));
        //In case of name coreference client commands have higher priority
        if (availableClientCommands.containsKey(commandName)) {
            try {
                AbstractCommand executableCommand = availableClientCommands.get(commandName);
                CommandArgument validatedArgs = executableCommand.validateArguments(argument);
                executableCommand.executeCommand(validatedArgs);
                return Optional.empty();
            } catch (ValidationException | InvalidNumberOfArgsException e) {
                OutputUtil.printErrorMessage(e.getMessage());
            }
        } else if (availableServerCommands.containsKey(commandName)) {
            try {
                AbstractCommand command = availableServerCommands.get(commandName);
                CommandArgument validatedArg = command.validateArguments(argument);
                CommandToSend commandToSend = new CommandToSend(commandName, validatedArg);
                return Optional.of(commandToSend);
            } catch (ValidationException | InvalidNumberOfArgsException e) {
                OutputUtil.printErrorMessage(e.getMessage());
            }
        }
        OutputUtil.printErrorMessage("Command " + commandName + " not found. Type \"help\" to see available commands.");
        return Optional.empty();
    }
}
