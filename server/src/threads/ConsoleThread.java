package threads;

import commonUtil.OutputUtil;
import exceptions.NoUserInputException;
import serverCommandLine.ServerCommandReader;
import serverUtil.ServerHandler;

public class ConsoleThread extends Thread {
    private final ServerCommandReader serverCommandReader;
    public ConsoleThread(ServerCommandReader serverCommandReader) {
        this.serverCommandReader = serverCommandReader;
    }

    @Override
    public void run() {
        while (ServerHandler.isRunning()) {
            try {
                serverCommandReader.readCommandFromConsole();
            } catch (NoUserInputException e) {
                OutputUtil.printErrorMessage(e.getMessage());
                ServerHandler.toggleRunning();
            }
        }
    }
}
