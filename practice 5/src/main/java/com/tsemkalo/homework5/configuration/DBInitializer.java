package com.tsemkalo.homework5.configuration;

import org.flywaydb.core.Flyway;
import org.jetbrains.annotations.NotNull;

public final class DBInitializer {

    public static void initDb(@NotNull JDBCCredentials CREDENTIALS) {
        final Flyway flyway = Flyway.configure()
                .dataSource(
                        CREDENTIALS.url(),
                        CREDENTIALS.login(),
                        CREDENTIALS.password()
                )
                .cleanDisabled(false)
                .locations("db")
                .load();
        flyway.clean();
        flyway.migrate();
    }

    public static void main(String[] args) {
        DBInitializer.initDb(JDBCCredentials.DEFAULT);
    }
}
