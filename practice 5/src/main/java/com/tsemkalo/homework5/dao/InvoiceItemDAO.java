package com.tsemkalo.homework5.dao;

import com.tsemkalo.homework5.entity.InvoiceItem;
import com.tsemkalo.homework5.entity.Product;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public final class InvoiceItemDAO extends AbstractDAO<InvoiceItem> {

    @NotNull
    private final Connection connection;

    public InvoiceItemDAO(@NotNull Connection connection) {
        super(connection, "invoice_item", "id");
        this.connection = connection;
    }

    @Override
    public InvoiceItem getEntityFromResultSet(ResultSet resultSet) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            ResultSet productResultSet = statement.executeQuery("SELECT * FROM product WHERE id = " + resultSet.getLong("product_id"));
            if (productResultSet.next()) {
                Product product = new Product(productResultSet.getLong("id"), productResultSet.getString("name"));
                return new InvoiceItem(resultSet.getLong("id"), resultSet.getInt("cost"), product, resultSet.getInt("amount"), resultSet.getLong("invoice_id"));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        throw new IllegalStateException("Product with id " + resultSet.getLong("product_id") + " not found");
    }

    @Override
    public PreparedStatement fillInsertStatement(PreparedStatement preparedStatement, InvoiceItem entity) throws SQLException {
        int fieldIndex = 1;
        preparedStatement.setInt(fieldIndex++, entity.getCost());
        preparedStatement.setLong(fieldIndex++, entity.getProduct().getId());
        preparedStatement.setInt(fieldIndex++, entity.getAmount());
        preparedStatement.setLong(fieldIndex, entity.getInvoiceId());
        return preparedStatement;
    }

    @Override
    public PreparedStatement fillUpdateStatement(PreparedStatement preparedStatement, InvoiceItem entity) throws SQLException {
        int fieldIndex = 1;
        preparedStatement.setInt(fieldIndex++, entity.getCost());
        preparedStatement.setLong(fieldIndex++, entity.getProduct().getId());
        preparedStatement.setInt(fieldIndex++, entity.getAmount());
        preparedStatement.setLong(fieldIndex++, entity.getInvoiceId());
        preparedStatement.setLong(fieldIndex, entity.getId());
        return preparedStatement;
    }
}
