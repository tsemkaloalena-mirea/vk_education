package com.tsemkalo.homework6;

import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;

public final class Application {
    private static final @NotNull
    JDBCCredentials CREDENTIALS = JDBCCredentials.DEFAULT;

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(CREDENTIALS.url(), CREDENTIALS.login(), CREDENTIALS.password())) {
            final DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);

            final ProductDAO productDAO = new ProductDAO(context);

            final OrganisationDAO organisationDAO = new OrganisationDAO(context);

            final InvoiceItemDAO invoiceItemDAO = new InvoiceItemDAO(context);

            final InvoiceDAO invoiceDao = new InvoiceDAO(context);

//            organisationDAO.getOrganisationsSortedByProductsAmount().forEach(System.out::println);
//            organisationDAO.getOrganisationsWithProductsAmountMoreThenGiven(5, 7L).forEach(System.out::println);
//            organisationDAO.getOrganisationProductsForPeriod(LocalDate.parse("2021-04-04"), LocalDate.parse("2021-05-24")).forEach(System.out::println);
//            productDAO.getProductsTotalForPeriod(LocalDate.parse("2021-04-04"), LocalDate.parse("2022-03-04")).forEach(System.out::println);
//            productDAO.getProductsAverageCostForPeriod(LocalDate.parse("2021-04-04"), LocalDate.parse("2022-03-04")).forEach(System.out::println);

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
