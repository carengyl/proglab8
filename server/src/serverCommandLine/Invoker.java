package serverCommandLine;

import commands.AbstractCommand;
import commands.CommandArgument;
import commonUtil.OutputUtil;
import exceptions.InvalidNumberOfArgsException;
import exceptions.NoUserInputException;
import exceptions.ValidationException;

import java.util.HashMap;

public class Invoker {
    private final HashMap<String, AbstractCommand> SERVER_AVAILABLE_COMMAND = new HashMap<>();

    public void addServerCommand(AbstractCommand command) {
        this.SERVER_AVAILABLE_COMMAND.put(command.getCommandName(), command);
    }

    public void performCommand(String commandName, CommandArgument argument) throws NoUserInputException {
        if (SERVER_AVAILABLE_COMMAND.containsKey(commandName)) {
            try {
                AbstractCommand executableCommand = SERVER_AVAILABLE_COMMAND.get(commandName);
                CommandArgument validatedArg = executableCommand.validateArguments(argument);
                executableCommand.executeCommand(validatedArg);
            } catch (ValidationException | InvalidNumberOfArgsException e) {
                OutputUtil.printErrorMessage(e.getMessage());
            }
        } else {
            OutputUtil.printErrorMessage("Command " + commandName + " not found. Type \"help\" to see available commands.");
        }
    }
}
