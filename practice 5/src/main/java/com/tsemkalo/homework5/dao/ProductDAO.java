package com.tsemkalo.homework5.dao;

import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.tsemkalo.homework5.entity.Product;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public final class ProductDAO extends AbstractDAO<Product> {
    @Inject
    public ProductDAO(@NotNull Connection connection) {
        super(connection, "product", "id");
    }

    @Override
    public Product getEntityFromResultSet(ResultSet resultSet) throws SQLException {
        return new Product(resultSet.getLong("id"), resultSet.getString("name"));
    }

    @Override
    public PreparedStatement fillInsertStatement(PreparedStatement preparedStatement, Product entity) throws SQLException {
        int fieldIndex = 1;
        preparedStatement.setString(fieldIndex, entity.getName());
        return preparedStatement;
    }

    @Override
    public PreparedStatement fillUpdateStatement(PreparedStatement preparedStatement, Product entity) throws SQLException {
        int fieldIndex = 1;
        preparedStatement.setString(fieldIndex++, entity.getName());
        preparedStatement.setLong(fieldIndex, entity.getId());
        return preparedStatement;
    }

    public List<JsonObject> getProductsTotalForPeriod(LocalDate fromDate, LocalDate toDate) {
        List<JsonObject> products = new ArrayList<>();
        String query = "SELECT i.invoice_date, item.product_id, sum(item.amount) AS amount, sum(item.cost) AS cost, sum(item.amount * item.cost) AS total FROM invoice_item AS item\n" +
                "INNER JOIN invoice AS i ON item.invoice_id = i.id\n" +
                "WHERE i.invoice_date BETWEEN ? AND ?\n" +
                "GROUP BY i.invoice_date, item.product_id ORDER BY i.invoice_date;";
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
            int fieldIndex = 1;
            preparedStatement.setDate(fieldIndex++, Date.valueOf(fromDate));
            preparedStatement.setDate(fieldIndex, Date.valueOf(toDate));
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                JsonObject productReport = new JsonObject();
                productReport.addProperty("invoiceDate", resultSet.getDate("invoice_date").toString());
                productReport.addProperty("productId", resultSet.getLong("product_id"));
                productReport.addProperty("amount", resultSet.getInt("amount"));
                productReport.addProperty("cost", resultSet.getInt("cost"));
                productReport.addProperty("total", resultSet.getInt("total"));
                products.add(productReport);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return products;
    }

    public List<JsonObject> getProductsAverageCostForPeriod(LocalDate fromDate, LocalDate toDate) {
        List<JsonObject> products = new ArrayList<>();
        String query = "SELECT item.product_id, sum(item.cost * item.amount) / CAST(sum(item.amount) AS float) AS average_cost FROM invoice_item AS item\n" +
                "INNER JOIN invoice AS i ON item.invoice_id = i.id\n" +
                "WHERE i.invoice_date BETWEEN ? AND ?\n" +
                "GROUP BY item.product_id;";
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
            int fieldIndex = 1;
            preparedStatement.setDate(fieldIndex++, Date.valueOf(fromDate));
            preparedStatement.setDate(fieldIndex, Date.valueOf(toDate));

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                JsonObject productAverageCost = new JsonObject();
                productAverageCost.addProperty("product_id", resultSet.getLong("product_id"));
                productAverageCost.addProperty("average_cost", resultSet.getDouble("average_cost"));
                products.add(productAverageCost);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return products;
    }
}
