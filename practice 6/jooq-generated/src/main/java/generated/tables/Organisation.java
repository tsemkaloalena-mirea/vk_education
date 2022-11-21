/*
 * This file is generated by jOOQ.
 */
package generated.tables;


import generated.Keys;
import generated.Public;
import generated.tables.records.OrganisationRecord;

import java.util.function.Function;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Function3;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Records;
import org.jooq.Row3;
import org.jooq.Schema;
import org.jooq.SelectField;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Organisation extends TableImpl<OrganisationRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.organisation</code>
     */
    public static final Organisation ORGANISATION = new Organisation();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<OrganisationRecord> getRecordType() {
        return OrganisationRecord.class;
    }

    /**
     * The column <code>public.organisation.tin</code>.
     */
    public final TableField<OrganisationRecord, Long> TIN = createField(DSL.name("tin"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>public.organisation.name</code>.
     */
    public final TableField<OrganisationRecord, String> NAME = createField(DSL.name("name"), SQLDataType.VARCHAR.nullable(false), this, "");

    /**
     * The column <code>public.organisation.account</code>.
     */
    public final TableField<OrganisationRecord, Long> ACCOUNT = createField(DSL.name("account"), SQLDataType.BIGINT.nullable(false), this, "");

    private Organisation(Name alias, Table<OrganisationRecord> aliased) {
        this(alias, aliased, null);
    }

    private Organisation(Name alias, Table<OrganisationRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.organisation</code> table reference
     */
    public Organisation(String alias) {
        this(DSL.name(alias), ORGANISATION);
    }

    /**
     * Create an aliased <code>public.organisation</code> table reference
     */
    public Organisation(Name alias) {
        this(alias, ORGANISATION);
    }

    /**
     * Create a <code>public.organisation</code> table reference
     */
    public Organisation() {
        this(DSL.name("organisation"), null);
    }

    public <O extends Record> Organisation(Table<O> child, ForeignKey<O, OrganisationRecord> key) {
        super(child, key, ORGANISATION);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    public UniqueKey<OrganisationRecord> getPrimaryKey() {
        return Keys.ORGANISATION_PK;
    }

    @Override
    public Organisation as(String alias) {
        return new Organisation(DSL.name(alias), this);
    }

    @Override
    public Organisation as(Name alias) {
        return new Organisation(alias, this);
    }

    @Override
    public Organisation as(Table<?> alias) {
        return new Organisation(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public Organisation rename(String name) {
        return new Organisation(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Organisation rename(Name name) {
        return new Organisation(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public Organisation rename(Table<?> name) {
        return new Organisation(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row3 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row3<Long, String, Long> fieldsRow() {
        return (Row3) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function3<? super Long, ? super String, ? super Long, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function3<? super Long, ? super String, ? super Long, ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}