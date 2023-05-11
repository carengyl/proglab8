package clientUtil;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class ClientSocketHandler {
    private final int defaultPort = 1337;
    private final DatagramSocket datagramSocket;
    private int port;
    private String address = "localhost";
    private InetAddress serverAddress;
    public ClientSocketHandler() throws SocketException, UnknownHostException {
        port = defaultPort;
        datagramSocket = new DatagramSocket();
        serverAddress = InetAddress.getByName(address);
    }

    public void setAddress(String address) throws UnknownHostException {
        this.address = address;
        serverAddress = InetAddress.getByName(address);
    }

    public void setPort(int port) {
        this.port = port;
    }
}
