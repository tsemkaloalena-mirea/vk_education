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
    @NotNull
    private final Connection connection;

    @Inject
    public InvoiceDAO(@NotNull Connection connection) {
        super(connection, "invoice", "id");
        this.connection = connection;
    }

    @Override
    public Invoice getEntityFromResultSet(ResultSet resultSet) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            ResultSet organisationResultSet = statement.executeQuery("SELECT * FROM organisation WHERE tin = " + resultSet.getLong("organisation_tin"));
            if (organisationResultSet.next()) {
                Organisation organisation = new Organisation(organisationResultSet.getLong("tin"), organisationResultSet.getString("name"), organisationResultSet.getLong("account"));
                return new Invoice(resultSet.getLong("id"), resultSet.getDate("invoice_date").toLocalDate(), organisation, this.getInvoiceItemsByInvoiceId(resultSet.getLong("id")));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        throw new IllegalStateException("Organisation with tin " + resultSet.getLong("organisation_tin") + " not found");
    }

    @Override
    public PreparedStatement fillInsertStatement(PreparedStatement preparedStatement, Invoice entity) throws SQLException {
        int fieldIndex = 1;
        preparedStatement.setDate(fieldIndex++, Date.valueOf(entity.getInvoiceDate()));
        preparedStatement.setLong(fieldIndex, entity.getOrganisation().getTIN());
        return preparedStatement;
    }

    @Override
    public PreparedStatement fillUpdateStatement(PreparedStatement preparedStatement, Invoice entity) throws SQLException {
        int fieldIndex = 1;
        preparedStatement.setDate(fieldIndex++, Date.valueOf(entity.getInvoiceDate()));
        preparedStatement.setLong(fieldIndex++, entity.getOrganisation().getTIN());
        preparedStatement.setLong(fieldIndex, entity.getId());
        return preparedStatement;
    }

    public List<InvoiceItem> getInvoiceItemsByInvoiceId(Long invoiceId) {
        final List<InvoiceItem> items = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM invoice_item WHERE invoice_id = " + invoiceId);
            while (resultSet.next()) {
                try (Statement productStatement = connection.createStatement()) {
                    ResultSet productResultSet = productStatement.executeQuery("SELECT * FROM product WHERE id = " + resultSet.getLong("product_id"));
                    if (productResultSet.next()) {
                        Product product = new Product(productResultSet.getLong("id"), productResultSet.getString("name"));
                        InvoiceItem invoiceItem = new InvoiceItem(resultSet.getLong("id"), resultSet.getInt("cost"), product, resultSet.getInt("amount"), resultSet.getLong("invoice_id"));
                        items.add(invoiceItem);
                    } else {
                        throw new IllegalStateException("Product with id " + resultSet.getLong("product_id") + " not found");
                    }
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return items;
    }
}
