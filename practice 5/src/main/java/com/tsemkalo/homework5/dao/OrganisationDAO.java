package com.tsemkalo.homework5.dao;

import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.tsemkalo.homework5.entity.Organisation;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public final class OrganisationDAO extends AbstractDAO<Organisation> {
    @Inject
    public OrganisationDAO(@NotNull Connection connection) {
        super(connection, "organisation", "tin");
    }

    @Override
    public Organisation getEntityFromResultSet(ResultSet resultSet) throws SQLException {
        return new Organisation(resultSet.getLong("tin"), resultSet.getString("name"), resultSet.getLong("account"));
    }

    @Override
    public PreparedStatement fillInsertStatement(PreparedStatement preparedStatement, Organisation entity) throws SQLException {
        int fieldIndex = 1;
        preparedStatement.setLong(fieldIndex++, entity.getTIN());
        preparedStatement.setString(fieldIndex++, entity.getName());
        preparedStatement.setLong(fieldIndex, entity.getAccount());
        return preparedStatement;
    }

    @Override
    public PreparedStatement fillUpdateStatement(PreparedStatement preparedStatement, Organisation entity) throws SQLException {
        int fieldIndex = 1;
        preparedStatement.setString(fieldIndex++, entity.getName());
        preparedStatement.setLong(fieldIndex++, entity.getAccount());
        preparedStatement.setLong(fieldIndex, entity.getTIN());
        return preparedStatement;
    }

    public List<Organisation> getOrganisationsSortedByProductsAmount() {
        List<Organisation> organisations = new ArrayList<>();
        try (Statement statement = getConnection().createStatement()) {
            String query = "SELECT o.* FROM invoice AS i\n" +
                    "INNER JOIN invoice_item AS item ON i.id = item.invoice_id\n" +
                    "RIGHT JOIN organisation AS o ON o.tin = i.organisation_tin\n" +
                    "GROUP BY o.tin ORDER BY sum(item.amount) DESC NULLS LAST limit 10;";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                organisations.add(getEntityFromResultSet(resultSet));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return organisations;
    }

    public List<Organisation> getOrganisationsWithProductsAmountMoreThenGiven(Integer amount, Long productId) {
        List<Organisation> organisations = new ArrayList<>();
        try (Statement statement = getConnection().createStatement()) {
            String query = "SELECT o.*, sum(item.amount) FROM organisation AS o\n" +
                    "INNER JOIN invoice AS i ON i.organisation_tin = o.tin\n" +
                    "INNER JOIN invoice_item AS item ON i.id = item.invoice_id AND item.product_id = " + productId + "\n" +
                    "GROUP BY o.tin HAVING sum(item.amount) > " + amount;

            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                organisations.add(getEntityFromResultSet(resultSet));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return organisations;
    }

    public List<JsonObject> getOrganisationProductsForPeriod(LocalDate fromDate, LocalDate toDate) {
        List<JsonObject> products = new ArrayList<>();
        String query = "SELECT product_id, o.tin FROM organisation AS o\n" +
                "INNER JOIN invoice AS i ON i.organisation_tin = o.tin\n" +
                "\tINNER JOIN invoice_item AS item ON item.invoice_id = i.id\n" +
                "\t\tWHERE i.invoice_date BETWEEN ? AND ?\n" +
                "UNION SELECT NULL, o.tin FROM organisation AS o\n" +
                "WHERE not exists (\n" +
                "\tSELECT * FROM invoice AS i2\n" +
                "\t\tWHERE i2.organisation_tin = o.tin AND\n" +
                "\t\t\ti2.invoice_date BETWEEN ? AND ?\n" +
                ");";
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
            int fieldIndex = 1;
            preparedStatement.setDate(fieldIndex++, Date.valueOf(fromDate));
            preparedStatement.setDate(fieldIndex++, Date.valueOf(toDate));
            preparedStatement.setDate(fieldIndex++, Date.valueOf(fromDate));
            preparedStatement.setDate(fieldIndex, Date.valueOf(toDate));

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                JsonObject organisationProduct = new JsonObject();
                organisationProduct.addProperty("product_id", resultSet.getLong("product_id"));
                organisationProduct.addProperty("tin", resultSet.getLong("tin"));
                products.add(organisationProduct);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return products;
    }
}
