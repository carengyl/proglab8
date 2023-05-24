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

public class RemoveGreaterCommand extends AbstractCommand implements Serializable {
    private final CollectionOfHumanBeings collection;

    public RemoveGreaterCommand(CollectionOfHumanBeings collection) {
        super("remove_greater", "Remove all elements, which are greater than {element}");
        this.collection = collection;
    }

    @Override
    public Optional<Response> executeCommand(CommandArgument argument) throws NoUserInputException {
        if (argument.getHumanBeingArgument() != null) {
            StringBuilder stringBuilder = new StringBuilder();
            for (long key : collection.getHumanBeings().keySet()) {
                if (collection.getHumanBeings().get(key).compareTo(argument.getHumanBeingArgument()) > 0) {
                    stringBuilder.append(collection.removeByKey(key)).append("\n");
                }
            }
            return Optional.of(new Response(stringBuilder.toString()));
        }
        else {
            return Optional.of(new Response(this.getCommandData(), argument));
        }
    }
    @Override
    public CommandArgument validateArguments(CommandArgument arguments, CommandData commandData) throws ValidationException, InvalidNumberOfArgsException {
        Validators.validateNumberOfArgs(arguments.getNumberOfArgs(), commandData.numberOfArgs());
        return arguments;
    }
}
