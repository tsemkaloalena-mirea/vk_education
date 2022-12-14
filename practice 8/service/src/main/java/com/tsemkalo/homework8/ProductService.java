package com.tsemkalo.homework8;

import com.google.inject.Inject;
import com.tsemkalo.homework8.generated.tables.pojos.Manufacturer;
import com.tsemkalo.homework8.generated.tables.pojos.Product;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class ProductService {
    @NotNull
    private final ProductDAO productDAO;
    @NotNull
    private final ManufacturerDAO manufacturerDAO;

    @Inject
    public ProductService(@NotNull ProductDAO productDAO, @NotNull ManufacturerDAO manufacturerDAO) {
        this.productDAO = productDAO;
        this.manufacturerDAO = manufacturerDAO;
    }

    public List<Product> getAll() {
        return productDAO.all();
    }

    public String save(@NotNull Product product) {
        StringBuilder answer = new StringBuilder();
        answer.append("Product ");
        answer.append(product.getName());
        answer.append(" is created. ");
        Manufacturer manufacturer = manufacturerDAO.get(product.getManufacturerName());
        if (manufacturer == null) {
            manufacturerDAO.save(new Manufacturer(product.getManufacturerName()));
            answer.append("\nManufacturer ");
            answer.append(product.getManufacturerName());
            answer.append(" is created.");
        }
        productDAO.save(product);
        return answer.toString();
    }

    public List<Integer> deleteProductsByName(@NotNull String productName) {
        return productDAO.deleteByName(productName);
    }
}
