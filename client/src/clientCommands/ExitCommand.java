package clientCommands;

import UDPutil.Response;
import clientUtil.ClientHandler;
import commands.AbstractCommand;
import commands.CommandArgument;
import commonUtil.OutputUtil;
import commonUtil.Validators;
import exceptions.InvalidNumberOfArgsException;
import exceptions.NoUserInputException;

import java.util.Optional;
import java.util.Scanner;

public class ExitCommand extends AbstractCommand {
    private final ClientHandler clientHandler;
    public ExitCommand(ClientHandler handler) {
        super("exit", "Shut down client");
        this.clientHandler = handler;
    }

    @Override
    public Optional<Response> executeCommand(CommandArgument argument) throws NoUserInputException {
        boolean userInput = Validators.validateBooleanInput("Shut down (everything not saved will be lost)",
                new Scanner(System.in));
        if (userInput) {
            OutputUtil.printSuccessfulMessage("Shutting down...");
            clientHandler.toggleStatus();
        }
        return Optional.empty();
    }

    @Override
    public CommandArgument validateArguments(CommandArgument arguments) throws InvalidNumberOfArgsException {
        Validators.validateNumberOfArgs(arguments.getNumberOfArgs(), this.getNumberOfArgs());
        return arguments;
    }
}
