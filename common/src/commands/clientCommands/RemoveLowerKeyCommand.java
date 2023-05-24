package commands.clientCommands;

import UDPutil.Response;
import commands.AbstractCommand;
import commands.CommandArgument;
import commands.CommandData;
import commonUtil.Validators;
import entities.CollectionOfHumanBeings;
import exceptions.InvalidNumberOfArgsException;
import exceptions.NoUserInputException;
import exceptions.ValidationException;

import java.io.Serializable;
import java.util.Optional;

public class RemoveLowerKeyCommand extends AbstractCommand implements Serializable {
    private final CollectionOfHumanBeings collection;
    //TO-DO record description, function validate
    //TO-DO execute_script, logic on server, read on client
    public RemoveLowerKeyCommand(CollectionOfHumanBeings collection) {
        super("remove_lower_key", "Remove elements from collection, which key is lower than @key", 1, "@key - (long) unique key of element in collection");
        this.collection = collection;
    }

    @Override
    public Optional<Response> executeCommand(CommandArgument argument) throws NoUserInputException {
        long key = argument.getLongArg();
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
