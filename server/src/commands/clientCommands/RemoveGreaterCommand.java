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
