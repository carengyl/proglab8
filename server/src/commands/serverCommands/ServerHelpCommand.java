package commands.serverCommands;

import UDPutil.Response;
import commands.AbstractCommand;
import commands.CommandArgument;
import commands.CommandData;
import commonUtil.OutputUtil;
import commonUtil.Validators;
import exceptions.InvalidNumberOfArgsException;
import exceptions.ValidationException;

import java.util.HashMap;
import java.util.Optional;

public class ServerHelpCommand extends AbstractCommand {
    private final HashMap<String, AbstractCommand> commands;
    public ServerHelpCommand(HashMap<String, AbstractCommand> commands) {
        super("help", "Show available commands", (commandArgument, commandData) -> {
            Validators.validateNumberOfArgs(commandArgument.getNumberOfArgs(), commandData.numberOfArgs());
            return commandArgument;
        });
        this.commands = commands;
    }

    @Override
    public Optional<Response> executeCommand(CommandArgument argument) {
        OutputUtil.printSuccessfulMessage("Builtin commands:");
        for (AbstractCommand command: commands.values()) {
            OutputUtil.printSuccessfulMessage(command);
        }
        return Optional.empty();
    }

    @Override
    public CommandArgument validateArguments(CommandArgument argument, CommandData commandData) throws ValidationException, InvalidNumberOfArgsException {
        Validators.validateNumberOfArgs(argument.getNumberOfArgs(), commandData.numberOfArgs());
        return argument;
    }
}
