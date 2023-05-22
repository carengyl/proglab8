package serverCommandLine;

import commands.CommandArgument;
import commonUtil.OutputUtil;
import exceptions.NoUserInputException;

import java.util.Arrays;
import java.util.Scanner;

public class ServerCommandReader {
    private Invoker invoker = new Invoker();
    public ServerCommandReader() {}

    private void initCommands() {
        //invoker.addCommands - - -
    }

    public void readCommandFromConsole(Scanner scanner) throws NoUserInputException {
        OutputUtil.printSuccessfulMessage(">");
        String[] splitString = scanner.nextLine().replaceAll("\s{2,}", " ").strip().split(" ");
        String commandName = splitString[0];
        CommandArgument argument = new CommandArgument(Arrays.copyOfRange(splitString, 1, splitString.length));
        invoker.performCommand(commandName, argument);
    }
}
