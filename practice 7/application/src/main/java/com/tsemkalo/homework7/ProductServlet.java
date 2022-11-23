package com.tsemkalo.homework7;

import generated.tables.records.ManufacturerRecord;
import generated.tables.records.ProductRecord;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@SuppressWarnings("NotNullNullableValidation")
public final class ProductServlet extends HttpServlet {
    @NotNull
    private final ProductDAO productDAO;

    @NotNull
    private final ManufacturerDAO manufacturerDAO;

    public ProductServlet(@NotNull ProductDAO productDAO, @NotNull ManufacturerDAO manufacturerDAO) {
        this.productDAO = productDAO;
        this.manufacturerDAO = manufacturerDAO;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (ServletOutputStream outputStream = resp.getOutputStream()) {

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<!DOCTYPE html>\n");
            stringBuilder.append("<html>\n");
            stringBuilder.append("<head><title>Products</title></head>\n");
            stringBuilder.append("<body>\n");
            stringBuilder.append("<h1>Products</h1>\n");

            for (ProductRecord productRecord : productDAO.all()) {
                stringBuilder.append(productRecord.toString().replace("\n", "<br>\n"));
                stringBuilder.append("<br>\n");
            }

            stringBuilder.append("</body>\n");
            stringBuilder.append("</html>");

            outputStream.write(stringBuilder.toString().getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String productName = req.getParameter("productName");
        Integer amount = Integer.parseInt(req.getParameter("amount"));
        String manufacturerName = req.getParameter("manufacturerName");
        ManufacturerRecord manufacturer = manufacturerDAO.get(manufacturerName);
        ProductRecord product = new ProductRecord()
                .setName(productName)
                .setAmount(amount)
                .setManufacturerName(manufacturerName);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Product ");
        stringBuilder.append(productName);
        stringBuilder.append(" is created. ");

        if (manufacturer == null) {
            manufacturerDAO.save(new ManufacturerRecord(manufacturerName));
            stringBuilder.append("\nManufacturer ");
            stringBuilder.append(manufacturerName);
            stringBuilder.append(" is created.");
        }
        productDAO.save(product);
        try (ServletOutputStream outputStream = resp.getOutputStream()) {
            outputStream.write(stringBuilder.toString().getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
        }
    }
}
