package com.tsemkalo.homework8;

import com.google.inject.Inject;
import com.tsemkalo.homework8.generated.tables.pojos.Manufacturer;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;

import static com.tsemkalo.homework8.generated.Tables.MANUFACTURER;

@SuppressWarnings({"NotNullNullableValidation", "SqlNoDataSourceInspection", "SqlResolve"})
public final class ManufacturerDAO {
    @NotNull
    private final DSLContext context;

    @Inject
    public ManufacturerDAO(@NotNull DSLContext context) {
        this.context = context;
    }

    public Manufacturer get(String name) {
        return context
                .selectFrom(MANUFACTURER)
                .where(MANUFACTURER.NAME.eq(name))
                .fetchOneInto(Manufacturer.class);
    }

    public void save(@NotNull Manufacturer manufacturer) {
        context.executeInsert(context.newRecord(MANUFACTURER, manufacturer));
    }
}