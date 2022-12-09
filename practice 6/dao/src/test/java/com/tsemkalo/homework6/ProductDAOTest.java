package com.tsemkalo.homework6;

import generated.tables.records.InvoiceItemRecord;
import generated.tables.records.InvoiceRecord;
import generated.tables.records.ProductRecord;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import org.jooq.Record2;
import org.jooq.Record5;
import org.jooq.SQLDialect;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static generated.Tables.INVOICE;
import static generated.Tables.INVOICE_ITEM;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProductDAOTest {
    @NotNull
    private static final JDBCCredentials CREDENTIALS = JDBCCredentials.TEST;

    @NotNull
    private ProductDAO productDAO;

    @NotNull
    private InvoiceItemDAO invoiceItemDAO;

    @NotNull
    private InvoiceDAO invoiceDAO;

    @BeforeEach
    public void init() {
        DBInitializer.initDb(CREDENTIALS);
        try {
            Connection connection = DriverManager.getConnection(CREDENTIALS.url(), CREDENTIALS.login(), CREDENTIALS.password());
            final DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);
            this.productDAO = new ProductDAO(context);
            this.invoiceItemDAO = new InvoiceItemDAO(context);
            this.invoiceDAO = new InvoiceDAO(context);
        } catch (SQLException exception) {
            exception.printStackTrace();
            System.exit(1);
        }
    }

    @Test
    public void getWhenEntityExists() {
        Long id = 17L;
        ProductRecord product = productDAO.get(id);
        assertNotNull(product);
        assertEquals(product.getId(), id);
        assertNotNull(product.getName());
    }

    @Test
    public void getWhenEntityDoesNotExist() {
        Long id = 20L;
        assertThrows(IllegalStateException.class, () -> productDAO.get(id));
    }

    @Test
    public void getAll() {
        List<ProductRecord> productList = productDAO.all();
        assertFalse(productList.isEmpty());
        List<Long> uniqueUsedIds = new ArrayList<>();
        for (ProductRecord product : productList) {
            assertNotNull(product.getId());
            assertNotNull(product.getName());
            assertFalse(uniqueUsedIds.contains(product.getId()));
            uniqueUsedIds.add(product.getId());
        }
    }

    @Test
    public void saveWhenIdIsSet() {
        Long id = 2L;
        ProductRecord product = new ProductRecord(id, "New product");

        assertThrows(DataAccessException.class, () -> productDAO.save(product));

        ProductRecord lastProduct = productDAO.all().get(productDAO.all().size() - 1);
        assertNotEquals(product.getName(), lastProduct.getName());
        assertNotEquals(product.getName(), productDAO.get(id).getName());
    }

    @Test
    public void saveWhenIdIsNotSet() {
        ProductRecord product = new ProductRecord()
                .setName("New product");
        int oldSize = productDAO.all().size();

        productDAO.save(product);
        Integer newSize = productDAO.all().size();
        ProductRecord newProduct = productDAO.all().get(newSize - 1);
        assertNotNull(newProduct.getId());
        assertEquals(product.getName(), newProduct.getName());
        assertEquals(newSize, oldSize + 1);
    }

    @Test
    public void updateWhenIdExists() {
        String newName = "Updated pr8";
        Long id = 8L;
        ProductRecord product = new ProductRecord(id, newName);
        int oldSize = productDAO.all().size();

        productDAO.update(product);

        assertEquals(productDAO.get(id).getName(), newName);
        int newSize = productDAO.all().size();
        assertEquals(newSize, oldSize);
    }

    @Test
    public void updateWhenIdDoesNotExist() {
        String newName = "Updated pr28";
        Long id = 28L;
        ProductRecord product = new ProductRecord(id, newName);
        int oldSize = productDAO.all().size();

        assertThrows(IllegalStateException.class, () -> productDAO.update(product));
        int newSize = productDAO.all().size();
        assertEquals(newSize, oldSize);
    }

    @Test
    public void updateWhenIdIsNotSet() {
        String newName = "Updated pr28";
        ProductRecord product = new ProductRecord();
        product.setName(newName);
        int oldSize = productDAO.all().size();

        assertThrows(IllegalStateException.class, () -> productDAO.update(product));
        int newSize = productDAO.all().size();
        assertEquals(newSize, oldSize);
    }

    @Test
    public void deleteWhenEntityExists() {
        Long id = 8L;
        ProductRecord product = new ProductRecord(id, "pr8");
        int oldSize = productDAO.all().size();

        productDAO.delete(product);

        assertThrows(IllegalStateException.class, () -> productDAO.get(id));
        int newSize = productDAO.all().size();
        assertEquals(newSize, oldSize - 1);
    }

    @Test
    public void deleteWhenIdDoesNotExist() {
        Long id = 28L;
        ProductRecord product = new ProductRecord(id, "pr28");
        int oldSize = productDAO.all().size();

        assertThrows(IllegalStateException.class, () -> productDAO.delete(product));
        int newSize = productDAO.all().size();
        assertEquals(newSize, oldSize);
    }

    @Test
    public void deleteByIdWhenEntityExists() {
        Long id = 8L;
        int oldSize = productDAO.all().size();

        productDAO.delete(id);

        assertThrows(IllegalStateException.class, () -> productDAO.get(id));
        int newSize = productDAO.all().size();
        assertEquals(newSize, oldSize - 1);
    }

    @Test
    public void deleteByIdWhenIdDoesNotExist() {
        Long id = 28L;
        int oldSize = productDAO.all().size();

        assertThrows(IllegalStateException.class, () -> productDAO.delete(id));
        int newSize = productDAO.all().size();
        assertEquals(newSize, oldSize);
    }

    @Test
    public void getProductsTotalForPeriod() {
        LocalDate fromDate = LocalDate.parse("2021-04-04");
        LocalDate toDate = LocalDate.parse("2022-03-04");
        int expectedTotal = 0;
        for (InvoiceItemRecord invoiceItem : invoiceItemDAO.all()) {
            LocalDate date = invoiceDAO.get(invoiceItem.getInvoiceId()).getInvoiceDate();
            if (date.isEqual(fromDate) || date.isEqual(toDate) || date.isAfter(fromDate) && date.isBefore(toDate))
                expectedTotal += invoiceItem.getAmount() * invoiceItem.getCost();
        }

        List<Record5<LocalDate, Long, BigDecimal, BigDecimal, BigDecimal>> records = productDAO.getProductsTotalForPeriod(fromDate, toDate);
        assertNotNull(records);

        Integer total = 0;
        for (Record5<LocalDate, Long, BigDecimal, BigDecimal, BigDecimal> record : records) {
            LocalDate invoiceDate = record.get(INVOICE.INVOICE_DATE);
            assertTrue(invoiceDate.isEqual(fromDate) || invoiceDate.isAfter(fromDate));
            assertTrue(invoiceDate.isEqual(toDate) || invoiceDate.isBefore(toDate));
            total += Integer.parseInt(record.get("total").toString());
        }
        assertEquals(expectedTotal, total);
    }

    @Test
    public void getProductsAverageCostForPeriod() {
        LocalDate fromDate = LocalDate.parse("2021-04-04");
        LocalDate toDate = LocalDate.parse("2022-03-04");
        List<Record2<Long, BigDecimal>> products = productDAO.getProductsAverageCostForPeriod(fromDate, toDate);
        Map<Long, List<Integer>> averageCosts = new HashMap<>();

        for (InvoiceRecord invoice : invoiceDAO.all()) {
            if (invoice.getInvoiceDate().isEqual(fromDate) || invoice.getInvoiceDate().isEqual(toDate) || invoice.getInvoiceDate().isAfter(fromDate) && invoice.getInvoiceDate().isBefore(toDate)) {
                for (InvoiceItemRecord invoiceItem : invoiceItemDAO.getInvoiceItemsByInvoiceId(invoice.getId())) {
                    if (!averageCosts.containsKey(invoiceItem.getProductId())) {
                        averageCosts.put(invoiceItem.getProductId(), new ArrayList<>());
                    }
                    for (int i = 0; i < invoiceItem.getAmount(); i++) {
                        averageCosts.get(invoiceItem.getProductId()).add(invoiceItem.getCost());
                    }
                }
            }
        }

        for (Record2<Long, BigDecimal> record : products) {
            Long productId = record.get(INVOICE_ITEM.PRODUCT_ID);
            Double expectedAvg = averageCosts.get(productId).stream().mapToDouble(Integer::intValue).sum();
            expectedAvg /= averageCosts.get(productId).size();
            assertEquals(expectedAvg, Double.parseDouble(record.get("average_cost").toString()));
        }
    }
}
