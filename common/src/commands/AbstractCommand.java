package commands;

import UDPutil.Response;
import exceptions.InvalidNumberOfArgsException;
import exceptions.NoUserInputException;
import exceptions.ValidationException;

import java.util.Optional;

public abstract class AbstractCommand {
    private final String commandName;
    private final String commandDescription;
    private final int numberOfArgs;
    private final String argsDescription;

    public AbstractCommand(String name, String description) {
        this.commandName = name;
        this.commandDescription = description;
        this.numberOfArgs = 0;
        this.argsDescription = "";
    }

    public AbstractCommand(String commandName, String commandDescription, int numberOfArgs, String argsDescription) {
        this.commandName = commandName;
        this.commandDescription = commandDescription;
        this.numberOfArgs = numberOfArgs;
        this.argsDescription = argsDescription;
    }

    public abstract Optional<Response> executeCommand(CommandArgument argument) throws NoUserInputException;
    public abstract CommandArgument validateArguments(CommandArgument arguments) throws ValidationException, InvalidNumberOfArgsException;

    public int getNumberOfArgs() {
        return numberOfArgs;
    }

    public String getCommandDescription() {
        return commandDescription;
    }

    public String getArgsDescription() {
        return argsDescription;
    }

    public String getCommandName() {
        return commandName;
    }
}
