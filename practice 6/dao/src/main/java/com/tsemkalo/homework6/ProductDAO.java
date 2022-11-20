package com.tsemkalo.homework6;

import generated.tables.records.ProductRecord;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import org.jooq.DataType;
import org.jooq.Record2;
import org.jooq.Record5;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static generated.Tables.INVOICE;
import static generated.Tables.INVOICE_ITEM;
import static generated.Tables.PRODUCT;
import static org.jooq.impl.DSL.avg;
import static org.jooq.impl.DSL.cast;
import static org.jooq.impl.DSL.sum;

public final class ProductDAO extends AbstractDAO<ProductRecord> {
    public ProductDAO(@NotNull DSLContext context) {
        super(context, PRODUCT, PRODUCT.ID);
    }

    public List<Record5<LocalDate, Long, BigDecimal, BigDecimal, BigDecimal>> getProductsTotalForPeriod(LocalDate fromDate, LocalDate toDate) {
        return getContext()
                .select(INVOICE.INVOICE_DATE, INVOICE_ITEM.PRODUCT_ID, sum(INVOICE_ITEM.AMOUNT).as("amount"), sum(INVOICE_ITEM.COST).as("cost"), sum(INVOICE_ITEM.AMOUNT.multiply(INVOICE_ITEM.COST)).as("total"))
                .from(INVOICE_ITEM)
                .innerJoin(INVOICE).on(INVOICE_ITEM.INVOICE_ID.eq(INVOICE.ID))
                .where(INVOICE.INVOICE_DATE.between(fromDate, toDate))
                .groupBy(INVOICE.INVOICE_DATE, INVOICE_ITEM.PRODUCT_ID)
                .orderBy(INVOICE.INVOICE_DATE)
                .fetch();
    }

    public List<Record2<Long, BigDecimal>> getProductsAverageCostForPeriod(LocalDate fromDate, LocalDate toDate) {
        return getContext()
                .select(INVOICE_ITEM.PRODUCT_ID, sum(INVOICE_ITEM.COST.multiply(INVOICE_ITEM.AMOUNT)).divide(cast(sum(INVOICE_ITEM.AMOUNT), Float.class)).as("average_cost"))
                .from(INVOICE_ITEM)
                .innerJoin(INVOICE).on(INVOICE_ITEM.INVOICE_ID.eq(INVOICE.ID))
                .where(INVOICE.INVOICE_DATE.between(fromDate, toDate))
                .groupBy(INVOICE_ITEM.PRODUCT_ID)
                .fetch();


//        "select item.product_id, sum(item.cost * item.amount) / cast(sum(item.amount) as float) as average_cost from invoice_item as item\n" +
//                "inner join invoice as i on item.invoice_id = i.id\n" +
//                "where i.invoice_date between '2021-04-04' and '2021-05-24'\n" +
//                "group by item.product_id;\n"
    }
}
