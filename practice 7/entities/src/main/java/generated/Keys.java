/*
 * This file is generated by jOOQ.
 */
package generated;


import generated.tables.FlywaySchemaHistory;
import generated.tables.Manufacturer;
import generated.tables.Product;
import generated.tables.records.FlywaySchemaHistoryRecord;
import generated.tables.records.ManufacturerRecord;
import generated.tables.records.ProductRecord;

import org.jooq.ForeignKey;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;


/**
 * A class modelling foreign key relationships and constraints of tables in
 * public.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<FlywaySchemaHistoryRecord> FLYWAY_SCHEMA_HISTORY_PK = Internal.createUniqueKey(FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY, DSL.name("flyway_schema_history_pk"), new TableField[] { FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY.INSTALLED_RANK }, true);
    public static final UniqueKey<ManufacturerRecord> MANUFACTURER_PKEY = Internal.createUniqueKey(Manufacturer.MANUFACTURER, DSL.name("manufacturer_pkey"), new TableField[] { Manufacturer.MANUFACTURER.NAME }, true);
    public static final UniqueKey<ProductRecord> PRODUCT_PKEY = Internal.createUniqueKey(Product.PRODUCT, DSL.name("product_pkey"), new TableField[] { Product.PRODUCT.ID }, true);

    // -------------------------------------------------------------------------
    // FOREIGN KEY definitions
    // -------------------------------------------------------------------------

    public static final ForeignKey<ProductRecord, ManufacturerRecord> PRODUCT__PRODUCT_MANUFACTURER_NAME_FKEY = Internal.createForeignKey(Product.PRODUCT, DSL.name("product_manufacturer_name_fkey"), new TableField[] { Product.PRODUCT.MANUFACTURER_NAME }, Keys.MANUFACTURER_PKEY, new TableField[] { Manufacturer.MANUFACTURER.NAME }, true);
}