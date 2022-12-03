package com.tsemkalo.homework7.db_init;

public final class JDBCCredentials {

    public static final JDBCCredentials DEFAULT = new JDBCCredentials(
            "127.0.0.1",
            "5432",
            "db_7",
            "postgres",
            "rootpswd"
    );

    private static final String PREFIX = "jdbc:postgresql";

    private final String host;

    private final String port;

    private final String dbName;

    private final String login;

    private final String password;

    private JDBCCredentials(String host, String port, String dbName, String login, String password) {
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
