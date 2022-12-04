package com.tsemkalo.homework8;

import com.google.inject.Inject;
import com.tsemkalo.homework8.generated.tables.pojos.Product;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class ManufacturerService {
    @NotNull
    private final ProductDAO productDAO;

    @Inject
    public ManufacturerService(@NotNull ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public List<Product> getProductsByManufacturer(@NotNull String name) {
        return productDAO.getAllByManufacturer(name);
    }
}
