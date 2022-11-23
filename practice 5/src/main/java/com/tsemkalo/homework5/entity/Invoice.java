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
    private final Long organisationTIN;

    @Override
    public Long getUniqueKey() {
        return id;
    }

    @Override
    public List<String> getFields() {
        return List.of(new String[]{"invoice_date", "organisation_tin"});
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "id=" + id +
                ", invoiceDate=" + invoiceDate +
                ", organisationId=" + organisationTIN +
                '}';
    }
}
