package serverCommandLine;

import commands.CommandArgument;
import commonUtil.OutputUtil;
import exceptions.NoUserInputException;

import java.util.Arrays;
import java.util.Scanner;

public class ServerCommandReader {
    private final CommandManager commandManager;
    private final Scanner scanner;
    public ServerCommandReader(CommandManager commandManager, Scanner scanner) {
        this.commandManager = commandManager;
        this.scanner = scanner;
    }

    public void readCommandFromConsole() throws NoUserInputException {
        OutputUtil.printSuccessfulMessageOneStrip("> ");
        String[] splitString = scanner.nextLine().replaceAll("\s{2,}", " ").strip().split(" ");
        String commandName = splitString[0];
        CommandArgument argument = new CommandArgument(Arrays.copyOfRange(splitString, 1, splitString.length));
        commandManager.executeServerCommand(commandName, argument);
    }
}
