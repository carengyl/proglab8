package commands.clientCommands;

import UDPutil.Response;
import commands.AbstractCommand;
import commands.ArgumentValidationFunctions;
import commands.CommandArgument;
import commands.CommandData;
import commonUtil.Validators;
import entities.CollectionManager;
import exceptions.InvalidNumberOfArgsException;
import exceptions.NoUserInputException;
import exceptions.ValidationException;

import java.io.Serializable;
import java.util.Optional;

public class RemoveKeyCommand extends AbstractCommand implements Serializable {
    private final CollectionManager collection;

    public RemoveKeyCommand(CollectionManager collection) {
        super("remove_key",
                "Remove element from collection by @key",
                1,
                "@key - (long) unique key of element in collection",
                ArgumentValidationFunctions.VALIDATE_KEY.getValidationFunction());
        this.collection = collection;
    }

    @Override
    public Optional<Response> executeCommand(CommandArgument argument) throws NoUserInputException {
        long key = argument.getLongArg();
        if (collection.getHumanBeings().containsKey(key)) {
            return Optional.of(new Response(collection.removeByKey(key)));
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
