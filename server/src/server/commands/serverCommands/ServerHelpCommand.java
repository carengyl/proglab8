package server.commands.serverCommands;

import common.util.udp.Request;
import common.util.udp.Response;
import common.commands.AbstractCommand;
import common.commands.CommandArgument;
import common.commands.CommandData;
import common.util.OutputUtil;
import common.util.Validators;
import common.exceptions.InvalidNumberOfArgsException;
import common.exceptions.ValidationException;

import java.util.HashMap;
import java.util.Optional;

public class ServerHelpCommand extends AbstractCommand {
    private final HashMap<String, AbstractCommand> commands;
    public ServerHelpCommand(HashMap<String, AbstractCommand> commands) {
        super("help", "Show available server.commands", (commandArgument, commandData) -> {
            Validators.validateNumberOfArgs(commandArgument.getNumberOfArgs(), commandData.numberOfArgs());
            return commandArgument;
        });
        this.commands = commands;
    }

    @Override
    public Optional<Response> executeCommand(Request request) {
        OutputUtil.printSuccessfulMessage("Builtin server.commands:");
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
