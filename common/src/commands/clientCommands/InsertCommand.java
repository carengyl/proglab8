package commands.clientCommands;

import UDPutil.Response;
import commands.AbstractCommand;
import commands.CommandArgument;
import commands.CommandData;
import commonUtil.HumanBeingFactory;
import commonUtil.Validators;
import entities.CollectionOfHumanBeings;
import exceptions.InvalidNumberOfArgsException;
import exceptions.NoUserInputException;
import exceptions.ValidationException;

import java.io.Serializable;
import java.util.Optional;

public class InsertCommand extends AbstractCommand implements Serializable {
    private final CollectionOfHumanBeings collection;

    public InsertCommand(CollectionOfHumanBeings collection) {
        super("insert", "Add element to collection by @key", 1, "@key - unique long of element");
        this.collection = collection;
    }

    @Override
    public Optional<Response> executeCommand(CommandArgument argument) throws NoUserInputException {
        long key = argument.getLongArg();
        if (!collection.getHumanBeings().containsKey(key)) {
            if (argument.getHumanBeingArgument() != null) {
                collection.addByKey(key, argument.getHumanBeingArgument());
                return Optional.of(new Response("Added Human Being by key: " + key,
                        argument.getHumanBeingArgument()));
            } else if (argument.getElementArgument() != null) {
                HumanBeingFactory humanBeingFactory = new HumanBeingFactory();
                try {
                    humanBeingFactory.setVariables(argument.getElementArgument());
                    collection.addByKey(key, humanBeingFactory.getCreatedHumanBeing());
                    return Optional.of(new Response("Added Human Being by key: " + key, humanBeingFactory.getCreatedHumanBeing()));
                } catch (ValidationException e) {
                    return Optional.of(new Response(e.getMessage()));
                }
            } else {
                return Optional.of(new Response(this.getCommandData(), argument));
            }
        } else {
            return Optional.of(new Response("Key isn't unique"));
        }
    }

    @Override
    public CommandArgument validateArguments(CommandArgument arguments, CommandData commandData) throws ValidationException, InvalidNumberOfArgsException {
        Validators.validateNumberOfArgs(arguments.getNumberOfArgs(), commandData.numberOfArgs());
        long key = Validators.validateArg(arg -> true,
                "Key isn't unique",
                Long::parseLong,
                arguments.getArg());
        arguments.setLongArg(key);
        return arguments;
    }
}
