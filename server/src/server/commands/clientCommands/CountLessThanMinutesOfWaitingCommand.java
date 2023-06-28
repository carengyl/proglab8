package server.commands.clientCommands;

import common.util.udp.Request;
import common.util.udp.Response;
import common.commands.AbstractCommand;
import common.commands.ArgumentValidationFunctions;
import common.commands.CommandArgument;
import common.commands.CommandData;
import common.util.Validators;
import common.entities.CollectionManager;
import common.entities.Mood;
import common.exceptions.InvalidNumberOfArgsException;
import common.exceptions.ValidationException;
import server.util.CommandProcessor;

import java.io.Serializable;
import java.util.Optional;

public class CountLessThanMinutesOfWaitingCommand extends AbstractCommand implements Serializable {
    private final CollectionManager collection;
    public CountLessThanMinutesOfWaitingCommand(CommandProcessor commandProcessor) {
        super("count_less_than_minutes_of_waiting",
                "Counts humanBeings, which minutes of waiting is less than @minutes_of_waiting",
                1,
                "@minutes_of_waiting (double) minutes of waiting",
                ArgumentValidationFunctions.VALIDATE_MINUTES_OF_WAITING.getValidationFunction());
        this.collection = commandProcessor.getCollectionManager();
    }

    @Override
    public Optional<Response> executeCommand(Request request) {
        double minutesOfWaiting = request.getCommandArgument().getDoubleArg();

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
