/*
 * This file is generated by jOOQ.
 */
package generated.tables;


import generated.Keys;
import generated.Public;
import generated.tables.records.InvoiceRecord;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Function3;
import org.jooq.Identity;
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
public class Invoice extends TableImpl<InvoiceRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.invoice</code>
     */
    public static final Invoice INVOICE = new Invoice();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<InvoiceRecord> getRecordType() {
        return InvoiceRecord.class;
    }

    /**
     * The column <code>public.invoice.id</code>.
     */
    public final TableField<InvoiceRecord, Long> ID = createField(DSL.name("id"), SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column <code>public.invoice.invoice_date</code>.
     */
    public final TableField<InvoiceRecord, LocalDate> INVOICE_DATE = createField(DSL.name("invoice_date"), SQLDataType.LOCALDATE.nullable(false), this, "");

    /**
     * The column <code>public.invoice.organisation_tin</code>.
     */
    public final TableField<InvoiceRecord, Long> ORGANISATION_TIN = createField(DSL.name("organisation_tin"), SQLDataType.BIGINT.nullable(false), this, "");

    private Invoice(Name alias, Table<InvoiceRecord> aliased) {
        this(alias, aliased, null);
    }

    private Invoice(Name alias, Table<InvoiceRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.invoice</code> table reference
     */
    public Invoice(String alias) {
        this(DSL.name(alias), INVOICE);
    }

    /**
     * Create an aliased <code>public.invoice</code> table reference
     */
    public Invoice(Name alias) {
        this(alias, INVOICE);
    }

    /**
     * Create a <code>public.invoice</code> table reference
     */
    public Invoice() {
        this(DSL.name("invoice"), null);
    }

    public <O extends Record> Invoice(Table<O> child, ForeignKey<O, InvoiceRecord> key) {
        super(child, key, INVOICE);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    public Identity<InvoiceRecord, Long> getIdentity() {
        return (Identity<InvoiceRecord, Long>) super.getIdentity();
    }

    @Override
    public UniqueKey<InvoiceRecord> getPrimaryKey() {
        return Keys.INVOICE_PK;
    }

    @Override
    public List<ForeignKey<InvoiceRecord, ?>> getReferences() {
        return Arrays.asList(Keys.INVOICE__INVOICE_ORGANISATION_TIN_FKEY);
    }

    private transient Organisation _organisation;

    /**
     * Get the implicit join path to the <code>public.organisation</code> table.
     */
    public Organisation organisation() {
        if (_organisation == null)
            _organisation = new Organisation(this, Keys.INVOICE__INVOICE_ORGANISATION_TIN_FKEY);

        return _organisation;
    }

    @Override
    public Invoice as(String alias) {
        return new Invoice(DSL.name(alias), this);
    }

    @Override
    public Invoice as(Name alias) {
        return new Invoice(alias, this);
    }

    @Override
    public Invoice as(Table<?> alias) {
        return new Invoice(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public Invoice rename(String name) {
        return new Invoice(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Invoice rename(Name name) {
        return new Invoice(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public Invoice rename(Table<?> name) {
        return new Invoice(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row3 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row3<Long, LocalDate, Long> fieldsRow() {
        return (Row3) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function3<? super Long, ? super LocalDate, ? super Long, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function3<? super Long, ? super LocalDate, ? super Long, ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}