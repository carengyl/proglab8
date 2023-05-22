package commands.client;

import UDPutil.Response;
import commands.AbstractCommand;
import commands.CommandArgument;
import commonUtil.Validators;
import entities.CollectionOfHumanBeings;
import exceptions.InvalidNumberOfArgsException;
import exceptions.NoUserInputException;
import exceptions.ValidationException;

import java.io.Serializable;
import java.util.Optional;

public class RemoveKeyCommand extends AbstractCommand implements Serializable {
    private final CollectionOfHumanBeings collection;

    public RemoveKeyCommand(CollectionOfHumanBeings collection) {
        super("remove_key", "Remove element from collection by @key", 1, "@key - (long) unique key of element in collection");
        this.collection = collection;
    }


    @Override
    public Optional<Response> executeCommand(CommandArgument argument) throws NoUserInputException {
        return Optional.of(new Response(collection.removeByKey(argument.getLongArg())));
    }

    @Override
    public CommandArgument validateArguments(CommandArgument arguments) throws ValidationException, InvalidNumberOfArgsException {
        Validators.validateNumberOfArgs(arguments.getNumberOfArgs(), this.getNumberOfArgs());
        long key = Validators.validateArg(arg -> (collection.getHumanBeings().containsKey((long) arg)),
                "Key not found",
                Long::parseLong,
                arguments.getArg());
        arguments.setLongArg(key);
        return arguments;
    }
}
