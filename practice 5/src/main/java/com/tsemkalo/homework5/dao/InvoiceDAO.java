package com.tsemkalo.homework5.dao;

import com.google.inject.Inject;
import com.tsemkalo.homework5.entity.Invoice;
import com.tsemkalo.homework5.entity.InvoiceItem;
import com.tsemkalo.homework5.entity.Organisation;
import com.tsemkalo.homework5.entity.Product;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public final class InvoiceDAO extends AbstractDAO<Invoice> {
    @Inject
    public InvoiceDAO(@NotNull Connection connection) {
        super(connection, "invoice", "id");
    }

    @Override
    public Invoice getEntityFromResultSet(ResultSet resultSet) throws SQLException {
        return new Invoice(resultSet.getLong("id"), resultSet.getDate("invoice_date").toLocalDate(), resultSet.getLong("organisation_tin"));
    }

    @Override
    public PreparedStatement fillInsertStatement(PreparedStatement preparedStatement, Invoice entity) throws SQLException {
        int fieldIndex = 1;
        preparedStatement.setDate(fieldIndex++, Date.valueOf(entity.getInvoiceDate()));
        preparedStatement.setLong(fieldIndex, entity.getOrganisationTIN());
        return preparedStatement;
    }

    @Override
    public PreparedStatement fillUpdateStatement(PreparedStatement preparedStatement, Invoice entity) throws SQLException {
        int fieldIndex = 1;
        preparedStatement.setDate(fieldIndex++, Date.valueOf(entity.getInvoiceDate()));
        preparedStatement.setLong(fieldIndex++, entity.getOrganisationTIN());
        preparedStatement.setLong(fieldIndex, entity.getId());
        return preparedStatement;
    }

    public List<InvoiceItem> getInvoiceItemsByInvoiceId(Long invoiceId) {
        final List<InvoiceItem> items = new ArrayList<>();
        try (Statement statement = getConnection().createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM invoice_item WHERE invoice_id = " + invoiceId);
            while (resultSet.next()) {
                items.add(new InvoiceItem(resultSet.getLong("id"), resultSet.getInt("cost"), resultSet.getLong("product_id"), resultSet.getInt("amount"), resultSet.getLong("invoice_id")));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return items;
    }
}
