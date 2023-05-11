package commandLine;

import commonUtil.OutputUtil;

import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Class, responsible for reading commands from console and executing them
 */
public class CommandReader {
    public void readCommandsFromConsole(Scanner scanner) {
        OutputUtil.printSuccessfulMessage(">");
        String line = scanner.nextLine().replaceAll("\s{2,}", " ").strip();
        //invoker.performCommand(line, false, null);
        System.out.println(line);
    }
}
