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

public class UpdateCommand extends AbstractCommand implements Serializable {
    private final CollectionOfHumanBeings collection;

    public UpdateCommand(CollectionOfHumanBeings collection) {
        super("update", "Update element by @id", 1, "@id - (long) id of collection element");
        this.collection = collection;
        this.setNeedsComplexData(true);
    }

    @Override
    public Optional<Response> executeCommand(CommandArgument argument) throws NoUserInputException {
        long id = argument.getLongArg();
        collection.updateById(id, argument.getHumanBeingArgument());
        return Optional.of(new Response("Updated human being by id: " + argument.getLongArg()));
    }

    @Override
    public CommandArgument validateArguments(CommandArgument arguments) throws ValidationException, InvalidNumberOfArgsException {
        Validators.validateNumberOfArgs(arguments.getNumberOfArgs(), this.getNumberOfArgs());
        long id = Validators.validateArg(arg -> (collection.checkForId((long) arg)),
                "There is no Human Being with id: " + arguments.getArg(),
                Long::parseLong,
                arguments.getArg());
        arguments.setLongArg(id);
        return arguments;
    }
}
