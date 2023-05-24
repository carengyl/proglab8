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

public class ShowCommand extends AbstractCommand implements Serializable {
    private final CollectionOfHumanBeings collection;
    public ShowCommand(CollectionOfHumanBeings collection) {
        super("show", "Show data about collection and elements");
        this.collection = collection;
    }

    @Override
    public Optional<Response> executeCommand(CommandArgument argument) throws NoUserInputException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Collection from file: ").append(collection.getFileName()).append("\n");
        if (!collection.getHumanBeings().isEmpty()) {
            for (long key : collection.getHumanBeings().keySet()) {
                stringBuilder.append("Key: ").append(key).append("; ").append(collection.getHumanBeings().get(key).toString()).append("\n");
            }
        } else {
            stringBuilder.append("Collection is empty");
        }
        return Optional.of(new Response(stringBuilder.toString()));
    }

    @Override
    public CommandArgument validateArguments(CommandArgument arguments, CommandData commandData) throws ValidationException, InvalidNumberOfArgsException {
        Validators.validateNumberOfArgs(arguments.getNumberOfArgs(), commandData.numberOfArgs());
        return arguments;
    }
}
