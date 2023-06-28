package server.commands.clientCommands;

import common.util.udp.Request;
import common.util.udp.Response;
import common.commands.AbstractCommand;
import common.commands.ArgumentValidationFunctions;
import common.commands.CommandArgument;
import common.commands.CommandData;
import common.util.Validators;
import common.exceptions.InvalidNumberOfArgsException;
import server.util.CommandProcessor;

import java.io.Serializable;
import java.util.Optional;

public class ClearCommand extends AbstractCommand implements Serializable {
    private final CommandProcessor commandProcessor;

    public ClearCommand(CommandProcessor commandProcessor) {
        super("clear", "Clear collection", ArgumentValidationFunctions.VALIDATE_NUMBER_OF_ARGS.getValidationFunction());
        this.commandProcessor = commandProcessor;
    }

    @Override
    public Optional<Response> executeCommand(Request request) {
        return Optional.of(commandProcessor.clear(request));
    }

    @Override
    public CommandArgument validateArguments(CommandArgument arguments, CommandData commandData) throws InvalidNumberOfArgsException {
        Validators.validateNumberOfArgs(arguments.getNumberOfArgs(), commandData.numberOfArgs());
        return arguments;
    }
}
