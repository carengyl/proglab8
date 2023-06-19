package commands.clientCommands;

import UDPutil.Request;
import UDPutil.Response;
import commands.AbstractCommand;
import commands.ArgumentValidationFunctions;
import commands.CommandArgument;
import commands.CommandData;
import commonUtil.HumanBeingFactory;
import commonUtil.Validators;
import exceptions.InvalidNumberOfArgsException;
import exceptions.NoUserInputException;
import exceptions.ValidationException;
import serverUtil.CommandProcessor;

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
