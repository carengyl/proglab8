package client.commands;

import common.util.udp.Request;
import common.util.udp.Response;
import client.util.Session;
import common.commands.AbstractCommand;
import common.commands.CommandArgument;
import common.commands.CommandData;
import common.util.OutputUtil;
import common.util.Validators;
import common.exceptions.InvalidNumberOfArgsException;
import common.exceptions.NoUserInputException;

import java.util.Optional;
import java.util.Scanner;

public class ExitCommand extends AbstractCommand {
    private final Session session;
    public ExitCommand(Session handler) {
        super("exit", "Shut down client",
                (commandArgument, commandData) -> {
                    Validators.validateNumberOfArgs(commandArgument.getNumberOfArgs(), commandData.numberOfArgs());
                    return commandArgument;
                });
        this.session = handler;
    }

    @Override
    public Optional<Response> executeCommand(Request request) throws NoUserInputException {
        boolean userInput = Validators.validateBooleanInput("Shut down (everything not saved will be lost)",
                new Scanner(System.in));
        if (userInput) {
            OutputUtil.printSuccessfulMessage("Shutting down...");
            session.turnOff();
        }
        return Optional.empty();
    }

    @Override
    public CommandArgument validateArguments(CommandArgument arguments, CommandData commandData) throws InvalidNumberOfArgsException {
        Validators.validateNumberOfArgs(arguments.getNumberOfArgs(), commandData.numberOfArgs());
        return arguments;
    }
}
