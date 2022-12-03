package com.tsemkalo.homework7;

import com.google.inject.Inject;
import generated.tables.pojos.Manufacturer;
import generated.tables.pojos.Product;

import java.util.List;

public final class ProductService {
    private final ProductDAO productDAO;

    private final ManufacturerDAO manufacturerDAO;

    @Inject
    public ProductService(ProductDAO productDAO, ManufacturerDAO manufacturerDAO) {
        this.productDAO = productDAO;
        this.manufacturerDAO = manufacturerDAO;
    }

    public List<Product> getProducts() {
        return productDAO.all();
    }

    public String saveProduct(String productName, Integer amount, String manufacturerName) {
        Manufacturer manufacturer = manufacturerDAO.get(manufacturerName);
        Product product = new Product(null, productName, amount, manufacturerName);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Product ");
        stringBuilder.append(productName);
        stringBuilder.append(" is created.");

        if (manufacturer == null) {
            manufacturerDAO.save(new Manufacturer(manufacturerName));
            stringBuilder.append("\nManufacturer ");
            stringBuilder.append(manufacturerName);
            stringBuilder.append(" is created.");
        }
        productDAO.save(product);
        return stringBuilder.toString();
    }
}
