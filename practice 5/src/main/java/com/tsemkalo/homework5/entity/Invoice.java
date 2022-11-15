package com.tsemkalo.homework5.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public final class Invoice extends AbstractEntity {
    private final Long id;
    private final LocalDate invoiceDate;
    private final Organisation organisation;
    private final List<InvoiceItem> items;

    public Invoice(Long id, LocalDate invoiceDate, Organisation organisation) {
        this.id = id;
        this.invoiceDate = invoiceDate;
        this.organisation = organisation;
        this.items = new ArrayList<>();
    }

    @Override
    public Long getUniqueKey() {
        return id;
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "id=" + id +
                ", invoiceDate=" + invoiceDate +
                ", organisation=" + organisation +
                ", items=" + items +
                '}';
    }
}
