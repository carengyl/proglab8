package commands.clientCommands;

import UDPutil.Request;
import UDPutil.Response;
import commands.AbstractCommand;
import commands.ArgumentValidationFunctions;
import commands.CommandArgument;
import commands.CommandData;
import commonUtil.Validators;
import exceptions.InvalidNumberOfArgsException;
import exceptions.NoUserInputException;
import exceptions.ValidationException;
import serverUtil.CommandProcessor;

import java.io.Serializable;
import java.util.Optional;

public class InfoCommand extends AbstractCommand implements Serializable {

    private final CommandProcessor commandProcessor;

    public InfoCommand(CommandProcessor commandProcessor) {
        super("info", "Show info about collection", ArgumentValidationFunctions.VALIDATE_NUMBER_OF_ARGS.getValidationFunction());
        this.commandProcessor = commandProcessor;
    }

    @Override
    public Optional<Response> executeCommand(Request request) throws NoUserInputException {
        return Optional.of(commandProcessor.info(request));
    }

    @Override
    public CommandArgument validateArguments(CommandArgument arguments, CommandData commandData) throws ValidationException, InvalidNumberOfArgsException {
        Validators.validateNumberOfArgs(arguments.getNumberOfArgs(), commandData.numberOfArgs());
        return arguments;
    }
}
