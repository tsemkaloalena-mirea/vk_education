package com.tsemkalo.homework5.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public final class InvoiceItem extends AbstractEntity {
    private final Long id;
    private final Integer cost;
    private final Product product;
    private final Integer amount;
    private final Long invoiceId;

    @Override
    public Long getUniqueKey() {
        return id;
    }

    @Override
    public List<String> getFields() {
        return List.of(new String[]{"cost", "product_id", "amount", "invoice_id"});
    }

    @Override
    public String toString() {
        return "InvoiceItem{" +
                "id=" + id +
                ", cost=" + cost +
                ", product=" + product +
                ", amount=" + amount +
                ", invoiceId=" + invoiceId +
                '}';
    }
}
