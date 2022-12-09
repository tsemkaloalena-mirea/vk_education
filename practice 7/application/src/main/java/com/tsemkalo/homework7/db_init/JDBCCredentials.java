package com.tsemkalo.homework7.db_init;

import org.jetbrains.annotations.NotNull;

public final class JDBCCredentials {
    @NotNull
    public static final JDBCCredentials DEFAULT = new JDBCCredentials(
            "127.0.0.1",
            "5432",
            "db_7",
            "postgres",
            "rootpswd"
    );
    @NotNull
    private static final String PREFIX = "jdbc:postgresql";
    @NotNull
    private final String host;
    @NotNull
    private final String port;
    @NotNull
    private final String dbName;
    @NotNull
    private final String login;
    @NotNull
    private final String password;

    private JDBCCredentials(@NotNull String host, @NotNull String port, @NotNull String dbName, @NotNull String login, @NotNull String password) {
        this.host = host;
        this.port = port;
        this.dbName = dbName;
        this.login = login;
        this.password = password;
    }

    public String url() {
        return PREFIX
                + "://" + host + ':' + port
                + '/' + dbName;
    }

    public String login() {
        return login;
    }

    public String password() {
        return password;
    }
}
