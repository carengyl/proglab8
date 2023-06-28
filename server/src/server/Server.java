package server;

import server.util.ServerHandler;

public class Server {
    public static void main(String[] args) {
        ServerHandler serverHandler = new ServerHandler();
        serverHandler.startServerHandler();
    }
}