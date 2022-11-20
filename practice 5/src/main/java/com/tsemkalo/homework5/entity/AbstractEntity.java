package com.tsemkalo.homework5.entity;

import java.util.List;

public abstract class AbstractEntity {
    abstract public Long getUniqueKey();

    abstract public List<String> getFields();
}
