package server.commands.clientCommands;

import common.util.udp.Request;
import common.util.udp.Response;
import common.commands.AbstractCommand;
import common.commands.ArgumentValidationFunctions;
import common.commands.CommandArgument;
import common.commands.CommandData;
import common.util.Validators;
import common.exceptions.InvalidNumberOfArgsException;
import common.exceptions.NoUserInputException;
import common.exceptions.ValidationException;

import java.io.Serializable;
import java.util.Optional;

public class ExecuteScriptCommand extends AbstractCommand implements Serializable {
    public ExecuteScriptCommand() {
        super("execute_script",
                "Execute script from @file",
                1,
                "@file - path to file with script",
                ArgumentValidationFunctions.VALIDATE_FILE_NAME.getValidationFunction());
    }

    @Override
    public Optional<Response> executeCommand(Request request) throws NoUserInputException {
        return Optional.of(new Response(request.getCommandArgument()));
    }

    @Override
    public CommandArgument validateArguments(CommandArgument argument, CommandData commandData) throws ValidationException, InvalidNumberOfArgsException {
        Validators.validateNumberOfArgs(argument.getNumberOfArgs(), commandData.numberOfArgs());
        argument.setFileName(argument.getArg());
        return argument;
    }
}
