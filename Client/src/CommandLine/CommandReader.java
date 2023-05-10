package CommandLine;

import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Class, responsible for reading commands from console and executing them
 */
public class CommandReader {
    /**
     * Field responsible for command working
     */
    private static boolean working = true;

    /**
     * Toggles program working status
     */
    public static void toggleProgram() {
        working = !working;
    }

    public void readCommandsFromConsole() {
        Scanner scanner = new Scanner(System.in);
        while (working) {
            System.out.print("> ");
            try {
                String line = scanner.nextLine().replaceAll("\s{2,}", " ").strip();
                //invoker.performCommand(line, false, null);
                System.out.println(line);
            } catch (NoSuchElementException e) {
                System.err.println(e.getMessage());
                working = false;
            }
        }
    }
}
