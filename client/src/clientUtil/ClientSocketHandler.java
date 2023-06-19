package clientUtil;

import UDPutil.Deserializer;
import UDPutil.Request;
import UDPutil.Response;
import UDPutil.Serializer;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;

public class ClientSocketHandler {
    private final int DEFAULT_PORT = 1337;
    private final int RESPONSE_TIME = 15000;
    private final DatagramSocket datagramSocket;
    private int port;
    private String address = "localhost";
    private InetAddress serverAddress;
    public ClientSocketHandler() throws SocketException, UnknownHostException {
        port = DEFAULT_PORT;
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

    public void sendRequest(Request request) throws IOException {
        ByteBuffer byteBuffer = Serializer.serializeRequest(request);
        byte[] sendingBuffer = byteBuffer.array();
        DatagramPacket datagramPacket = new DatagramPacket(sendingBuffer, sendingBuffer.length, serverAddress, port);
        datagramSocket.send(datagramPacket);
    }

    public Response receiveResponse() throws IOException, ClassNotFoundException {
        datagramSocket.setSoTimeout(RESPONSE_TIME);
        int receivedSize = datagramSocket.getReceiveBufferSize();
        byte[] bytes = new byte[receivedSize];
        DatagramPacket receivedPacket = new DatagramPacket(bytes, bytes.length);
        datagramSocket.receive(receivedPacket);
        return Deserializer.deserializeResponse(receivedPacket.getData());
    }

    public int getPort() {
        return port;
    }

    public String getAddress() {
        return address;
    }
}
