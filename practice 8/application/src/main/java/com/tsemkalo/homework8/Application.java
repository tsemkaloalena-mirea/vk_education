package com.tsemkalo.homework8;

import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.JDBCLoginService;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher;

@SuppressWarnings("NotNullNullableValidation")
public final class Application {
    public static void main(String[] args) throws Exception {
        Server server = JettyServer.build();

        ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        contextHandler.addServlet(HttpServletDispatcher.class, "/");
        contextHandler.addEventListener(new GuiceListener());

        String jdbcConfiguration = Application.class.getResource("/jdbc_config").toExternalForm();
        JDBCLoginService jdbcLoginService = new JDBCLoginService("login", jdbcConfiguration);
        ConstraintSecurityHandler securityHandler = new SecurityHandlerBuilder().build(jdbcLoginService);
        server.addBean(jdbcLoginService);
        contextHandler.setHandler(securityHandler);

        server.setHandler(contextHandler);
        server.start();
    }
}