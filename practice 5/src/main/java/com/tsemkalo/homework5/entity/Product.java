package com.tsemkalo.homework5.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

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
    public List<String> getFields() {
        return List.of(new String[]{"name"});
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
