package com.tsemkalo.homework5;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tsemkalo.homework5.configuration.DBInitializer;
import com.tsemkalo.homework5.configuration.JDBCCredentials;
import com.tsemkalo.homework5.dao.InvoiceDAO;
import com.tsemkalo.homework5.dao.InvoiceItemDAO;
import com.tsemkalo.homework5.dao.OrganisationDAO;
import com.tsemkalo.homework5.entity.Invoice;
import com.tsemkalo.homework5.entity.InvoiceItem;
import com.tsemkalo.homework5.entity.Organisation;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
            this.organisationDAO = new OrganisationDAO(connection);
            this.invoiceDAO = new InvoiceDAO(connection);
            this.invoiceItemDAO = new InvoiceItemDAO(connection);
        } catch (SQLException exception) {
            exception.printStackTrace();
            System.exit(1);
        }
    }

    @Test
    public void getWhenEntityExists() {
        System.out.println(organisationDAO.all());
        Long tin = 11116L;
        Organisation organisation = organisationDAO.get(tin);
        assertNotNull(organisation);
        assertEquals(organisation.getTIN(), tin);
        assertNotNull(organisation.getName());
    }

    @Test
    public void getWhenEntityDoesNotExist() {
        Long tin = 20L;
        assertThrows(IllegalStateException.class, () -> organisationDAO.get(tin));
    }

    @Test
    public void getAll() {
        List<Organisation> organisations = organisationDAO.all();
        assertFalse(organisations.isEmpty());
        List<Long> uniqueUsedTINs = new ArrayList<>();
        for (Organisation organisation : organisations) {
            assertNotNull(organisation.getTIN());
            assertNotNull(organisation.getName());
            assertNotNull(organisation.getAccount());
            assertFalse(uniqueUsedTINs.contains(organisation.getTIN()));
            uniqueUsedTINs.add(organisation.getTIN());
        }
    }

    @Test
    public void saveWhenTINExists() {
        Long tin = 11121L;
        int oldSize = organisationDAO.all().size();
        Organisation organisation = new Organisation(tin, "New org", 548413L);
        organisationDAO.save(organisation);
        int newSize = organisationDAO.all().size();
        assertEquals(oldSize, newSize);
        assertNotEquals(organisation.getName(), organisationDAO.get(tin).getName());
        assertNotEquals(organisation.getAccount(), organisationDAO.get(tin).getAccount());
    }

    @Test
    public void saveWhenTINDoesNotExist() {
        Long tin = 118541L;
        int oldSize = organisationDAO.all().size();
        Organisation organisation = new Organisation(tin, "New org", 548413L);
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
        Organisation organisation = new Organisation(tin, newName, newAccount);
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
        Organisation organisation = new Organisation(tin, newName, newAccount);
        int oldSize = organisationDAO.all().size();

        assertThrows(IllegalStateException.class, () -> organisationDAO.update(organisation));

        int newSize = organisationDAO.all().size();
        assertEquals(newSize, oldSize);
    }

    @Test
    public void deleteWhenEntityExists() {
        Long tin = 11118L;
        Organisation organisation = new Organisation(tin, "org8", 888888888L);
        int oldSize = organisationDAO.all().size();

        organisationDAO.delete(organisation);

        assertThrows(IllegalStateException.class, () -> organisationDAO.get(tin));
        int newSize = organisationDAO.all().size();
        assertEquals(newSize, oldSize - 1);
    }

    @Test
    public void deleteWhenEntityDoesNotExist() {
        Long tin = 28L;
        Organisation organisation = new Organisation(tin, "org8", 888888888L);
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
    public void getOrganisationsSortedByProductsAmount() {
        Map<Long, Integer> correctOrganizations = new HashMap<>();

        for (InvoiceItem invoiceItem : invoiceItemDAO.all()) {
            Organisation organisation = invoiceDAO.get(invoiceItem.getInvoiceId()).getOrganisation();
            Integer itemsAmount = 0;
            if (correctOrganizations.containsKey(organisation.getTIN())) {
                itemsAmount = correctOrganizations.get(organisation.getTIN());
            }
            itemsAmount += invoiceItem.getAmount();
            correctOrganizations.put(organisation.getTIN(), itemsAmount);
        }
        correctOrganizations = correctOrganizations.entrySet().stream().
                sorted(Map.Entry.<Long, Integer>comparingByValue().reversed())
                .limit(10)
                .collect(Collectors.toMap(
                        Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

        List<Organisation> organisations = organisationDAO.getOrganisationsSortedByProductsAmount();
        System.out.println(organisations);
        System.out.println(correctOrganizations);
        assertEquals(correctOrganizations.size(), organisations.size());
        for (Organisation organisation : organisations) {
            assertTrue(correctOrganizations.containsKey(organisation.getTIN()));
        }
    }

    @Test
    public void getOrganisationsWithProductsAmountMoreThenGiven() {
        int n = 12;
        Map<Long, Integer> correctOrganizations = new HashMap<>();
        for (InvoiceItem invoiceItem : invoiceItemDAO.all()) {
            Organisation organisation = invoiceDAO.get(invoiceItem.getInvoiceId()).getOrganisation();
            Integer itemsAmount = 0;
            if (correctOrganizations.containsKey(organisation.getTIN())) {
                itemsAmount = correctOrganizations.get(organisation.getTIN());
            }
            itemsAmount += invoiceItem.getAmount();
            correctOrganizations.put(organisation.getTIN(), itemsAmount);
        }

        List<Long> organisationTINs = organisationDAO.getOrganisationsWithProductsAmountMoreThenGiven(n)
                .stream().map(Organisation::getTIN).collect(Collectors.toList());

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
    public void getOrganisationProductsForPeriod() {
        LocalDate fromDate = LocalDate.parse("2021-04-04");
        LocalDate toDate = LocalDate.parse("2021-05-24");

        Map<Long, Boolean> organisationsHavingProducts = new HashMap<>();
        for (Organisation organisation : organisationDAO.all()) {
            organisationsHavingProducts.put(organisation.getTIN(), false);
        }
        Map<Long, List<Long>> correctOrganisationProducts = new HashMap<>();
        for (InvoiceItem invoiceItem : invoiceItemDAO.all()) {
            Invoice invoice = invoiceDAO.get(invoiceItem.getInvoiceId());
            if (invoice.getInvoiceDate().isEqual(fromDate) || invoice.getInvoiceDate().isEqual(toDate) || invoice.getInvoiceDate().isAfter(fromDate) && invoice.getInvoiceDate().isBefore(toDate)) {
                Long organisationTIN = invoice.getOrganisation().getTIN();
                if (!correctOrganisationProducts.containsKey(organisationTIN)) {
                    correctOrganisationProducts.put(organisationTIN, new ArrayList<>());
                    organisationsHavingProducts.put(organisationTIN, true);
                }
                correctOrganisationProducts.get(organisationTIN).add(invoiceItem.getProduct().getId());
            }
        }

        List<JsonObject> organisationProducts = organisationDAO.getOrganisationProductsForPeriod(fromDate, toDate);
        for (JsonObject organisationProduct : organisationProducts) {
            Long organisationTIN = organisationProduct.get("tin").getAsLong();
            JsonElement productId = organisationProduct.get("product_id");
            if (productId.getAsLong() == 0) {
                assertFalse(organisationsHavingProducts.get(organisationTIN));
            } else {
                assertTrue(correctOrganisationProducts.containsKey(organisationTIN));
                assertTrue(correctOrganisationProducts.get(organisationTIN).contains(productId.getAsLong()));
            }
        }
    }
}
