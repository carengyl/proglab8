package commands.clientCommands;

import UDPutil.Response;
import commands.AbstractCommand;
import commands.CommandArgument;
import commands.CommandData;
import commonUtil.Validators;
import entities.Mood;
import exceptions.InvalidNumberOfArgsException;
import exceptions.ValidationException;
import entities.CollectionManager;

import java.io.Serializable;
import java.util.Optional;

public class CountLessThanMinutesOfWaitingCommand extends AbstractCommand implements Serializable {
    private final CollectionManager collection;
    public CountLessThanMinutesOfWaitingCommand(CollectionManager collection) {
        super("count_less_than_minutes_of_waiting",
                "Counts humanBeings, which minutes of waiting is less than @minutes_of_waiting",
                1,
                "@minutes_of_waiting (double) minutes of waiting");
        this.collection = collection;
    }

    @Override
    public Optional<Response> executeCommand(CommandArgument argument) {
        double minutesOfWaiting = argument.getDoubleArg();

        int counter = 0;
        for (long key: collection.getHumanBeings().keySet()) {
            if (collection.getHumanBeings().get(key).getMinutesOfWaiting() != null) {
                if (collection.getHumanBeings().get(key).getMinutesOfWaiting() > minutesOfWaiting) {
                    counter++;
                }
            }
        }

        return Optional.of(new Response("People with minutes of waiting greater than " + minutesOfWaiting + ": " + counter));
    }

    @Override
    public CommandArgument validateArguments(CommandArgument arguments, CommandData commandData) throws ValidationException, InvalidNumberOfArgsException {
        Validators.validateNumberOfArgs(arguments.getNumberOfArgs(), commandData.numberOfArgs());
        double minutesOfWaiting = Validators.validateArg(arg -> true,
                "Pick a Mood number:\n" + Mood.show(),
                Double::parseDouble,
                arguments.getArg());

        arguments.setDoubleArg(minutesOfWaiting);
        return arguments;
    }
}
