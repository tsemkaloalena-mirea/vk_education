package com.tsemkalo.homework7;

import com.google.inject.Inject;
import generated.tables.pojos.Product;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@SuppressWarnings("NotNullNullableValidation")
public final class ProductServlet extends HttpServlet {
    private final ProductService productService;

    @Inject
    public ProductServlet(ProductService productService) {
        this.productService = productService;
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

            for (Product product : productService.getProducts()) {
                stringBuilder.append(product.toString().replace("\n", "<br>\n"));
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

        String answer = productService.saveProduct(productName, amount, manufacturerName);

        try (ServletOutputStream outputStream = resp.getOutputStream()) {
            outputStream.write(answer.getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
        }
    }
}
