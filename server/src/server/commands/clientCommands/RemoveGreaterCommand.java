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

public class RemoveGreaterCommand extends AbstractCommand implements Serializable {
    private final CommandProcessor commandProcessor;

    public RemoveGreaterCommand(CommandProcessor commandProcessor) {
        super("remove_greater", "Remove all elements, which are greater than {element}", ArgumentValidationFunctions.VALIDATE_NUMBER_OF_ARGS.getValidationFunction());
        this.commandProcessor = commandProcessor;
    }

    @Override
    public Optional<Response> executeCommand(Request request) throws NoUserInputException {
        if (request.getCommandArgument().getHumanBeingArgument() != null) {
            return Optional.of(commandProcessor.removeGreater(request));
        }
        else if (request.getCommandArgument().getElementArgument() != null) {
          try {
              HumanBeingFactory humanBeingFactory = new HumanBeingFactory();
              humanBeingFactory.setVariables(request.getCommandArgument().getElementArgument());
              request.getCommandArgument().setHumanBeingArgument(humanBeingFactory.getCreatedHumanBeing());
              return Optional.of(commandProcessor.removeGreater(request));
          } catch (ValidationException e) {
              return Optional.of(new Response(e.getMessage()));
          }
        } else {
            return Optional.of(new Response(this.getCommandData(), request.getCommandArgument()));
        }
    }
    @Override
    public CommandArgument validateArguments(CommandArgument arguments, CommandData commandData) throws ValidationException, InvalidNumberOfArgsException {
        Validators.validateNumberOfArgs(arguments.getNumberOfArgs(), commandData.numberOfArgs());
        return arguments;
    }
}
