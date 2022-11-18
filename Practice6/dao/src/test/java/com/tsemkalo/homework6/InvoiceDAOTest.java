package com.tsemkalo.homework6;

import com.tsemkalo.homework6.entity.Invoice;
import com.tsemkalo.homework6.entity.InvoiceItem;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InvoiceDAOTest {
    @NotNull
    private static final JDBCCredentials CREDENTIALS = JDBCCredentials.TEST;

    @NotNull
    private InvoiceDAO invoiceDAO;

    @NotNull
    private OrganisationDAO organisationDAO;

    @NotNull
    private InvoiceItemDAO invoiceItemDAO;

    @BeforeEach
    public void init() {
        DBInitializer.initDb(CREDENTIALS);
        try {
            Connection connection = DriverManager.getConnection(CREDENTIALS.url(), CREDENTIALS.login(), CREDENTIALS.password());
            this.invoiceDAO = new InvoiceDAO(connection);
            this.organisationDAO = new OrganisationDAO(connection);
            this.invoiceItemDAO = new InvoiceItemDAO(connection);
        } catch (SQLException exception) {
            exception.printStackTrace();
            System.exit(1);
        }
    }

    @Test
    public void getWhenEntityExists() {
        Long id = 7L;
        Invoice invoice = invoiceDAO.get(id);
        assertNotNull(invoice);
        assertEquals(invoice.getId(), id);
        assertNotNull(invoice.getInvoiceDate());
        assertNotNull(invoice.getItems());
        assertFalse(invoice.getItems().isEmpty());
    }

    @Test
    public void getWhenEntityDoesNotExist() {
        Long id = 20L;
        assertThrows(IllegalStateException.class, () -> invoiceDAO.get(id));
    }

    @Test
    public void getAll() {
        List<Invoice> invoices = invoiceDAO.all();
        assertFalse(invoices.isEmpty());
        List<Long> uniqueUsedIds = new ArrayList<>();
        for (Invoice invoice : invoices) {
            assertNotNull(invoice.getId());
            assertNotNull(invoice.getInvoiceDate());
            assertNotNull(invoice.getItems());
            assertFalse(invoice.getItems().isEmpty());
            assertFalse(uniqueUsedIds.contains(invoice.getId()));
            uniqueUsedIds.add(invoice.getId());
        }
    }

    @Test
    public void saveWhenIdIsSet() {
        Long id = 2L;
        Invoice invoice = new Invoice(id, LocalDate.now(), organisationDAO.get(11121L));
        int oldSize = invoiceDAO.all().size();

        invoiceDAO.save(invoice);

        int newSize = invoiceDAO.all().size();
        Invoice lastInvoice = invoiceDAO.all().get(invoiceDAO.all().size() - 1);
        assertEquals(invoice.getInvoiceDate(), lastInvoice.getInvoiceDate());
        assertEquals(invoice.getOrganisation().getTIN(), lastInvoice.getOrganisation().getTIN());
        assertNotEquals(invoice.getInvoiceDate(), invoiceDAO.get(id).getInvoiceDate());
        assertNotEquals(invoice.getOrganisation().getTIN(), invoiceDAO.get(id).getOrganisation().getTIN());
        assertEquals(newSize, oldSize + 1);
    }

    @Test
    public void saveWhenIdIsNotSet() {
        Invoice invoice = new Invoice(null, LocalDate.now(), organisationDAO.get(11121L));
        int oldSize = invoiceDAO.all().size();

        invoiceDAO.save(invoice);
        int newSize = invoiceDAO.all().size();
        Invoice lastInvoice = invoiceDAO.all().get(newSize - 1);
        assertNotNull(lastInvoice.getId());
        assertEquals(invoice.getInvoiceDate(), lastInvoice.getInvoiceDate());
        assertEquals(invoice.getOrganisation().getTIN(), lastInvoice.getOrganisation().getTIN());
        assertEquals(newSize, oldSize + 1);
    }

    @Test
    public void updateWhenIdExists() {
        Long id = 2L;
        Invoice invoice = new Invoice(id, LocalDate.now(), organisationDAO.get(11117L));
        int oldSize = invoiceDAO.all().size();

        invoiceDAO.update(invoice);

        assertEquals(invoiceDAO.get(id).getInvoiceDate(), invoice.getInvoiceDate());
        assertEquals(invoiceDAO.get(id).getOrganisation().getTIN(), invoice.getOrganisation().getTIN());
        assertEquals(invoiceDAO.get(id).getOrganisation().getName(), invoice.getOrganisation().getName());
        int newSize = invoiceDAO.all().size();
        assertEquals(newSize, oldSize);
    }

    @Test
    public void updateWhenIdDoesNotExist() {
        Long id = 28L;
        Invoice invoice = new Invoice(id, LocalDate.now(), organisationDAO.get(11117L));
        int oldSize = invoiceDAO.all().size();

        assertThrows(IllegalStateException.class, () -> invoiceDAO.update(invoice));

        int newSize = invoiceDAO.all().size();
        assertEquals(newSize, oldSize);
    }

    @Test
    public void deleteWhenEntityExists() {
        Long id = 8L;
        Invoice invoice = new Invoice(id, LocalDate.parse("2022-01-20"), organisationDAO.get(11118L));
        int oldSize = invoiceDAO.all().size();

        invoiceDAO.delete(invoice);

        assertThrows(IllegalStateException.class, () -> invoiceDAO.get(id));
        int newSize = invoiceDAO.all().size();
        assertEquals(newSize, oldSize - 1);
    }

    @Test
    public void deleteWhenIdDoesNotExist() {
        Long id = 28L;
        Invoice invoice = new Invoice(id, LocalDate.parse("2022-01-20"), organisationDAO.get(11118L));
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

    @Test
    public void getInvoiceItemsByInvoiceId() {
        Long invoiceId = 1L;

        List<Long> invoiceItemIds = invoiceDAO.getInvoiceItemsByInvoiceId(invoiceId).stream().map(InvoiceItem::getId).collect(Collectors.toList());

        int size = 0;
        for (InvoiceItem invoiceItem : invoiceItemDAO.all()) {
            if (invoiceId.equals(invoiceItem.getInvoiceId())) {
                size++;
                assertTrue(invoiceItemIds.contains(invoiceItem.getId()));
            }
        }
        assertEquals(size, invoiceItemIds.size());
    }
}
