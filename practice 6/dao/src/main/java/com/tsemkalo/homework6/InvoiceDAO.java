package com.tsemkalo.homework6;

import generated.tables.records.InvoiceRecord;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;

import static generated.Tables.INVOICE;

public final class InvoiceDAO extends AbstractDAO<InvoiceRecord> {
    public InvoiceDAO(@NotNull DSLContext context) {
        super(context, INVOICE, INVOICE.ID);
    }
}
