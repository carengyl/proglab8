package commands.clientCommands;

import UDPutil.Request;
import UDPutil.Response;
import commands.AbstractCommand;
import commands.ArgumentValidationFunctions;
import commands.CommandArgument;
import commands.CommandData;
import commonUtil.Validators;
import exceptions.InvalidNumberOfArgsException;
import exceptions.NoUserInputException;
import exceptions.ValidationException;

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
