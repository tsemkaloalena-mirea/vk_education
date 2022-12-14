package com.tsemkalo.homework8;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;

@SuppressWarnings({"NotNullNullableValidation"})
public enum JettyServer {
    ;

    public static Server build() {
        Server server = new Server();
        ServerConnector serverConnector = new ServerConnector(server, new HttpConnectionFactory());
        serverConnector.setHost("localhost");
        serverConnector.setPort(3466);
        server.setConnectors(new Connector[]{serverConnector});
        return server;
    }
}
