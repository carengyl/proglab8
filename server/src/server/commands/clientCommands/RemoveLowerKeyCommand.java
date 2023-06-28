package server.commands.clientCommands;

import common.util.udp.Request;
import common.util.udp.Response;
import common.commands.AbstractCommand;
import common.commands.ArgumentValidationFunctions;
import common.commands.CommandArgument;
import common.commands.CommandData;
import common.util.Validators;
import common.entities.CollectionManager;
import common.exceptions.InvalidNumberOfArgsException;
import common.exceptions.NoUserInputException;
import common.exceptions.ValidationException;
import server.util.CommandProcessor;

import java.io.Serializable;
import java.util.Optional;

public class RemoveLowerKeyCommand extends AbstractCommand implements Serializable {
    private final CollectionManager collection;

    public RemoveLowerKeyCommand(CommandProcessor commandProcessor) {
        super("remove_lower_key",
                "Remove elements from collection, which key is lower than @key",
                1,
                "@key - (long) unique key of element in collection",
                ArgumentValidationFunctions.VALIDATE_KEY.getValidationFunction());
        this.collection = commandProcessor.getCollectionManager();
    }

    @Override
    public Optional<Response> executeCommand(Request request) throws NoUserInputException {
        long key = request.getCommandArgument().getLongArg();
        if (collection.getHumanBeings().containsKey(key)) {
            return Optional.of(new Response(collection.removeLowerKey(key)));
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
