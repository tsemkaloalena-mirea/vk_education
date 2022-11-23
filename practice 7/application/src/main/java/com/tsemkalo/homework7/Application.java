package com.tsemkalo.homework7;

import com.tsemkalo.homework7.db_init.JDBCCredentials;
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
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.EnumSet;

@SuppressWarnings({"Duplicates", "NotNullNullableValidation"})
public final class Application {
    @NotNull
    private static final JDBCCredentials CREDENTIALS = JDBCCredentials.DEFAULT;

    public static void main(String[] args) throws Exception {
        final ProductDAO productDAO;
        final ManufacturerDAO manufacturerDAO;
        try {
            Connection connection = DriverManager.getConnection(CREDENTIALS.url(), CREDENTIALS.login(), CREDENTIALS.password());
            final DSLContext dslContext = DSL.using(connection, SQLDialect.POSTGRES);
            productDAO = new ProductDAO(dslContext);
            manufacturerDAO = new ManufacturerDAO(dslContext);
        } catch (SQLException exception) {
            exception.printStackTrace();
            System.exit(-1);
            return;
        }
        final Server server = new DefaultServer().build();
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        context.setContextPath("/");
        final URL resource = LoginService.class.getResource("/static");
        context.setBaseResource(Resource.newResource(resource.toExternalForm()));

        context.setWelcomeFiles(new String[]{"/static/description"});
        context.addServlet(new ServletHolder("products", new ProductServlet(productDAO, manufacturerDAO)), "/products");
        context.addServlet(new ServletHolder("default", DefaultServlet.class), "/*");

        final MyFilter filter = new MyFilter();
        final FilterHolder filterHolder = new FilterHolder(filter);
        context.addFilter(filterHolder, "/products", EnumSet.of(DispatcherType.REQUEST));

        final String jdbcConfiguration = Application.class.getResource("/jdbc_config").toExternalForm();
        final JDBCLoginService jdbcLoginService = new JDBCLoginService("login", jdbcConfiguration);
        final ConstraintSecurityHandler securityHandler = new SecurityHandlerBuilder().build(jdbcLoginService);
        server.addBean(jdbcLoginService);
        securityHandler.setHandler(context);

        server.setHandler(securityHandler);

        server.start();
    }
}
