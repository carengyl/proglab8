package commands.clientCommands;

import UDPutil.Response;
import commands.AbstractCommand;
import commands.ArgumentValidationFunctions;
import commands.CommandArgument;
import commands.CommandData;
import commonUtil.HumanBeingFactory;
import commonUtil.Validators;
import entities.CollectionManager;
import exceptions.InvalidNumberOfArgsException;
import exceptions.NoUserInputException;
import exceptions.ValidationException;

import java.io.Serializable;
import java.util.Optional;

public class RemoveGreaterCommand extends AbstractCommand implements Serializable {
    private final CollectionManager collection;

    public RemoveGreaterCommand(CollectionManager collection) {
        super("remove_greater", "Remove all elements, which are greater than {element}", ArgumentValidationFunctions.VALIDATE_NUMBER_OF_ARGS.getValidationFunction());
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
        else if (argument.getElementArgument() != null) {
          try {
              HumanBeingFactory humanBeingFactory = new HumanBeingFactory();
              humanBeingFactory.setVariables(argument.getElementArgument());
              StringBuilder stringBuilder = new StringBuilder();
              for (long key : collection.getHumanBeings().keySet()) {
                  if (collection.getHumanBeings().get(key).compareTo(humanBeingFactory.getCreatedHumanBeing()) > 0) {
                      stringBuilder.append(collection.removeByKey(key)).append("\n");
                  }
              }
              return Optional.of(new Response(stringBuilder.toString()));
          } catch (ValidationException e) {
              return Optional.of(new Response(e.getMessage()));
          }
        } else {
            return Optional.of(new Response(this.getCommandData(), argument));
        }
    }
    @Override
    public CommandArgument validateArguments(CommandArgument arguments, CommandData commandData) throws ValidationException, InvalidNumberOfArgsException {
        Validators.validateNumberOfArgs(arguments.getNumberOfArgs(), commandData.numberOfArgs());
        return arguments;
    }
}
