package localCommands;

import UDPutil.Response;
import clientUtil.ScriptReader;
import commands.AbstractCommand;
import commands.CommandArgument;
import commands.CommandData;
import commonUtil.OutputUtil;
import commonUtil.Validators;
import entities.HumanBeing;
import exceptions.CommandExecutionException;
import exceptions.InvalidNumberOfArgsException;
import exceptions.NoUserInputException;
import exceptions.ValidationException;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class ExecuteScriptCommand extends AbstractCommand {
    private final ScriptReader scriptReader;
    private final Set<Path> fileHistory = new HashSet<>();
    public ExecuteScriptCommand(ScriptReader scriptReader) {
        super("execute_script", "Execute script from @file", 1, "@file - path to file with script");
        this.scriptReader = scriptReader;
    }

    @Override
    public Optional<Response> executeCommand(CommandArgument argument) throws NoUserInputException {
        try {
            Path fileName = argument.getFileNameArg();
            if (fileHistory.contains(fileName)) {
                OutputUtil.printErrorMessage("Loop possible");
            } else {
                fileHistory.add(fileName);
                scriptReader.readCommandsFromFile(fileName);
                ArrayList<String> commands = scriptReader.getCommandsFromScript();
                if (commands.contains("execute_script " + fileName)) {
                    throw new CommandExecutionException("Script calls itself from inside. Loop possible");
                }
                for (int i = 0; i < commands.size(); i++) {
                    String command = commands.get(i);
                    OutputUtil.printSuccessfulMessage(command);
                    if (i <= commands.size() - HumanBeing.getNumberOfFields() - 2) {
                        //CommandReader.getInvoker().performCommand(command, true, new ArrayList<>(commands.subList(i + 1, i + 2 + HumanBeing.getNumberOfFields())));
                    } else {
                        //CommandReader.getInvoker().performCommand(command, true, new ArrayList<>(commands.subList(i + 1, commands.size())));
                    }
                }
                fileHistory.remove(fileName);
            }
        } catch (CommandExecutionException | IOException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public CommandArgument validateArguments(CommandArgument arguments, CommandData commandData) throws ValidationException, InvalidNumberOfArgsException {
        Validators.validateNumberOfArgs(arguments.getNumberOfArgs(), commandData.numberOfArgs());
        Path fileName = Validators.validateArg(arg -> true,
                "Enter a path to file",
                Path::of,
                arguments.getArg());
        arguments.setFileNameArg(fileName);
        return arguments;
    }
}
