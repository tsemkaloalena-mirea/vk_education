package com.tsemkalo.homework6.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class Product extends AbstractEntity {
    private final Long id;

    private final String name;

    @Override
    public Long getUniqueKey() {
        return id;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
