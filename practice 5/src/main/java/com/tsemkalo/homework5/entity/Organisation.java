package com.tsemkalo.homework5.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public final class Organisation extends AbstractEntity {
    private final Long TIN;
    private final String name;
    private final Long account;

    @Override
    public Long getUniqueKey() {
        return TIN;
    }

    @Override
    public List<String> getFields() {
        return List.of(new String[]{"tin", "name", "account"});
    }

    @Override
    public String toString() {
        return "Organisation{" +
                "TIN=" + TIN +
                ", name='" + name + '\'' +
                ", account=" + account +
                '}';
    }
}
