package com.tsemkalo.homework6;

import generated.tables.records.InvoiceRecord;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;

import static generated.Tables.INVOICE;

public final class InvoiceDAO extends AbstractDAO<InvoiceRecord> {
    @NotNull
    private final DSLContext context;

    public InvoiceDAO(@NotNull DSLContext context) {
        super(context, INVOICE, INVOICE.ID);
        this.context = context;
    }
}
