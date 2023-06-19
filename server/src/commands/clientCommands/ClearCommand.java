package commands.clientCommands;

import UDPutil.Request;
import UDPutil.Response;
import commands.AbstractCommand;
import commands.ArgumentValidationFunctions;
import commands.CommandArgument;
import commands.CommandData;
import commonUtil.Validators;
import exceptions.InvalidNumberOfArgsException;
import serverUtil.CommandProcessor;

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
