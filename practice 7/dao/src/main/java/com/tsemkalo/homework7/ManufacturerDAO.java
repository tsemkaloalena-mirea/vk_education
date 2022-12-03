package com.tsemkalo.homework7;

import com.google.inject.Inject;
import generated.tables.pojos.Manufacturer;
import org.jooq.DSLContext;

import static generated.Tables.MANUFACTURER;

@SuppressWarnings({"NotNullNullableValidation", "SqlNoDataSourceInspection", "SqlResolve"})
public final class ManufacturerDAO {
    private final DSLContext context;

    @Inject
    public ManufacturerDAO(DSLContext context) {
        this.context = context;
    }

    public Manufacturer get(String name) {
        return context
                .selectFrom(MANUFACTURER)
                .where(MANUFACTURER.NAME.eq(name))
                .fetchOneInto(Manufacturer.class);
    }

    public void save(Manufacturer manufacturer) {
        context.executeInsert(context.newRecord(MANUFACTURER, manufacturer));
    }
}
