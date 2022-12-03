package com.tsemkalo.homework7;

import com.google.inject.AbstractModule;
import com.tsemkalo.homework7.db_init.JDBCCredentials;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class GuiceModule extends AbstractModule {
    private static final JDBCCredentials CREDENTIALS = JDBCCredentials.DEFAULT;

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

        bind(ProductDAO.class);
        bind(ManufacturerDAO.class);
        bind(ProductService.class);
        bind(ProductServlet.class);
    }
}
