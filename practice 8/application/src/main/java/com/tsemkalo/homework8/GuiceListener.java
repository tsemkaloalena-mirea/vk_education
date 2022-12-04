package com.tsemkalo.homework8;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.tsemkalo.homework8.db_init.JDBCCredentials;
import org.jboss.resteasy.plugins.guice.GuiceResteasyBootstrapServletContextListener;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import javax.servlet.ServletContext;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("NotNullNullableValidation")
public final class GuiceListener extends GuiceResteasyBootstrapServletContextListener {
    @NotNull
    private static final JDBCCredentials CREDENTIALS = JDBCCredentials.DEFAULT;

    @Override
    protected List<? extends Module> getModules(ServletContext context) {
        return Collections.singletonList(new GuiceModule());
    }

    @SuppressWarnings("rawtypes")
    private static final class GuiceModule extends AbstractModule {
        @SuppressWarnings("PointlessBinding")
        @Override
        protected void configure() {
            try {
                Connection connection = DriverManager.getConnection(CREDENTIALS.url(), CREDENTIALS.login(), CREDENTIALS.password());
                DSLContext dslContext = DSL.using(connection, SQLDialect.POSTGRES);
                bind(DSLContext.class).toInstance(dslContext);
            } catch (SQLException exception) {
                exception.printStackTrace();
                System.exit(-1);
                return;
            }

            bind(JacksonMessageBodyHandler.class).toInstance(new JacksonMessageBodyHandler());
            bind(MyObjectMapperProvider.class).toInstance(new MyObjectMapperProvider());
            bind(ProductDAO.class);
            bind(ManufacturerDAO.class);
            bind(ProductController.class);
            bind(ManufacturerController.class);
        }
    }
}
