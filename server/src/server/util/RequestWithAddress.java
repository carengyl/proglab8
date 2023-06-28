package server.util;

import common.util.udp.Request;

import java.net.SocketAddress;

public record RequestWithAddress(Request request, SocketAddress socketAddress) {
}
