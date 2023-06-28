package server.threads;

import common.util.udp.RequestType;
import common.util.udp.Response;
import common.util.OutputUtil;
import server.database.util.DBSSHConnector;
import server.commandline.CommandManager;
import server.util.RequestWithAddress;
import server.util.ServerHandler;
import server.util.ServerSocketHandler;
import server.util.UsersManager;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.*;

public class RequestThread implements Runnable {
    private final CommandManager commandManager;
    private final UsersManager usersManager;
    private final ServerSocketHandler socketHandler;
    private final ExecutorService fixedService = Executors.newFixedThreadPool(5);
    private final ExecutorService cachedService = Executors.newCachedThreadPool();
    private final ForkJoinPool forkJoinPool = new ForkJoinPool(4);


    public RequestThread(CommandManager commandManager, ServerSocketHandler socketHandler, UsersManager usersManager) {
        this.commandManager = commandManager;
        this.socketHandler = socketHandler;
        this.usersManager = usersManager;
    }
    @Override
    public void run() {
        while (ServerHandler.isRunning()) {
            try {
                Future<Optional<RequestWithAddress>> listenFuture = fixedService.submit(socketHandler::getRequest);
                Optional<RequestWithAddress> optionalRequestWithAddress = listenFuture.get();
                if (optionalRequestWithAddress.isPresent()) {
                    RequestWithAddress acceptedRequest = optionalRequestWithAddress.get();
                    CompletableFuture
                            .supplyAsync(acceptedRequest::request)
                            .thenApplyAsync(request -> {
                               if (request.getRequestType().equals(RequestType.COMMAND)) {
                                   return commandManager.executeClientCommand(request);
                               } else if (request.getRequestType().equals(RequestType.INIT_COMMANDS)) {
                                   return new Response(commandManager.getClientSendingCommand());
                               } else if (request.getRequestType().equals(RequestType.REGISTER)) {
                                   return usersManager.registerNewUser(request);
                               } else {
                                   return usersManager.loginUser(request);
                               }
                            }, cachedService).
                            thenAcceptAsync(response -> {
                                try {
                                    socketHandler.sendResponse(response, acceptedRequest.socketAddress());
                                } catch (IOException e) {
                                    OutputUtil.printErrorMessage(e.getMessage());
                                }
                            }, forkJoinPool);
                }
            } catch (ExecutionException e) {
                OutputUtil.printErrorMessage(e.getMessage());
            } catch (InterruptedException e) {
                OutputUtil.printErrorMessage("An error occurred while deserializing the request, try again");
            }
        }
        try {
            socketHandler.stopServer();
            DBSSHConnector.closeSSH();
            fixedService.shutdown();
            cachedService.shutdown();
            forkJoinPool.shutdown();
        } catch (IOException e) {
            OutputUtil.printErrorMessage("An error occurred during stopping the server");
        }
    }
}
