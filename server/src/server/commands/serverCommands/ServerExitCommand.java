package server.commands.serverCommands;

import common.util.udp.Request;
import common.util.udp.Response;
import common.commands.AbstractCommand;
import common.commands.CommandArgument;
import common.commands.CommandData;
import common.util.OutputUtil;
import common.util.Validators;
import common.exceptions.InvalidNumberOfArgsException;
import common.exceptions.NoUserInputException;
import common.exceptions.ValidationException;
import server.util.ServerHandler;

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
