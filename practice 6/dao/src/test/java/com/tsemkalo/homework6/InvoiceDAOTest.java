package com.tsemkalo.homework6;

import generated.tables.records.InvoiceRecord;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class InvoiceDAOTest {
    @NotNull
    private static final JDBCCredentials CREDENTIALS = JDBCCredentials.TEST;

    @NotNull
    private InvoiceDAO invoiceDAO;

    @BeforeEach
    public void init() {
        DBInitializer.initDb(CREDENTIALS);
        try {
            Connection connection = DriverManager.getConnection(CREDENTIALS.url(), CREDENTIALS.login(), CREDENTIALS.password());
            final DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);
            this.invoiceDAO = new InvoiceDAO(context);
        } catch (SQLException exception) {
            exception.printStackTrace();
            System.exit(1);
        }
    }

    @Test
    public void getWhenEntityExists() {
        Long id = 7L;
        InvoiceRecord invoice = invoiceDAO.get(id);
        assertNotNull(invoice);
        assertEquals(invoice.getId(), id);
        assertNotNull(invoice.getInvoiceDate());
    }

    @Test
    public void getWhenEntityDoesNotExist() {
        Long id = 20L;
        assertThrows(IllegalStateException.class, () -> invoiceDAO.get(id));
    }

    @Test
    public void getAll() {
        List<InvoiceRecord> invoices = invoiceDAO.all();
        assertFalse(invoices.isEmpty());
        List<Long> uniqueUsedIds = new ArrayList<>();
        for (InvoiceRecord invoice : invoices) {
            assertNotNull(invoice.getId());
            assertNotNull(invoice.getInvoiceDate());
            assertFalse(uniqueUsedIds.contains(invoice.getId()));
            uniqueUsedIds.add(invoice.getId());
        }
    }

    @Test
    public void saveWhenIdIsSet() {
        Long id = 2L;
        InvoiceRecord invoice = new InvoiceRecord(id, LocalDate.now(), 11121L);
        int oldSize = invoiceDAO.all().size();

        assertThrows(DataAccessException.class, () -> invoiceDAO.save(invoice));

        int newSize = invoiceDAO.all().size();
        InvoiceRecord lastInvoice = invoiceDAO.all().get(invoiceDAO.all().size() - 1);
        assertNotEquals(invoice.getInvoiceDate(), lastInvoice.getInvoiceDate());
        assertNotEquals(invoice.getOrganisationTin(), lastInvoice.getOrganisationTin());
        assertNotEquals(invoice.getInvoiceDate(), invoiceDAO.get(id).getInvoiceDate());
        assertNotEquals(invoice.getOrganisationTin(), invoiceDAO.get(id).getOrganisationTin());
        assertNotEquals(newSize, oldSize + 1);
    }

    @Test
    public void saveWhenIdIsNotSet() {
        InvoiceRecord invoice = new InvoiceRecord();
        invoice
                .setInvoiceDate(LocalDate.now())
                .setOrganisationTin(11121L);
        int oldSize = invoiceDAO.all().size();

        invoiceDAO.save(invoice);
        int newSize = invoiceDAO.all().size();
        InvoiceRecord lastInvoice = invoiceDAO.all().get(newSize - 1);
        assertNotNull(lastInvoice.getId());
        assertEquals(invoice.getInvoiceDate(), lastInvoice.getInvoiceDate());
        assertEquals(invoice.getOrganisationTin(), lastInvoice.getOrganisationTin());
        assertEquals(newSize, oldSize + 1);
    }

    @Test
    public void updateWhenIdExists() {
        Long id = 2L;
        InvoiceRecord invoice = new InvoiceRecord(id, LocalDate.now(), 11117L);
        int oldSize = invoiceDAO.all().size();

        invoiceDAO.update(invoice);

        assertEquals(invoiceDAO.get(id).getInvoiceDate(), invoice.getInvoiceDate());
        assertEquals(invoiceDAO.get(id).getOrganisationTin(), invoice.getOrganisationTin());
        int newSize = invoiceDAO.all().size();
        assertEquals(newSize, oldSize);
    }

    @Test
    public void updateWhenIdDoesNotExist() {
        Long id = 28L;
        InvoiceRecord invoice = new InvoiceRecord(id, LocalDate.now(), 11117L);
        int oldSize = invoiceDAO.all().size();

        assertThrows(IllegalStateException.class, () -> invoiceDAO.update(invoice));

        int newSize = invoiceDAO.all().size();
        assertEquals(newSize, oldSize);
    }

    @Test
    public void deleteWhenEntityExists() {
        Long id = 8L;
        InvoiceRecord invoice = new InvoiceRecord(id, LocalDate.parse("2022-01-20"), 11118L);
        int oldSize = invoiceDAO.all().size();

        invoiceDAO.delete(invoice);

        assertThrows(IllegalStateException.class, () -> invoiceDAO.get(id));
        int newSize = invoiceDAO.all().size();
        assertEquals(newSize, oldSize - 1);
    }

    @Test
    public void deleteWhenIdDoesNotExist() {
        Long id = 28L;
        InvoiceRecord invoice = new InvoiceRecord(id, LocalDate.parse("2022-01-20"), 11118L);
        int oldSize = invoiceDAO.all().size();

        assertThrows(IllegalStateException.class, () -> invoiceDAO.delete(invoice));
        int newSize = invoiceDAO.all().size();
        assertEquals(newSize, oldSize);
    }

    @Test
    public void deleteByIdWhenEntityExists() {
        Long id = 8L;
        int oldSize = invoiceDAO.all().size();

        invoiceDAO.delete(id);

        assertThrows(IllegalStateException.class, () -> invoiceDAO.get(id));
        int newSize = invoiceDAO.all().size();
        assertEquals(newSize, oldSize - 1);
    }

    @Test
    public void deleteByIdWhenIdDoesNotExist() {
        Long id = 28L;
        int oldSize = invoiceDAO.all().size();

        assertThrows(IllegalStateException.class, () -> invoiceDAO.delete(id));
        int newSize = invoiceDAO.all().size();
        assertEquals(newSize, oldSize);
    }
}
