package com.tsemkalo.homework5.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

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
    public String toString() {
        return "Organisation{" +
                "TIN=" + TIN +
                ", name='" + name + '\'' +
                ", account=" + account +
                '}';
    }
}
