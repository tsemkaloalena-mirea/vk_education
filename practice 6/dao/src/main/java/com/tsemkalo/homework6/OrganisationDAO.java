package com.tsemkalo.homework6;

import generated.tables.records.OrganisationRecord;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import org.jooq.Record2;
import org.jooq.Select;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static generated.Tables.INVOICE;
import static generated.Tables.INVOICE_ITEM;
import static generated.Tables.ORGANISATION;
import static org.jooq.impl.DSL.notExists;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.sum;

public final class OrganisationDAO extends AbstractDAO<OrganisationRecord> {
    public OrganisationDAO(@NotNull DSLContext context) {
        super(context, ORGANISATION, ORGANISATION.TIN);
    }

    public List<OrganisationRecord> getOrganisationsSortedByProductsAmount() {
        return getContext()
                .select(ORGANISATION.fields())
                .from(ORGANISATION)
                .innerJoin(INVOICE).on(ORGANISATION.TIN.eq(INVOICE.ORGANISATION_TIN))
                .innerJoin(INVOICE_ITEM).on(INVOICE.ID.eq(INVOICE_ITEM.INVOICE_ID))
                .groupBy(ORGANISATION.TIN)
                .orderBy(sum(INVOICE_ITEM.AMOUNT).desc())
                .limit(10)
                .fetchInto(ORGANISATION);
    }

    public List<OrganisationRecord> getOrganisationsWithProductsAmountMoreThenGiven(Integer amount, Long productId) {
        return getContext()
                .select(ORGANISATION.fields())
                .from(ORGANISATION)
                .innerJoin(INVOICE).on(ORGANISATION.TIN.eq(INVOICE.ORGANISATION_TIN))
                .innerJoin(INVOICE_ITEM).on(INVOICE.ID.eq(INVOICE_ITEM.INVOICE_ID).and(INVOICE_ITEM.PRODUCT_ID.eq(productId)))
                .groupBy(ORGANISATION.TIN)
                .having(sum(INVOICE_ITEM.AMOUNT).greaterThan(BigDecimal.valueOf(amount)))
                .fetchInto(ORGANISATION);
    }

    public List<Record2<Long, Long>> getOrganisationProductsForPeriod(LocalDate fromDate, LocalDate toDate) {
        Select<Record2<Long, Long>> organisationsWithoutInvoices = select(DSL.inline(null, SQLDataType.BIGINT), ORGANISATION.TIN)
                .from(ORGANISATION)
                .where(notExists(
                        getContext().select()
                                .from(INVOICE)
                                .where(INVOICE.ORGANISATION_TIN.eq(ORGANISATION.TIN).and(INVOICE.INVOICE_DATE.between(fromDate, toDate)))
                ));

        return getContext()
                .select(INVOICE_ITEM.PRODUCT_ID, ORGANISATION.TIN)
                .from(ORGANISATION)
                .innerJoin(INVOICE).on(INVOICE.ORGANISATION_TIN.eq(ORGANISATION.TIN))
                .innerJoin(INVOICE_ITEM).on(INVOICE_ITEM.INVOICE_ID.eq(INVOICE.ID))
                .where(INVOICE.INVOICE_DATE.between(fromDate, toDate))
                .union(organisationsWithoutInvoices).fetch();
    }
}
