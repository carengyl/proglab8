package server.threads;

import common.util.OutputUtil;
import common.exceptions.NoUserInputException;
import server.commandline.ServerCommandReader;
import server.util.ServerHandler;

public class ConsoleThread implements Runnable {
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
                ServerHandler.turnOff();
            }
        }
    }
}
