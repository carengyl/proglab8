package localCommands;

import UDPutil.Request;
import UDPutil.Response;
import commands.AbstractCommand;
import commands.CommandArgument;
import commands.CommandData;
import commonUtil.OutputUtil;
import commonUtil.Validators;
import exceptions.InvalidNumberOfArgsException;

import java.util.HashMap;
import java.util.Optional;

public class HelpCommand extends AbstractCommand {

    private final HashMap<String, AbstractCommand> clientCommands;
    private final HashMap<String, CommandData> serverCommands;

    public HelpCommand(HashMap<String, AbstractCommand> clientCommands, HashMap<String, CommandData> serverCommands) {
        super("help", "Show all available commands", (commandArgument, commandData) -> {
            Validators.validateNumberOfArgs(commandArgument.getNumberOfArgs(), commandData.numberOfArgs());
            return commandArgument;
        });
        this.clientCommands = clientCommands;
        this.serverCommands = serverCommands;
    }

    @Override
    public Optional<Response> executeCommand(Request request) {
        OutputUtil.printSuccessfulMessage("Server commands:");
        for (String command: serverCommands.keySet()) {
            OutputUtil.printSuccessfulMessage(serverCommands.get(command).toString());
        }
        OutputUtil.printSuccessfulMessage("");
        OutputUtil.printSuccessfulMessage("Local commands:");
        for (String command: clientCommands.keySet()) {
            OutputUtil.printSuccessfulMessage(clientCommands.get(command).toString());
        }
        return Optional.empty();
    }

    @Override
    public CommandArgument validateArguments(CommandArgument arguments, CommandData commandData) throws InvalidNumberOfArgsException {
        Validators.validateNumberOfArgs(arguments.getNumberOfArgs(), commandData.numberOfArgs());
        return arguments;
    }
}
