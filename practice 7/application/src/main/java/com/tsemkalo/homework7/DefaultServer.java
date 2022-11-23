package com.tsemkalo.homework7;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;

public final class DefaultServer {
    private final Server server = new Server();
    private static final int port = 3466;

    public Server build() {
        return build(port);
    }

    public Server build(int port) {
        final HttpConnectionFactory httpConnectionFactory = new HttpConnectionFactory();
        final ServerConnector serverConnector = new ServerConnector(server, httpConnectionFactory);
        serverConnector.setHost("localhost");
        serverConnector.setPort(port);
        server.setConnectors(new Connector[]{serverConnector});
        return server;
    }
}
