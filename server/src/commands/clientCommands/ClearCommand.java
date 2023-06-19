package commands.clientCommands;

import UDPutil.Response;
import commands.AbstractCommand;
import commands.ArgumentValidationFunctions;
import commands.CommandArgument;
import commands.CommandData;
import commonUtil.Validators;
import entities.CollectionManager;
import exceptions.InvalidNumberOfArgsException;

import java.io.Serializable;
import java.util.Optional;

public class ClearCommand extends AbstractCommand implements Serializable {
    private final CollectionManager collection;

    public ClearCommand(CollectionManager collection) {
        super("clear", "Clear collection", ArgumentValidationFunctions.VALIDATE_NUMBER_OF_ARGS.getValidationFunction());
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
    public CommandArgument validateArguments(CommandArgument arguments, CommandData commandData) throws InvalidNumberOfArgsException {
        Validators.validateNumberOfArgs(arguments.getNumberOfArgs(), commandData.numberOfArgs());
        return arguments;
    }
}
