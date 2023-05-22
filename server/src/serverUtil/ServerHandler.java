package serverUtil;

import java.nio.file.Path;
import java.util.Scanner;

public class ServerHandler {
    private final Scanner scanner = new Scanner(System.in);
    private final int maxPort = 65535;
    //private ServerSocketWorker serverSocketWorker;
    //private final ServerCommandReader
    private CollectionOfHumanBeings collection;
    private final XMLParser parser = new XMLParser();

    public ServerHandler(CollectionOfHumanBeings collection) {
        this.collection = collection;
    }

    public void startServerHandler() {
        //do-smth
    }
}
