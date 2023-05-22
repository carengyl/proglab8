package commands.client;

import UDPutil.Response;
import commands.AbstractCommand;
import commands.CommandArgument;
import commonUtil.Validators;
import exceptions.InvalidNumberOfArgsException;
import entities.CollectionOfHumanBeings;

import java.io.Serializable;
import java.util.Optional;

public class ClearCommand extends AbstractCommand implements Serializable {
    private final CollectionOfHumanBeings collection;

    public ClearCommand(CollectionOfHumanBeings collection) {
        super("clear", "Clear collection");
        this.collection = collection;
    }

    @Override
    public Optional<Response> executeCommand(CommandArgument argument) {
        Optional<Response> optionalResponse;
        if (collection.getHumanBeings().isEmpty()) {
            optionalResponse = Optional.of(new Response("Collection is already empty"));
        } else {
            collection.getHumanBeings().clear();
            optionalResponse = Optional.of(new Response("Collection has been cleared"));
        }
        return optionalResponse;
    }

    @Override
    public CommandArgument validateArguments(CommandArgument arguments) throws InvalidNumberOfArgsException {
        Validators.validateNumberOfArgs(arguments.getNumberOfArgs(), this.getNumberOfArgs());
        return arguments;
    }
}
