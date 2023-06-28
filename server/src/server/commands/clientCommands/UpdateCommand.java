package server.commands.clientCommands;

import common.util.udp.Request;
import common.util.udp.Response;
import common.commands.AbstractCommand;
import common.commands.ArgumentValidationFunctions;
import common.commands.CommandArgument;
import common.commands.CommandData;
import common.util.HumanBeingFactory;
import common.util.Validators;
import common.exceptions.InvalidNumberOfArgsException;
import common.exceptions.NoUserInputException;
import common.exceptions.ValidationException;
import server.util.CommandProcessor;

import java.io.Serializable;
import java.util.Optional;

public class UpdateCommand extends AbstractCommand implements Serializable {
    private final CommandProcessor commandProcessor;

    public UpdateCommand(CommandProcessor commandProcessor) {
        super("update",
                "Update element by @id",
                1,
                "@id - (long) id of collection element",
                ArgumentValidationFunctions.VALIDATE_ID.getValidationFunction());
        this.commandProcessor = commandProcessor;
    }

    @Override
    public Optional<Response> executeCommand(Request request) throws NoUserInputException {
        long id = request.getCommandArgument().getLongArg();
        if (request.getCommandArgument().getHumanBeingArgument() != null) {
            return Optional.of(commandProcessor.updateById(request));
        } else if (request.getCommandArgument().getElementArgument() != null) {
            try {
                HumanBeingFactory humanBeingFactory = new HumanBeingFactory(id);
                humanBeingFactory.setVariables(request.getCommandArgument().getElementArgument());
                request.getCommandArgument().setHumanBeingArgument(humanBeingFactory.getCreatedHumanBeing());
                return Optional.of(commandProcessor.updateById(request));
            }
            catch (ValidationException e) {
                return Optional.of(new Response(e.getMessage()));
            }
        } else {
            return Optional.of(new Response(this.getCommandData(), request.getCommandArgument()));
        }
    }

    @Override
    public CommandArgument validateArguments(CommandArgument arguments, CommandData commandData) throws ValidationException, InvalidNumberOfArgsException {
        Validators.validateNumberOfArgs(arguments.getNumberOfArgs(), commandData.numberOfArgs());
        long id = Validators.validateArg(arg -> true,
                "There is no Human Being with id: " + arguments.getArg(),
                Long::parseLong,
                arguments.getArg());
        arguments.setLongArg(id);
        return arguments;
    }
}
