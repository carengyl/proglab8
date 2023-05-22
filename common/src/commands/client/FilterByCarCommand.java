package commands.client;

import UDPutil.Response;
import commands.AbstractCommand;
import commands.CommandArgument;
import commonUtil.Validators;
import exceptions.InvalidNumberOfArgsException;
import exceptions.NoUserInputException;
import exceptions.ValidationException;
import entities.CollectionOfHumanBeings;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

public class FilterByCarCommand extends AbstractCommand implements Serializable {
    private final CollectionOfHumanBeings collection;

    public FilterByCarCommand(CollectionOfHumanBeings collection) {
        super("filter_by_car", "Show collection elements, which car equals @car", 1, "@car - \"true\" string equals true, others to false");
        this.collection = collection;
    }


    @Override
    public Optional<Response> executeCommand(CommandArgument argument) throws NoUserInputException {
        boolean car = argument.isBooleanArg();

        Optional<Response> optionalResponse;

        if (!collection.getHumanBeings().isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            for (long key : collection.getHumanBeings().keySet()) {
                if (collection.getHumanBeings().get(key).getCar() != null) {
                    if (Objects.equals(collection.getHumanBeings().get(key).getCar().isCool(), car)) {
                        stringBuilder.append(collection.getHumanBeings().get(key));
                    }
                }
            }
            optionalResponse = Optional.of(new Response(stringBuilder.toString()));
        } else {
            optionalResponse = Optional.of(new Response("Collection is empty"));
        }
        return optionalResponse;
    }

    @Override
    public CommandArgument validateArguments(CommandArgument arguments) throws ValidationException, InvalidNumberOfArgsException {
        Validators.validateNumberOfArgs(arguments.getNumberOfArgs(), this.getNumberOfArgs());
        Boolean car = Validators.validateArg(arg -> true,
                "should be true or false",
                Boolean::parseBoolean,
                arguments.getArg());

        arguments.setBooleanArg(car);
        return arguments;
    }
}
