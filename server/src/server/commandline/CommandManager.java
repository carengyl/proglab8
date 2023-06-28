package server.commandline;

import common.util.udp.Request;
import common.util.udp.Response;
import common.commands.AbstractCommand;
import common.commands.CommandArgument;
import common.commands.CommandData;
import server.commands.clientCommands.*;
import server.commands.serverCommands.ServerExitCommand;
import server.commands.serverCommands.ServerHelpCommand;
import common.util.OutputUtil;
import common.exceptions.InvalidNumberOfArgsException;
import common.exceptions.NoUserInputException;
import common.exceptions.ValidationException;
import server.util.CommandProcessor;

import java.util.HashMap;
import java.util.Optional;

public class CommandManager {
    private final CommandProcessor commandProcessor;
    private final HashMap<String, CommandData> CLIENT_SENDING_COMMANDS = new HashMap<>();
    private final HashMap<String, AbstractCommand> SERVER_AVAILABLE_COMMAND = new HashMap<>();

    private final HashMap<String, AbstractCommand> CLIENT_AVAILABLE_COMMAND = new HashMap<>();

    public CommandManager(CommandProcessor commandProcessor) {
        this.commandProcessor = commandProcessor;
        initCommands();
    }

    private void initCommands() {
        addClientCommand(new ClearCommand(commandProcessor));
        addClientCommand(new CountGreaterThanMoodCommand(commandProcessor));
        addClientCommand(new CountLessThanMinutesOfWaitingCommand(commandProcessor));
        addClientCommand(new ExecuteScriptCommand());
        addClientCommand(new FilterByCarCommand(commandProcessor));
        addClientCommand(new InfoCommand(commandProcessor));
        addClientCommand(new InsertCommand(commandProcessor));
        addClientCommand(new RemoveGreaterCommand(commandProcessor));
        addClientCommand(new RemoveKeyCommand(commandProcessor));
        addClientCommand(new RemoveLowerKeyCommand(commandProcessor));
        addClientCommand(new ShowCommand(commandProcessor));
        addClientCommand(new UpdateCommand(commandProcessor));

        addServerCommand(new ServerExitCommand());
        addServerCommand(new ServerHelpCommand(this.getSERVER_AVAILABLE_COMMAND()));
    }

    public void addServerCommand(AbstractCommand command) {
        this.SERVER_AVAILABLE_COMMAND.put(command.getCommandData().commandName(), command);
    }

    public void addClientCommand(AbstractCommand command) {
        this.CLIENT_AVAILABLE_COMMAND.put(command.getCommandData().commandName(), command);
        this.CLIENT_SENDING_COMMANDS.put(command.getCommandData().commandName(), command.getCommandData());
    }
    public Response executeClientCommand(Request request) {
        AbstractCommand executableCommand = CLIENT_AVAILABLE_COMMAND.get(request.getCommandName());
        Optional<Response> optionalResponse = Optional.empty();
        try {
            optionalResponse = executableCommand.executeCommand(request);
        } catch (NoUserInputException e) {
            //Suppress exception, because there will be no input from user
            e.getSuppressed();
        }
        //Suppress warning, because optional will always be present
        //noinspection OptionalGetWithoutIsPresent
        return optionalResponse.get();
    }

    public void executeServerCommand(String commandName, CommandArgument argument) throws NoUserInputException {
        if (SERVER_AVAILABLE_COMMAND.containsKey(commandName)) {
            try {
                AbstractCommand executableCommand = SERVER_AVAILABLE_COMMAND.get(commandName);
                CommandArgument validatedArg = executableCommand.validateArguments(argument, executableCommand.getCommandData());
                executableCommand.executeCommand(new Request(commandName, validatedArg));
            } catch (ValidationException | InvalidNumberOfArgsException e) {
                OutputUtil.printErrorMessage(e.getMessage());
            }
        } else {
            OutputUtil.printErrorMessage("Command " + commandName + " not found. Type \"help\" to see available server.commands.");
        }
    }

    public HashMap<String, AbstractCommand> getSERVER_AVAILABLE_COMMAND() {
        return SERVER_AVAILABLE_COMMAND;
    }

    public HashMap<String, CommandData> getClientSendingCommand() {
        return CLIENT_SENDING_COMMANDS;
    }
}
