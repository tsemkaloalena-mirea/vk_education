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
    public InvoiceItemDAO(@NotNull Connection connection) {
        super(connection, "invoice_item", "id");
    }

    @Override
    public InvoiceItem getEntityFromResultSet(ResultSet resultSet) throws SQLException {
        return new InvoiceItem(resultSet.getLong("id"), resultSet.getInt("cost"), resultSet.getLong("product_id"), resultSet.getInt("amount"), resultSet.getLong("invoice_id"));
    }

    @Override
    public PreparedStatement fillInsertStatement(PreparedStatement preparedStatement, InvoiceItem entity) throws SQLException {
        int fieldIndex = 1;
        preparedStatement.setInt(fieldIndex++, entity.getCost());
        preparedStatement.setLong(fieldIndex++, entity.getProductId());
        preparedStatement.setInt(fieldIndex++, entity.getAmount());
        preparedStatement.setLong(fieldIndex, entity.getInvoiceId());
        return preparedStatement;
    }

    @Override
    public PreparedStatement fillUpdateStatement(PreparedStatement preparedStatement, InvoiceItem entity) throws SQLException {
        int fieldIndex = 1;
        preparedStatement.setInt(fieldIndex++, entity.getCost());
        preparedStatement.setLong(fieldIndex++, entity.getProductId());
        preparedStatement.setInt(fieldIndex++, entity.getAmount());
        preparedStatement.setLong(fieldIndex++, entity.getInvoiceId());
        preparedStatement.setLong(fieldIndex, entity.getId());
        return preparedStatement;
    }
}
