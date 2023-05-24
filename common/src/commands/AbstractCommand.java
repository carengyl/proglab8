package commands;

import UDPutil.Response;
import exceptions.InvalidNumberOfArgsException;
import exceptions.NoUserInputException;
import exceptions.ValidationException;

import java.io.Serializable;
import java.util.Optional;

public abstract class AbstractCommand implements Serializable {
    private final CommandData commandData;

    public AbstractCommand(String name, String description) {
        this.commandData = new CommandData(name,
                description,
                0,
                "",
                this::validateArguments);

    }

    public AbstractCommand(String commandName, String commandDescription, int numberOfArgs, String argsDescription) {
        this.commandData = new CommandData(commandName,
                commandDescription,
                numberOfArgs,
                argsDescription,
                this::validateArguments);
    }

    public abstract Optional<Response> executeCommand(CommandArgument argument) throws NoUserInputException;

    public abstract CommandArgument validateArguments(CommandArgument argument, CommandData commandData) throws ValidationException, InvalidNumberOfArgsException;

    public CommandData getCommandData() {
        return commandData;
    }

    @Override
    public String toString() {
        return commandData.toString();
    }
}
