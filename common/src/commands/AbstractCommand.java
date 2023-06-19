package commands;

import UDPutil.Request;
import UDPutil.Response;
import exceptions.InvalidNumberOfArgsException;
import exceptions.NoUserInputException;
import exceptions.ValidationException;

import java.io.Serializable;
import java.util.Optional;

public abstract class AbstractCommand implements Serializable {
    private final CommandData commandData;

    public AbstractCommand(String name, String description, SerializableBiFunction<CommandArgument, CommandData, CommandArgument> validationFunction) {
        this.commandData = new CommandData(name,
                description,
                0,
                "",
                validationFunction);
    }

    public AbstractCommand(String commandName, String commandDescription, int numberOfArgs, String argsDescription, SerializableBiFunction<CommandArgument, CommandData, CommandArgument> validationFunction) {
        this.commandData = new CommandData(commandName,
                commandDescription,
                numberOfArgs,
                argsDescription,
                validationFunction);
    }

    public abstract Optional<Response> executeCommand(Request request) throws NoUserInputException;

    public abstract CommandArgument validateArguments(CommandArgument argument, CommandData commandData) throws ValidationException, InvalidNumberOfArgsException;

    public CommandData getCommandData() {
        return commandData;
    }

    @Override
    public String toString() {
        return commandData.toString();
    }
}
