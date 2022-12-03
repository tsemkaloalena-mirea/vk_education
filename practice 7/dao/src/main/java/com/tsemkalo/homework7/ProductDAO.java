package com.tsemkalo.homework7;

import com.google.inject.Inject;
import generated.tables.pojos.Product;
import org.jooq.DSLContext;

import java.util.List;

import static generated.Tables.PRODUCT;

@SuppressWarnings({"NotNullNullableValidation", "SqlNoDataSourceInspection", "SqlResolve"})
public final class ProductDAO {
    private final DSLContext context;

    @Inject
    public ProductDAO(DSLContext context) {
        this.context = context;
    }

    public List<Product> all() {
        return context
                .selectFrom(PRODUCT)
                .fetchInto(Product.class);
    }

    public void save(Product product) {
        context.executeInsert(context.newRecord(PRODUCT, product));
    }
}
