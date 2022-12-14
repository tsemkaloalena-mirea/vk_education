package com.tsemkalo.homework8;

import com.google.inject.Inject;
import com.tsemkalo.homework8.generated.tables.pojos.Product;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;

import java.util.List;
import java.util.stream.Collectors;

import static com.tsemkalo.homework8.generated.Tables.PRODUCT;

@SuppressWarnings({"NotNullNullableValidation", "SqlNoDataSourceInspection", "SqlResolve"})
public final class ProductDAO {
    @NotNull
    private final DSLContext context;

    @Inject
    public ProductDAO(@NotNull DSLContext context) {
        this.context = context;
    }

    @NotNull
    public List<Product> all() {
        return context
                .selectFrom(PRODUCT)
                .fetchInto(Product.class);
    }

    public void save(@NotNull Product product) {
        context.executeInsert(context.newRecord(PRODUCT, product));
    }

    public List<Integer> deleteByName(@NotNull String name) {
        List<Product> products = context
                .selectFrom(PRODUCT)
                .where(PRODUCT.NAME.eq(name))
                .fetchInto(Product.class);
        if (products.isEmpty()) {
            return null;
        }
        List<Integer> productIds = products.stream().map(Product::getId).collect(Collectors.toList());
        context.delete(PRODUCT)
                .where(PRODUCT.NAME.eq(name))
                .execute();
        return productIds;
    }

    public List<Product> getAllByManufacturer(@NotNull String manufacturerName) {
        return context
                .selectFrom(PRODUCT)
                .where(PRODUCT.MANUFACTURER_NAME.eq(manufacturerName))
                .fetchInto(Product.class);
    }
}