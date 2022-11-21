/*
 * This file is generated by jOOQ.
 */
package generated.tables;


import generated.Keys;
import generated.Public;
import generated.tables.records.InvoiceItemRecord;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Function5;
import org.jooq.Identity;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Records;
import org.jooq.Row5;
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
public class InvoiceItem extends TableImpl<InvoiceItemRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.invoice_item</code>
     */
    public static final InvoiceItem INVOICE_ITEM = new InvoiceItem();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<InvoiceItemRecord> getRecordType() {
        return InvoiceItemRecord.class;
    }

    /**
     * The column <code>public.invoice_item.id</code>.
     */
    public final TableField<InvoiceItemRecord, Long> ID = createField(DSL.name("id"), SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column <code>public.invoice_item.cost</code>.
     */
    public final TableField<InvoiceItemRecord, Integer> COST = createField(DSL.name("cost"), SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>public.invoice_item.product_id</code>.
     */
    public final TableField<InvoiceItemRecord, Long> PRODUCT_ID = createField(DSL.name("product_id"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>public.invoice_item.amount</code>.
     */
    public final TableField<InvoiceItemRecord, Integer> AMOUNT = createField(DSL.name("amount"), SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>public.invoice_item.invoice_id</code>.
     */
    public final TableField<InvoiceItemRecord, Long> INVOICE_ID = createField(DSL.name("invoice_id"), SQLDataType.BIGINT.nullable(false), this, "");

    private InvoiceItem(Name alias, Table<InvoiceItemRecord> aliased) {
        this(alias, aliased, null);
    }

    private InvoiceItem(Name alias, Table<InvoiceItemRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.invoice_item</code> table reference
     */
    public InvoiceItem(String alias) {
        this(DSL.name(alias), INVOICE_ITEM);
    }

    /**
     * Create an aliased <code>public.invoice_item</code> table reference
     */
    public InvoiceItem(Name alias) {
        this(alias, INVOICE_ITEM);
    }

    /**
     * Create a <code>public.invoice_item</code> table reference
     */
    public InvoiceItem() {
        this(DSL.name("invoice_item"), null);
    }

    public <O extends Record> InvoiceItem(Table<O> child, ForeignKey<O, InvoiceItemRecord> key) {
        super(child, key, INVOICE_ITEM);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    public Identity<InvoiceItemRecord, Long> getIdentity() {
        return (Identity<InvoiceItemRecord, Long>) super.getIdentity();
    }

    @Override
    public UniqueKey<InvoiceItemRecord> getPrimaryKey() {
        return Keys.INVOICE_ITEM_PK;
    }

    @Override
    public List<ForeignKey<InvoiceItemRecord, ?>> getReferences() {
        return Arrays.asList(Keys.INVOICE_ITEM__INVOICE_ITEM_PRODUCT_ID_FKEY, Keys.INVOICE_ITEM__INVOICE_ITEM_INVOICE_ID_FKEY);
    }

    private transient Product _product;
    private transient Invoice _invoice;

    /**
     * Get the implicit join path to the <code>public.product</code> table.
     */
    public Product product() {
        if (_product == null)
            _product = new Product(this, Keys.INVOICE_ITEM__INVOICE_ITEM_PRODUCT_ID_FKEY);

        return _product;
    }

    /**
     * Get the implicit join path to the <code>public.invoice</code> table.
     */
    public Invoice invoice() {
        if (_invoice == null)
            _invoice = new Invoice(this, Keys.INVOICE_ITEM__INVOICE_ITEM_INVOICE_ID_FKEY);

        return _invoice;
    }

    @Override
    public InvoiceItem as(String alias) {
        return new InvoiceItem(DSL.name(alias), this);
    }

    @Override
    public InvoiceItem as(Name alias) {
        return new InvoiceItem(alias, this);
    }

    @Override
    public InvoiceItem as(Table<?> alias) {
        return new InvoiceItem(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public InvoiceItem rename(String name) {
        return new InvoiceItem(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public InvoiceItem rename(Name name) {
        return new InvoiceItem(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public InvoiceItem rename(Table<?> name) {
        return new InvoiceItem(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row5 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row5<Long, Integer, Long, Integer, Long> fieldsRow() {
        return (Row5) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function5<? super Long, ? super Integer, ? super Long, ? super Integer, ? super Long, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function5<? super Long, ? super Integer, ? super Long, ? super Integer, ? super Long, ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}