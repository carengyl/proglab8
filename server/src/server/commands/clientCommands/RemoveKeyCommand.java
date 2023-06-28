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

public class RemoveKeyCommand extends AbstractCommand implements Serializable {
    private final CommandProcessor commandProcessor;

    public RemoveKeyCommand(CommandProcessor commandProcessor) {
        super("remove_key",
                "Remove element from collection by @key",
                1,
                "@key - (long) unique key of element in collection",
                ArgumentValidationFunctions.VALIDATE_KEY.getValidationFunction());
        this.commandProcessor = commandProcessor;
    }

    @Override
    public Optional<Response> executeCommand(Request request) throws NoUserInputException {
        long key = request.getCommandArgument().getLongArg();
        if (commandProcessor.getCollectionManager().getHumanBeings().containsKey(key)) {
            request.getCommandArgument().setLongArg(commandProcessor.getCollectionManager().getHumanBeings().get(key).getId());
            return Optional.of(commandProcessor.removeById(request));
        } else {
            return Optional.of(new Response("Key not found"));
        }
    }

    @Override
    public CommandArgument validateArguments(CommandArgument arguments, CommandData commandData) throws ValidationException, InvalidNumberOfArgsException {
        Validators.validateNumberOfArgs(arguments.getNumberOfArgs(), commandData.numberOfArgs());
        long key = Validators.validateArg(arg -> true,
                "Key not found",
                Long::parseLong,
                arguments.getArg());
        arguments.setLongArg(key);
        return arguments;
    }
}
