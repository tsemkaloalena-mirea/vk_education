package com.tsemkalo.homework6;

import generated.tables.records.InvoiceRecord;
import generated.tables.records.ProductRecord;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;

import static generated.Tables.INVOICE;
import static generated.Tables.PRODUCT;

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


//            final InvoiceRecord result = context
//                    .selectFrom(INVOICE)
//                    .where(INVOICE.ID.eq(117L))
//                    .fetchOne();
//            System.out.println(result);
//            System.out.println(INVOICE.ID);
//            System.out.println(INVOICE.ID.getName());

//            ProductRecord productRecord = new ProductRecord();
//            productRecord.setId(18L).setName("prod188");
//
//            context.executeUpdate(productRecord);
//
//
//
//            @NotNull Result<ProductRecord> result = context
//                    .selectFrom(PRODUCT)
//                    .fetch();
//
//            for (Record record : result) {
//                System.out.println(record);
//            }

//            organisationDAO.getOrganisationsSortedByProductsAmount().forEach(System.out::println);
//            organisationDAO.getOrganisationsWithProductsAmountMoreThenGiven(12).forEach(System.out::println);
            organisationDAO.getOrganisationProductsForPeriod(LocalDate.parse("2021-04-04"), LocalDate.parse("2021-05-24")).forEach(System.out::println);
//            productDAO.getProductsTotalForPeriod(LocalDate.parse("2021-04-04"), LocalDate.parse("2022-03-04")).forEach(System.out::println);
//            productDAO.getProductsAverageCostForPeriod(LocalDate.parse("2021-04-04"), LocalDate.parse("2022-03-04")).forEach(System.out::println);

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
