/*
 * This file is generated by jOOQ.
 */
package generated;


import generated.tables.FlywaySchemaHistory;
import generated.tables.Invoice;
import generated.tables.InvoiceItem;
import generated.tables.Organisation;
import generated.tables.Product;

import java.util.Arrays;
import java.util.List;

import org.jooq.Catalog;
import org.jooq.Table;
import org.jooq.impl.SchemaImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Public extends SchemaImpl {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public</code>
     */
    public static final Public PUBLIC = new Public();

    /**
     * The table <code>public.flyway_schema_history</code>.
     */
    public final FlywaySchemaHistory FLYWAY_SCHEMA_HISTORY = FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY;

    /**
     * The table <code>public.invoice</code>.
     */
    public final Invoice INVOICE = Invoice.INVOICE;

    /**
     * The table <code>public.invoice_item</code>.
     */
    public final InvoiceItem INVOICE_ITEM = InvoiceItem.INVOICE_ITEM;

    /**
     * The table <code>public.organisation</code>.
     */
    public final Organisation ORGANISATION = Organisation.ORGANISATION;

    /**
     * The table <code>public.product</code>.
     */
    public final Product PRODUCT = Product.PRODUCT;

    /**
     * No further instances allowed
     */
    private Public() {
        super("public", null);
    }


    @Override
    public Catalog getCatalog() {
        return DefaultCatalog.DEFAULT_CATALOG;
    }

    @Override
    public final List<Table<?>> getTables() {
        return Arrays.asList(
            FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY,
            Invoice.INVOICE,
            InvoiceItem.INVOICE_ITEM,
            Organisation.ORGANISATION,
            Product.PRODUCT
        );
    }
}