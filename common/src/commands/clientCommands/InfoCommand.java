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

public class InfoCommand extends AbstractCommand implements Serializable {

    private final CollectionOfHumanBeings collection;

    public InfoCommand(CollectionOfHumanBeings collection) {
        super("info", "Show info about collection");
        this.collection = collection;
    }

    @Override
    public Optional<Response> executeCommand(CommandArgument argument) throws NoUserInputException {
        return Optional.of(new Response(collection.toString()));
    }

    @Override
    public CommandArgument validateArguments(CommandArgument arguments, CommandData commandData) throws ValidationException, InvalidNumberOfArgsException {
        Validators.validateNumberOfArgs(arguments.getNumberOfArgs(), commandData.numberOfArgs());
        return arguments;
    }
}
