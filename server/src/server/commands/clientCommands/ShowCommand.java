package server.commands.clientCommands;

import common.util.udp.Request;
import common.util.udp.Response;
import common.commands.AbstractCommand;
import common.commands.ArgumentValidationFunctions;
import common.commands.CommandArgument;
import common.commands.CommandData;
import common.util.Validators;
import common.exceptions.InvalidNumberOfArgsException;
import common.exceptions.NoUserInputException;
import common.exceptions.ValidationException;
import server.util.CommandProcessor;

import java.io.Serializable;
import java.util.Optional;

public class ShowCommand extends AbstractCommand implements Serializable {
    private final CommandProcessor commandProcessor;
    public ShowCommand(CommandProcessor commandProcessor) {
        super("show", "Show data about collection and elements",
                ArgumentValidationFunctions.VALIDATE_NUMBER_OF_ARGS.getValidationFunction());
        this.commandProcessor = commandProcessor;
    }

    @Override
    public Optional<Response> executeCommand(Request request) throws NoUserInputException {
        return Optional.of(commandProcessor.show(request));
    }

    @Override
    public CommandArgument validateArguments(CommandArgument arguments, CommandData commandData) throws ValidationException, InvalidNumberOfArgsException {
        Validators.validateNumberOfArgs(arguments.getNumberOfArgs(), commandData.numberOfArgs());
        return arguments;
    }
}
