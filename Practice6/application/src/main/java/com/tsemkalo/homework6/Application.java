package com.tsemkalo.homework6;

import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static generated.Tables.INVOICE;

public final class Application {
    private static final @NotNull
    JDBCCredentials CREDENTIALS = JDBCCredentials.DEFAULT;

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(CREDENTIALS.url(), CREDENTIALS.login(), CREDENTIALS.password())) {
            final DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);

            final ProductDAO productDAO = new ProductDAO(connection);

            final OrganisationDAO organisationDAO = new OrganisationDAO(connection);

            final InvoiceItemDAO invoiceItemDAO = new InvoiceItemDAO(connection);

            final InvoiceDAO invoiceDao = new InvoiceDAO(connection);

            final Result<Record> result = context
                    .select()
                    .from(INVOICE)
                    .fetch();

            for (Record record : result) {
                System.out.println(record);
            }

//            organisationDAO.getOrganisationsSortedByProductsAmount().forEach(System.out::println);
//            organisationDAO.getOrganisationsWithProductsAmountMoreThenGiven(12).forEach(System.out::println);
//            organisationDAO.getOrganisationProductsForPeriod(LocalDate.parse("2021-04-04"), LocalDate.parse("2021-05-24")).forEach(System.out::println);
//            productDAO.getProductsTotalForPeriod(LocalDate.parse("2021-04-04"), LocalDate.parse("2022-03-04")).forEach(System.out::println);
//            productDAO.getProductsAverageCostForPeriod(LocalDate.parse("2021-04-04"), LocalDate.parse("2022-03-04")).forEach(System.out::println);

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
