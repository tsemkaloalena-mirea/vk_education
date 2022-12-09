package com.tsemkalo.homework6;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import org.jooq.TableField;
import org.jooq.impl.TableImpl;
import org.jooq.impl.UpdatableRecordImpl;

import java.util.List;

@Getter
@SuppressWarnings({"NotNullNullableValidation", "SqlNoDataSourceInspection", "SqlResolve"})
public abstract class AbstractDAO<T extends UpdatableRecordImpl<T>> {
    @NotNull
    private final DSLContext context;

    @NotNull
    private final TableField<T, Long> uniqueKey;

    @NotNull
    private final TableImpl<T> table;

    public AbstractDAO(@NotNull DSLContext context, @NotNull TableImpl<T> table, @NotNull TableField<T, Long> uniqueKey) {
        this.context = context;
        this.table = table;
        this.uniqueKey = uniqueKey;
    }

    @NotNull
    public T get(Long uniqueKey) {
        T record = context
                .selectFrom(this.getTable())
                .where(this.getUniqueKey().eq(uniqueKey))
                .fetchOne();
        if (record == null) {
            throw new IllegalStateException("Record with " + this.getUniqueKey().getName() + " " + uniqueKey + " not found");
        }
        return record;
    }

    @NotNull
    public List<T> all() {
        return context
                .selectFrom(this.getTable())
                .fetch();
    }

    public void save(@NotNull T record) {
        context.executeInsert(record);
    }

    public void update(@NotNull T record) {
        Long uniqueKey = record.getValue(this.getUniqueKey());
        if (uniqueKey == null) {
            throw new IllegalStateException(this.getUniqueKey().getName() + " is not set");
        }
        this.get(uniqueKey);
        context.executeUpdate(record);
    }

    public void delete(@NotNull T record) {
        Long uniqueKey = record.getValue(this.getUniqueKey());
        this.get(uniqueKey);
        context.delete(this.getTable())
                .where(this.getUniqueKey().eq((Long) record.getValue(this.getUniqueKey().getName())))
                .execute();
    }

    public void delete(@NotNull Long uniqueKey) {
        this.get(uniqueKey);
        context.delete(this.getTable())
                .where(this.getUniqueKey().eq(uniqueKey))
                .execute();
    }
}
