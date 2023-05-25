package commands.clientCommands;

import UDPutil.Response;
import commands.AbstractCommand;
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
        super("execute_script", "Execute script from @file", 1, "@file - path to file with script");
    }


    @Override
    public Optional<Response> executeCommand(CommandArgument argument) throws NoUserInputException {
        return Optional.of(new Response(argument));
    }

    @Override
    public CommandArgument validateArguments(CommandArgument argument, CommandData commandData) throws ValidationException, InvalidNumberOfArgsException {
        Validators.validateNumberOfArgs(argument.getNumberOfArgs(), commandData.numberOfArgs());
        argument.setFileName(argument.getArg());
        return argument;
    }
}
