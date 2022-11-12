package com.tsemkalo.homework4;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.flywaydb.core.Flyway;
import org.jetbrains.annotations.NotNull;

public final class DBInitializer {
    public final String CONNECTION;
    public final String DB_NAME;
    public final String USERNAME;
    public final String PASSWORD;

    @Inject
    public DBInitializer(@NotNull @Named("CONNECTION") String CONNECTION, @NotNull @Named("DB_NAME") String DB_NAME, @NotNull @Named("USERNAME") String USERNAME, @NotNull @Named("PASSWORD") String PASSWORD) {
        this.CONNECTION = CONNECTION;
        this.DB_NAME = DB_NAME;
        this.USERNAME = USERNAME;
        this.PASSWORD = PASSWORD;
    }

    public void runMigration() {
        final Flyway flyway = Flyway
                .configure()
                .dataSource(CONNECTION + DB_NAME, USERNAME, PASSWORD)
                .locations("db")
                .load();
        flyway.migrate();
        System.out.println("Migrations applied successfully");
    }
}
