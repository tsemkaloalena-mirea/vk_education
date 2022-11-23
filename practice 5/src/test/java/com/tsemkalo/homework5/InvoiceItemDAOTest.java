package com.tsemkalo.homework5;

import com.tsemkalo.homework5.configuration.DBInitializer;
import com.tsemkalo.homework5.configuration.JDBCCredentials;
import com.tsemkalo.homework5.dao.InvoiceDAO;
import com.tsemkalo.homework5.dao.InvoiceItemDAO;
import com.tsemkalo.homework5.dao.ProductDAO;
import com.tsemkalo.homework5.entity.InvoiceItem;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class InvoiceItemDAOTest {
    @NotNull
    private static final JDBCCredentials CREDENTIALS = JDBCCredentials.TEST;

    @NotNull
    private InvoiceItemDAO invoiceItemDAO;

    @NotNull
    private ProductDAO productDAO;

    @BeforeEach
    public void init() {
        DBInitializer.initDb(CREDENTIALS);
        try {
            Connection connection = DriverManager.getConnection(CREDENTIALS.url(), CREDENTIALS.login(), CREDENTIALS.password());
            this.invoiceItemDAO = new InvoiceItemDAO(connection);
            this.productDAO = new ProductDAO(connection);
        } catch (SQLException exception) {
            exception.printStackTrace();
            System.exit(1);
        }
    }

    @Test
    public void getWhenEntityExists() {
        System.out.println(invoiceItemDAO.all());
        Long id = 5L;
        InvoiceItem invoiceItem = invoiceItemDAO.get(id);
        assertNotNull(invoiceItem);
        assertEquals(invoiceItem.getId(), id);
        assertNotNull(invoiceItem.getCost());
        assertNotNull(invoiceItem.getProductId());
        assertNotNull(invoiceItem.getAmount());
        assertNotNull(invoiceItem.getInvoiceId());
    }

    @Test
    public void getWhenEntityDoesNotExist() {
        Long id = 200L;
        assertThrows(IllegalStateException.class, () -> invoiceItemDAO.get(id));
    }

    @Test
    public void getAll() {
        List<InvoiceItem> invoiceItems = invoiceItemDAO.all();
        assertFalse(invoiceItems.isEmpty());
        List<Long> uniqueUsedIds = new ArrayList<>();
        for (InvoiceItem invoiceItem : invoiceItems) {
            assertNotNull(invoiceItem.getId());
            assertNotNull(invoiceItem.getCost());
            assertNotNull(invoiceItem.getProductId());
            assertNotNull(invoiceItem.getAmount());
            assertNotNull(invoiceItem.getInvoiceId());
            assertFalse(uniqueUsedIds.contains(invoiceItem.getId()));
            uniqueUsedIds.add(invoiceItem.getId());
        }
    }

    @Test
    public void saveWhenIdIsSet() {
        Long id = 5L;
        int oldSize = invoiceItemDAO.all().size();
        InvoiceItem invoiceItem = new InvoiceItem(id, 9, 1L, 17, 1L);
        invoiceItemDAO.save(invoiceItem);
        int newSize = invoiceItemDAO.all().size();
        assertEquals(oldSize, newSize - 1);
        assertNotEquals(invoiceItem.getCost(), invoiceItemDAO.get(id).getCost());
        assertNotEquals(invoiceItem.getProductId(), invoiceItemDAO.get(id).getProductId());
        assertNotEquals(invoiceItem.getAmount(), invoiceItemDAO.get(id).getAmount());
        assertNotEquals(invoiceItem.getInvoiceId(), invoiceItemDAO.get(id).getInvoiceId());
    }

    @Test
    public void saveWhenIdIsNotSet() {
        int oldSize = invoiceItemDAO.all().size();
        InvoiceItem invoiceItem = new InvoiceItem(null, 9, 1L, 17, 1L);

        invoiceItemDAO.save(invoiceItem);
        int newSize = invoiceItemDAO.all().size();
        InvoiceItem newInvoiceItem = invoiceItemDAO.all().get(newSize - 1);
        assertNotNull(newInvoiceItem.getId());
        assertEquals(invoiceItem.getCost(), newInvoiceItem.getCost());
        assertEquals(invoiceItem.getProductId(), newInvoiceItem.getProductId());
        assertEquals(invoiceItem.getAmount(), newInvoiceItem.getAmount());
        assertEquals(invoiceItem.getInvoiceId(), newInvoiceItem.getInvoiceId());
        assertEquals(newSize, oldSize + 1);
    }

    @Test
    public void saveWhenInvoiceDoesNotExist() {
        int oldSize = invoiceItemDAO.all().size();
        InvoiceItem invoiceItem = new InvoiceItem(null, 9, 1L, 17, 1001L);

        invoiceItemDAO.save(invoiceItem);

        int newSize = invoiceItemDAO.all().size();
        InvoiceItem lastInvoiceItem = invoiceItemDAO.all().get(newSize - 1);
        assertEquals(oldSize, newSize);
        assertNotEquals(invoiceItem.getCost(), lastInvoiceItem.getCost());
        assertNotEquals(invoiceItem.getProductId(), lastInvoiceItem.getProductId());
        assertNotEquals(invoiceItem.getAmount(), lastInvoiceItem.getAmount());
        assertNotEquals(invoiceItem.getInvoiceId(), lastInvoiceItem.getInvoiceId());
    }

    @Test
    public void updateWhenIdExists() {
        Long id = 5L;
        InvoiceItem invoiceItem = new InvoiceItem(id, 9, 1L, 17, 1L);
        int oldSize = invoiceItemDAO.all().size();

        invoiceItemDAO.update(invoiceItem);

        assertEquals(invoiceItemDAO.get(id).getCost(), invoiceItem.getCost());
        assertEquals(invoiceItemDAO.get(id).getProductId(), invoiceItem.getProductId());
        assertEquals(invoiceItemDAO.get(id).getAmount(), invoiceItem.getAmount());
        assertEquals(invoiceItemDAO.get(id).getInvoiceId(), invoiceItem.getInvoiceId());
        int newSize = invoiceItemDAO.all().size();
        assertEquals(newSize, oldSize);
    }

    @Test
    public void updateWhenIdDoesNotExist() {
        Long id = 5148L;
        InvoiceItem invoiceItem = new InvoiceItem(id, 9, 1L, 17, 1L);
        int oldSize = invoiceItemDAO.all().size();

        assertThrows(IllegalStateException.class, () -> invoiceItemDAO.update(invoiceItem));

        int newSize = invoiceItemDAO.all().size();
        assertEquals(newSize, oldSize);
    }

    @Test
    public void deleteWhenEntityExists() {
        Long id = 5L;
        InvoiceItem invoiceItem = new InvoiceItem(id, 14, 2L, 5, 2L);
        int oldSize = invoiceItemDAO.all().size();

        invoiceItemDAO.delete(invoiceItem);

        assertThrows(IllegalStateException.class, () -> invoiceItemDAO.get(id));
        int newSize = invoiceItemDAO.all().size();
        assertEquals(newSize, oldSize - 1);
    }

    @Test
    public void deleteWhenEntityDoesNotExist() {
        Long id = 26487L;
        InvoiceItem invoiceItem = new InvoiceItem(id, 14, 2L, 5, 2L);
        int oldSize = invoiceItemDAO.all().size();

        assertThrows(IllegalStateException.class, () -> invoiceItemDAO.delete(invoiceItem));

        int newSize = invoiceItemDAO.all().size();
        assertEquals(newSize, oldSize);
    }

    @Test
    public void deleteByIdWhenEntityExists() {
        Long id = 5L;
        int oldSize = invoiceItemDAO.all().size();

        invoiceItemDAO.delete(id);

        assertThrows(IllegalStateException.class, () -> invoiceItemDAO.get(id));
        int newSize = invoiceItemDAO.all().size();
        assertEquals(newSize, oldSize - 1);
    }

    @Test
    public void deleteByIdWhenIdDoesNotExist() {
        Long id = 26487L;
        int oldSize = invoiceItemDAO.all().size();

        assertThrows(IllegalStateException.class, () -> invoiceItemDAO.delete(id));

        int newSize = invoiceItemDAO.all().size();
        assertEquals(newSize, oldSize);
    }
}
