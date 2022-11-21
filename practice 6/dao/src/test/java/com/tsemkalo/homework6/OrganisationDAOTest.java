package com.tsemkalo.homework6;

import generated.tables.records.InvoiceItemRecord;
import generated.tables.records.InvoiceRecord;
import generated.tables.records.OrganisationRecord;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import org.jooq.Record2;
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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static generated.Tables.INVOICE_ITEM;
import static generated.Tables.ORGANISATION;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OrganisationDAOTest {
    @NotNull
    private static final JDBCCredentials CREDENTIALS = JDBCCredentials.TEST;

    @NotNull
    private OrganisationDAO organisationDAO;

    @NotNull
    private InvoiceDAO invoiceDAO;

    @NotNull
    private InvoiceItemDAO invoiceItemDAO;

    @BeforeEach
    public void init() {
        DBInitializer.initDb(CREDENTIALS);
        try {
            Connection connection = DriverManager.getConnection(CREDENTIALS.url(), CREDENTIALS.login(), CREDENTIALS.password());
            final DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);
            this.organisationDAO = new OrganisationDAO(context);
            this.invoiceDAO = new InvoiceDAO(context);
            this.invoiceItemDAO = new InvoiceItemDAO(context);
        } catch (SQLException exception) {
            exception.printStackTrace();
            System.exit(1);
        }
    }

    @Test
    public void getWhenEntityExists() {
        Long tin = 11116L;
        OrganisationRecord organisation = organisationDAO.get(tin);
        assertNotNull(organisation);
        assertEquals(organisation.getTin(), tin);
        assertNotNull(organisation.getName());
    }

    @Test
    public void getWhenEntityDoesNotExist() {
        Long tin = 20L;
        assertThrows(IllegalStateException.class, () -> organisationDAO.get(tin));
    }

    @Test
    public void getAll() {
        List<OrganisationRecord> organisations = organisationDAO.all();
        assertFalse(organisations.isEmpty());
        List<Long> uniqueUsedTINs = new ArrayList<>();
        for (OrganisationRecord organisation : organisations) {
            assertNotNull(organisation.getTin());
            assertNotNull(organisation.getName());
            assertNotNull(organisation.getAccount());
            assertFalse(uniqueUsedTINs.contains(organisation.getTin()));
            uniqueUsedTINs.add(organisation.getTin());
        }
    }

    @Test
    public void saveWhenTINExists() {
        Long tin = 11121L;
        int oldSize = organisationDAO.all().size();
        OrganisationRecord organisation = new OrganisationRecord(tin, "New org", 548413L);

        assertThrows(DataAccessException.class, () -> organisationDAO.save(organisation));

        int newSize = organisationDAO.all().size();
        assertEquals(oldSize, newSize);
        assertNotEquals(organisation.getName(), organisationDAO.get(tin).getName());
        assertNotEquals(organisation.getAccount(), organisationDAO.get(tin).getAccount());
    }

    @Test
    public void saveWhenTINDoesNotExist() {
        Long tin = 118541L;
        int oldSize = organisationDAO.all().size();
        OrganisationRecord organisation = new OrganisationRecord(tin, "New org", 548413L);
        organisationDAO.save(organisation);
        int newSize = organisationDAO.all().size();
        assertEquals(newSize, oldSize + 1);
        assertEquals(organisation.getName(), organisationDAO.get(tin).getName());
        assertEquals(organisation.getAccount(), organisationDAO.get(tin).getAccount());
    }

    @Test
    public void updateWhenTINExists() {
        String newName = "Updated org8";
        Long newAccount = 465481L;
        Long tin = 11118L;
        OrganisationRecord organisation = new OrganisationRecord(tin, newName, newAccount);
        int oldSize = organisationDAO.all().size();

        organisationDAO.update(organisation);

        assertEquals(organisationDAO.get(tin).getName(), newName);
        int newSize = organisationDAO.all().size();
        assertEquals(newSize, oldSize);
    }

    @Test
    public void updateWhenTINDoesNotExist() {
        String newName = "Updated org8";
        Long newAccount = 465481L;
        Long tin = 898118L;
        OrganisationRecord organisation = new OrganisationRecord(tin, newName, newAccount);
        int oldSize = organisationDAO.all().size();

        assertThrows(IllegalStateException.class, () -> organisationDAO.update(organisation));

        int newSize = organisationDAO.all().size();
        assertEquals(newSize, oldSize);
    }

    @Test
    public void deleteWhenEntityExists() {
        Long tin = 11118L;
        OrganisationRecord organisation = new OrganisationRecord(tin, "org8", 888888888L);
        int oldSize = organisationDAO.all().size();

        organisationDAO.delete(organisation);

        assertThrows(IllegalStateException.class, () -> organisationDAO.get(tin));
        int newSize = organisationDAO.all().size();
        assertEquals(newSize, oldSize - 1);
    }

    @Test
    public void deleteWhenEntityDoesNotExist() {
        Long tin = 28L;
        OrganisationRecord organisation = new OrganisationRecord(tin, "org8", 888888888L);
        int oldSize = organisationDAO.all().size();

        assertThrows(IllegalStateException.class, () -> organisationDAO.delete(organisation));

        int newSize = organisationDAO.all().size();
        assertEquals(newSize, oldSize);
    }

    @Test
    public void deleteByIdWhenEntityExists() {
        Long tin = 11118L;
        int oldSize = organisationDAO.all().size();

        organisationDAO.delete(tin);

        assertThrows(IllegalStateException.class, () -> organisationDAO.get(tin));
        int newSize = organisationDAO.all().size();
        assertEquals(newSize, oldSize - 1);
    }

    @Test
    public void deleteByIdWhenIdDoesNotExist() {
        Long tin = 28L;
        int oldSize = organisationDAO.all().size();

        assertThrows(IllegalStateException.class, () -> organisationDAO.delete(tin));

        int newSize = organisationDAO.all().size();
        assertEquals(newSize, oldSize);
    }

    @Test
    public void getOrganisationRecordsSortedByProductsAmount() {
        Map<Long, Integer> correctOrganizations = new HashMap<>();

        for (OrganisationRecord organisation : organisationDAO.all()) {
            correctOrganizations.put(organisation.getTin(), 0);
        }

        for (InvoiceItemRecord invoiceItem : invoiceItemDAO.all()) {
            Long organisationTIN = invoiceDAO.get(invoiceItem.getInvoiceId()).getOrganisationTin();
            Integer itemsAmount = correctOrganizations.get(organisationTIN);
            itemsAmount += invoiceItem.getAmount();
            correctOrganizations.put(organisationTIN, itemsAmount);
        }
        correctOrganizations = correctOrganizations.entrySet().stream()
                .sorted(Map.Entry.<Long, Integer>comparingByValue().reversed())
                .limit(10)
                .collect(Collectors.toMap(
                        Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

        List<OrganisationRecord> organisations = organisationDAO.getOrganisationsSortedByProductsAmount();
        assertEquals(correctOrganizations.size(), organisations.size());
        for (OrganisationRecord organisation : organisations) {
            assertTrue(correctOrganizations.containsKey(organisation.getTin()));
        }
    }

    @Test
    public void getOrganisationRecordsWithProductsAmountMoreThenGiven() {
        int n = 5;
        Long productId = 7L;
        Map<Long, Integer> correctOrganizations = new HashMap<>();
        for (InvoiceItemRecord invoiceItem : invoiceItemDAO.all()) {
            if (productId.equals(invoiceItem.getProductId())) {
                Long organisationTIN = invoiceDAO.get(invoiceItem.getInvoiceId()).getOrganisationTin();
                Integer itemsAmount = 0;
                if (correctOrganizations.containsKey(organisationTIN)) {
                    itemsAmount = correctOrganizations.get(organisationTIN);
                }
                itemsAmount += invoiceItem.getAmount();
                correctOrganizations.put(organisationTIN, itemsAmount);
            }
        }

        List<Long> organisationTINs = organisationDAO.getOrganisationsWithProductsAmountMoreThenGiven(n, productId)
                .stream().map(OrganisationRecord::getTin).collect(Collectors.toList());

        int size = 0;
        for (Long organisationTIN : correctOrganizations.keySet()) {
            if (correctOrganizations.get(organisationTIN) > n) {
                assertTrue(organisationTINs.contains(organisationTIN));
                size++;
            }
        }
        assertEquals(size, organisationTINs.size());
    }

    @Test
    public void getOrganisationRecordProductsForPeriod() {
        LocalDate fromDate = LocalDate.parse("2021-04-04");
        LocalDate toDate = LocalDate.parse("2021-05-24");

        Map<Long, Boolean> organisationsHavingProducts = new HashMap<>();
        for (OrganisationRecord organisation : organisationDAO.all()) {
            organisationsHavingProducts.put(organisation.getTin(), false);
        }
        Map<Long, List<Long>> correctOrganisationRecordProducts = new HashMap<>();
        for (InvoiceItemRecord invoiceItem : invoiceItemDAO.all()) {
            InvoiceRecord invoice = invoiceDAO.get(invoiceItem.getInvoiceId());
            if (invoice.getInvoiceDate().isEqual(fromDate) || invoice.getInvoiceDate().isEqual(toDate) || invoice.getInvoiceDate().isAfter(fromDate) && invoice.getInvoiceDate().isBefore(toDate)) {
                Long organisationTIN = invoice.getOrganisationTin();
                if (!correctOrganisationRecordProducts.containsKey(organisationTIN)) {
                    correctOrganisationRecordProducts.put(organisationTIN, new ArrayList<>());
                    organisationsHavingProducts.put(organisationTIN, true);
                }
                correctOrganisationRecordProducts.get(organisationTIN).add(invoiceItem.getProductId());
            }
        }

        List<Record2<Long, Long>> organisationProducts = organisationDAO.getOrganisationProductsForPeriod(fromDate, toDate);
        for (Record2<Long, Long> organisationProduct : organisationProducts) {
            Long organisationTIN = organisationProduct.get(ORGANISATION.TIN);
            Long productId = organisationProduct.get(INVOICE_ITEM.PRODUCT_ID);
            if (productId == null) {
                assertFalse(organisationsHavingProducts.get(organisationTIN));
            } else {
                assertTrue(correctOrganisationRecordProducts.containsKey(organisationTIN));
                assertTrue(correctOrganisationRecordProducts.get(organisationTIN).contains(productId));
            }
        }
    }
}
