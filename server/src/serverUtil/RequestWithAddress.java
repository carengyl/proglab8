package serverUtil;

import UDPutil.Request;

import java.net.SocketAddress;

public record RequestWithAddress(Request request, SocketAddress socketAddress) {
}
