package serverUtil;

import UDPutil.Deserializer;
import UDPutil.Request;
import UDPutil.Response;
import UDPutil.Serializer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

public class ServerSocketHandler {
    private final int DEFAULT_PORT = 228;
    private final int SELECTOR_DELAY = 100;
    private Selector selector;
    private DatagramChannel datagramChannel;
    private SocketAddress socketAddress;
    private int selfPort = DEFAULT_PORT;

    public ServerSocketHandler(int port) throws IOException {
        init(port);
    }

    public ServerSocketHandler() throws IOException {
        init(DEFAULT_PORT);
    }

    private void init(int port) throws IOException {
        datagramChannel = DatagramChannel.open();
        selector = Selector.open();
        datagramChannel.socket().bind(new InetSocketAddress(port));
        datagramChannel.configureBlocking(false);
        datagramChannel.register(selector, SelectionKey.OP_READ);
    }

    public void stopServer() throws IOException {
        selector.close();
        datagramChannel.close();
    }

    public Optional<Request> getRequest() throws IOException, ClassNotFoundException {
        if (selector.select(SELECTOR_DELAY) == 0) {
            return Optional.empty();
        }
        Set<SelectionKey> readyKeys = selector.selectedKeys();
        Iterator<SelectionKey> iterator = readyKeys.iterator();
        while (iterator.hasNext()) {
            SelectionKey key = iterator.next();
            iterator.remove();
            if (key.isReadable()) {
                int arraySize = datagramChannel.socket().getReceiveBufferSize();
                ByteBuffer packet = ByteBuffer.allocate(arraySize);
                socketAddress = datagramChannel.receive(packet);
                ((Buffer) packet).flip();
                byte[] bytes = new byte[packet.remaining()];
                packet.get(bytes);
                return Optional.of(Deserializer.deserializeRequest(bytes));
            }
        }
        return Optional.empty();
    }

    public void sendResponse(Response response) throws IOException {
        ByteBuffer byteBuffer = Serializer.serializeResponse(response);
        datagramChannel.send(byteBuffer, socketAddress);
    }

    public void setSelfPort(int selfPort) {
        this.selfPort = selfPort;
    }
}
