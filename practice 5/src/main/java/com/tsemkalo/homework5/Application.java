package com.tsemkalo.homework5;

import com.tsemkalo.homework5.configuration.JDBCCredentials;
import com.tsemkalo.homework5.dao.InvoiceDAO;
import com.tsemkalo.homework5.dao.InvoiceItemDAO;
import com.tsemkalo.homework5.dao.OrganisationDAO;
import com.tsemkalo.homework5.dao.ProductDAO;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;

public final class Application {
    private static final @NotNull
    JDBCCredentials CREDENTIALS = JDBCCredentials.DEFAULT;

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(CREDENTIALS.url(), CREDENTIALS.login(), CREDENTIALS.password())) {
            final ProductDAO productDAO = new ProductDAO(connection);

            final OrganisationDAO organisationDAO = new OrganisationDAO(connection);

            final InvoiceItemDAO invoiceItemDAO = new InvoiceItemDAO(connection);

            final InvoiceDAO invoiceDao = new InvoiceDAO(connection);

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
