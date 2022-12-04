package com.tsemkalo.homework8.db_init;

import org.flywaydb.core.Flyway;
import org.jetbrains.annotations.NotNull;

public final class DBInitializer {

    public static void initDb(@NotNull JDBCCredentials CREDENTIALS) {
        Flyway flyway = Flyway.configure()
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