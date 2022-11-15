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
    private final @NotNull
    Connection connection;

    @Inject
    public OrganisationDAO(@NotNull Connection connection) {
        super(connection, "organisation", "tin");
        this.connection = connection;
    }

    @Override
    public Organisation getEntityFromResultSet(ResultSet resultSet) throws SQLException {
        return new Organisation(resultSet.getLong("tin"), resultSet.getString("name"), resultSet.getLong("account"));
    }

    @Override
    public String getValuesForInsertStatement() {
        return "(tin, name, account) VALUES (?,?,?)";
    }

    @Override
    public String getValuesForUpdateStatement() {
        return "name = ?, account = ?";
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
        try (Statement statement = connection.createStatement()) {
            String query = "select o.* from organisation as o\n" +
                    "inner join invoice as i on o.tin = i.organisation_tin\n" +
                    "\tinner join invoice_item as item on i.id = item.invoice_id\n" +
                    "group by o.tin order by sum(item.amount) desc limit 10";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                organisations.add(getEntityFromResultSet(resultSet));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return organisations;
    }

    public List<Organisation> getOrganisationsWithProductsAmountMoreThenGiven(Integer amount) {
        List<Organisation> organisations = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            String query = "select o.* from organisation as o\n" +
                    "inner join invoice as i on i.organisation_tin = o.tin\n" +
                    "inner join invoice_item as item on i.id = item.invoice_id\n" +
                    "group by o.tin having sum(item.amount) > " + amount;

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
        String query = "select product_id, o.tin from organisation as o\n" +
                "inner join invoice as i on i.organisation_tin = o.tin\n" +
                "\tinner join invoice_item as item on item.invoice_id = i.id\n" +
                "\t\twhere i.invoice_date between ? and ?\n" +
                "union select NULL, o.tin from organisation as o\n" +
                "where not exists (\n" +
                "\tselect * from invoice as i2\n" +
                "\t\twhere i2.organisation_tin = o.tin and\n" +
                "\t\t\ti2.invoice_date between ? and ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
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
