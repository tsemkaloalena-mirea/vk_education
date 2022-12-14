/*
 * This file is com.tsemkalo.homework8.generated by jOOQ.
 */
package com.tsemkalo.homework8.generated.tables.records;


import com.tsemkalo.homework8.generated.tables.Manufacturer;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Row1;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is com.tsemkalo.homework8.generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class ManufacturerRecord extends UpdatableRecordImpl<ManufacturerRecord> implements Record1<String> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.manufacturer.name</code>.
     */
    public ManufacturerRecord setName(String value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>public.manufacturer.name</code>.
     */
    public String getName() {
        return (String) get(0);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<String> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record1 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row1<String> fieldsRow() {
        return (Row1) super.fieldsRow();
    }

    @Override
    public Row1<String> valuesRow() {
        return (Row1) super.valuesRow();
    }

    @Override
    public Field<String> field1() {
        return Manufacturer.MANUFACTURER.NAME;
    }

    @Override
    public String component1() {
        return getName();
    }

    @Override
    public String value1() {
        return getName();
    }

    @Override
    public ManufacturerRecord value1(String value) {
        setName(value);
        return this;
    }

    @Override
    public ManufacturerRecord values(String value1) {
        value1(value1);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached ManufacturerRecord
     */
    public ManufacturerRecord() {
        super(Manufacturer.MANUFACTURER);
    }

    /**
     * Create a detached, initialised ManufacturerRecord
     */
    public ManufacturerRecord(String name) {
        super(Manufacturer.MANUFACTURER);

        setName(name);
    }

    /**
     * Create a detached, initialised ManufacturerRecord
     */
    public ManufacturerRecord(com.tsemkalo.homework8.generated.tables.pojos.Manufacturer value) {
        super(Manufacturer.MANUFACTURER);

        if (value != null) {
            setName(value.getName());
        }
    }
}
