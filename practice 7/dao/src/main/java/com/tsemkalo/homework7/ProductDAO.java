package com.tsemkalo.homework7;

import com.google.inject.Inject;
import generated.tables.pojos.Product;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;

import java.util.List;

import static generated.Tables.PRODUCT;

@SuppressWarnings({"NotNullNullableValidation", "SqlNoDataSourceInspection", "SqlResolve"})
public final class ProductDAO {
    @NotNull
    private final DSLContext context;

    @Inject
    public ProductDAO(@NotNull DSLContext context) {
        this.context = context;
    }

    public List<Product> all() {
        return context
                .selectFrom(PRODUCT)
                .fetchInto(Product.class);
    }

    public void save(@NotNull Product product) {
        context.executeInsert(context.newRecord(PRODUCT, product));
    }
}
