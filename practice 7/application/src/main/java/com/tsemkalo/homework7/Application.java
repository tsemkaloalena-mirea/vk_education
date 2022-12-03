package com.tsemkalo.homework7;

import com.google.inject.Guice;
import com.google.inject.Injector;
import jakarta.servlet.DispatcherType;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.JDBCLoginService;
import org.eclipse.jetty.security.LoginService;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;

import java.net.URL;
import java.util.EnumSet;

@SuppressWarnings({"Duplicates", "NotNullNullableValidation"})
public final class Application {
    public static void main(String[] args) throws Exception {
        Injector injector = Guice.createInjector(new GuiceModule());

        Server server = new DefaultServer().build();
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        context.setContextPath("/");
        URL resource = LoginService.class.getResource("/static");
        context.setBaseResource(Resource.newResource(resource.toExternalForm()));

        context.setWelcomeFiles(new String[]{"/static/description"});
        context.addServlet(new ServletHolder("products", injector.getInstance(ProductServlet.class)), "/products");
        context.addServlet(new ServletHolder("default", DefaultServlet.class), "/*");

        MyFilter filter = new MyFilter();
        FilterHolder filterHolder = new FilterHolder(filter);
        context.addFilter(filterHolder, "/products", EnumSet.of(DispatcherType.REQUEST));

        String jdbcConfiguration = Application.class.getResource("/jdbc_config").toExternalForm();
        JDBCLoginService jdbcLoginService = new JDBCLoginService("login", jdbcConfiguration);
        ConstraintSecurityHandler securityHandler = new SecurityHandlerBuilder().build(jdbcLoginService);
        server.addBean(jdbcLoginService);
        securityHandler.setHandler(context);

        server.setHandler(securityHandler);

        server.start();
    }
}
