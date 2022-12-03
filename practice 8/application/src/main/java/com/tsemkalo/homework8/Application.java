package com.tsemkalo.homework8;

import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.JDBCLoginService;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher;

@SuppressWarnings("NotNullNullableValidation")
public final class Application {
    public static void main(String[] args) throws Exception {
        final Server server = JettyServer.build();

        ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        contextHandler.addServlet(HttpServletDispatcher.class, "/");
        contextHandler.addEventListener(new GuiceListener());

        final String jdbcConfiguration = Application.class.getResource("/jdbc_config").toExternalForm();
        final JDBCLoginService jdbcLoginService = new JDBCLoginService("login", jdbcConfiguration);
        final ConstraintSecurityHandler securityHandler = new SecurityHandlerBuilder().build(jdbcLoginService);
        server.addBean(jdbcLoginService);
        contextHandler.setHandler(securityHandler);

        server.setHandler(contextHandler);
        server.start();
    }
}