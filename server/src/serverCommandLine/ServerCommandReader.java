package serverCommandLine;

import commands.CommandArgument;
import commonUtil.OutputUtil;
import exceptions.NoUserInputException;

import java.util.Arrays;
import java.util.Scanner;

public class ServerCommandReader {
    private final Invoker invoker;
    private final Scanner scanner;
    public ServerCommandReader(Invoker invoker, Scanner scanner) {
        this.invoker = invoker;
        this.scanner = scanner;
    }

    public void readCommandFromConsole() throws NoUserInputException {
        OutputUtil.printSuccessfulMessageOneStrip("> ");
        String[] splitString = scanner.nextLine().replaceAll("\s{2,}", " ").strip().split(" ");
        String commandName = splitString[0];
        CommandArgument argument = new CommandArgument(Arrays.copyOfRange(splitString, 1, splitString.length));
        invoker.executeServerCommand(commandName, argument);
    }
}
