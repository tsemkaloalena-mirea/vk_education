package com.tsemkalo.homework6;

import generated.tables.records.InvoiceItemRecord;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import org.jooq.Result;

import java.util.List;

import static generated.Tables.INVOICE_ITEM;

public final class InvoiceItemDAO extends AbstractDAO<InvoiceItemRecord> {

    public InvoiceItemDAO(@NotNull DSLContext context) {
        super(context, INVOICE_ITEM, INVOICE_ITEM.ID);
    }

    public List<InvoiceItemRecord> getInvoiceItemsByInvoiceId(Long invoiceId) {
        Result<InvoiceItemRecord> result = getContext()
                .selectFrom(INVOICE_ITEM)
                .where(INVOICE_ITEM.INVOICE_ID.eq(invoiceId))
                .fetch();

        return result.stream().toList();
    }
}
