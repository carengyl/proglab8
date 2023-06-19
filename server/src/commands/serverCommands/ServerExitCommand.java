package commands.serverCommands;

import UDPutil.Request;
import UDPutil.Response;
import commands.AbstractCommand;
import commands.CommandArgument;
import commands.CommandData;
import commonUtil.OutputUtil;
import commonUtil.Validators;
import exceptions.InvalidNumberOfArgsException;
import exceptions.NoUserInputException;
import exceptions.ValidationException;
import serverUtil.ServerHandler;

import java.util.Optional;
import java.util.Scanner;

public class ServerExitCommand extends AbstractCommand {
    public ServerExitCommand() {
        super("exit", "Shut down server program", ((commandArgument, commandData) -> {
            Validators.validateNumberOfArgs(commandArgument.getNumberOfArgs(), commandData.numberOfArgs());
            return commandArgument;
        }));
    }

    @Override
    public Optional<Response> executeCommand(Request request) throws NoUserInputException {
        boolean userInput = Validators.validateBooleanInput("Shut down (everything not saved will be lost)",
                new Scanner(System.in));
        if (userInput) {
            OutputUtil.printSuccessfulMessage("Shutting down...");
            ServerHandler.turnOff();
        }
        return Optional.empty();
    }

    @Override
    public CommandArgument validateArguments(CommandArgument arguments, CommandData commandData) throws ValidationException, InvalidNumberOfArgsException {
        Validators.validateNumberOfArgs(arguments.getNumberOfArgs(), commandData.numberOfArgs());
        return arguments;
    }
}
