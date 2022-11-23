package com.tsemkalo.homework7;

import generated.tables.records.ManufacturerRecord;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;

import static generated.Tables.MANUFACTURER;

@SuppressWarnings({"NotNullNullableValidation", "SqlNoDataSourceInspection", "SqlResolve"})
public final class ManufacturerDAO {
    @NotNull
    private final DSLContext context;

    public ManufacturerDAO(@NotNull DSLContext context) {
        this.context = context;
    }

    public ManufacturerRecord get(String name) {
        return context
                .selectFrom(MANUFACTURER)
                .where(MANUFACTURER.NAME.eq(name))
                .fetchOne();
    }

    public void save(@NotNull ManufacturerRecord record) {
        context.executeInsert(record);
    }
}
