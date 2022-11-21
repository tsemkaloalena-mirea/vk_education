package com.tsemkalo.homework5.dao;

import com.tsemkalo.homework5.entity.AbstractEntity;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.lang.String.join;

@SuppressWarnings({"NotNullNullableValidation", "SqlNoDataSourceInspection", "SqlResolve"})
@Getter
public abstract class AbstractDAO<T extends AbstractEntity> {
    @NotNull
    private final Connection connection;

    @NotNull
    private final String uniqueKeyName;

    @NotNull
    private final String tableName;

    public AbstractDAO(@NotNull Connection connection, @NotNull String tableName, @NotNull String uniqueKeyName) {
        this.connection = connection;
        this.tableName = tableName;
        this.uniqueKeyName = uniqueKeyName;
    }

    @NotNull
    public T get(Long uniqueKey) {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + this.getTableName() + " WHERE " + this.getUniqueKeyName() + " = " + uniqueKey);
            if (resultSet.next()) {
                return getEntityFromResultSet(resultSet);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        throw new IllegalStateException("Record with " + this.getUniqueKeyName() + " " + uniqueKey + " not found");
    }

    @NotNull
    public List<T> all() {
        final List<T> result = new ArrayList<T>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + this.getTableName());
            while (resultSet.next()) {
                result.add(getEntityFromResultSet(resultSet));
            }
            return result;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return result;
    }

    public void save(@NotNull T entity) {
        try (PreparedStatement preparedStatement = this.fillInsertStatement(connection.prepareStatement(
                "INSERT INTO " + this.getTableName() + " " + this.getValuesForInsertStatement(entity)), entity)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(@NotNull T entity) {
        try (PreparedStatement preparedStatement = this.fillUpdateStatement(connection.prepareStatement(
                "UPDATE " + this.getTableName() + " SET " + this.getValuesForUpdateStatement(entity) +
                        " WHERE " + this.getUniqueKeyName() + " = ?"), entity)) {
            if (preparedStatement.executeUpdate() == 0) {
                throw new IllegalStateException("Record with " + this.getUniqueKeyName() + " " + entity.getUniqueKey() + " not found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(@NotNull T entity) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM " + this.getTableName() + " WHERE " + this.getUniqueKeyName() + " = ?")) {
            preparedStatement.setLong(1, entity.getUniqueKey());
            if (preparedStatement.executeUpdate() == 0) {
                throw new IllegalStateException("Record with " + this.getUniqueKeyName() + " " + entity.getUniqueKey() + " not found");
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void delete(@NotNull Long uniqueKey) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM " + this.getTableName() + " WHERE " + this.getUniqueKeyName() + " = ?")) {
            preparedStatement.setLong(1, uniqueKey);
            if (preparedStatement.executeUpdate() == 0) {
                throw new IllegalStateException("Record with " + this.getUniqueKeyName() + " " + uniqueKey + "not found");
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public String getValuesForInsertStatement(T entity) {
        String fields = join(", ", entity.getFields());
        String values = join(",", Collections.nCopies(entity.getFields().size(), "?"));
        return "(" + fields + ") VALUES (" + values + ")";
    }

    public String getValuesForUpdateStatement(T entity) {
        List<String> fields = new ArrayList<>();
        for (String field : entity.getFields()) {
            if (!field.equals(this.getUniqueKeyName())) {
                fields.add(field + " = ?");
            }
        }
        return join(", ", fields);
    }

    abstract public T getEntityFromResultSet(ResultSet resultSet) throws SQLException;

    abstract public PreparedStatement fillInsertStatement(PreparedStatement preparedStatement, T entity) throws SQLException;

    abstract public PreparedStatement fillUpdateStatement(PreparedStatement preparedStatement, T entity) throws SQLException;
}
