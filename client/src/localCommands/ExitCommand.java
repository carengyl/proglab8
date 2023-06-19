package localCommands;

import UDPutil.Request;
import UDPutil.Response;
import clientUtil.Session;
import commands.AbstractCommand;
import commands.CommandArgument;
import commands.CommandData;
import commonUtil.OutputUtil;
import commonUtil.Validators;
import exceptions.InvalidNumberOfArgsException;
import exceptions.NoUserInputException;

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
