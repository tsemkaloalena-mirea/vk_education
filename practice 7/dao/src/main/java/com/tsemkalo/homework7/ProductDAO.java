package com.tsemkalo.homework7;

import generated.tables.records.ProductRecord;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;

import java.util.List;

import static generated.Tables.PRODUCT;

@SuppressWarnings({"NotNullNullableValidation", "SqlNoDataSourceInspection", "SqlResolve"})
public final class ProductDAO {
    @NotNull
    private final DSLContext context;

    public ProductDAO(@NotNull DSLContext context) {
        this.context = context;
    }

    @NotNull
    public List<ProductRecord> all() {
        return context
                .selectFrom(PRODUCT)
                .fetch();
    }

    public void save(@NotNull ProductRecord record) {
        context.executeInsert(record);
    }
}
