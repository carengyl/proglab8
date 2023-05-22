package threads;

import UDPutil.Request;
import UDPutil.Response;
import commonUtil.OutputUtil;
import serverCommandLine.Invoker;
import serverUtil.ServerHandler;
import serverUtil.ServerSocketHandler;

import java.io.IOException;
import java.util.Optional;

public class RequestThread extends Thread {
    private final Invoker invoker;
    private final ServerSocketHandler socketHandler;

    public RequestThread(Invoker invoker, ServerSocketHandler socketHandler) {
        this.invoker = invoker;
        this.socketHandler = socketHandler;
    }
    @Override
    public void run() {
        while (ServerHandler.isRunning()) {
            try {
                Optional<Request> acceptedRequest = socketHandler.getRequest();
                if (acceptedRequest.isPresent()) {
                    Request request = acceptedRequest.get();
                    if (request.isRequestCommand()) {
                        socketHandler.sendResponse(new Response(invoker.getClientAvailableCommand()));
                    } else {
                        Response responseToSend = invoker.executeClientCommand(request);
                        socketHandler.sendResponse(responseToSend);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                OutputUtil.printErrorMessage("An error occurred while deserializing the request, try again");
            } catch (ClassNotFoundException e) {
                OutputUtil.printErrorMessage(e.getMessage());
            }
        }
        try {
            socketHandler.stopServer();
        } catch (IOException e) {
            OutputUtil.printErrorMessage("An error occurred during stopping the server");
        }
    }
}
