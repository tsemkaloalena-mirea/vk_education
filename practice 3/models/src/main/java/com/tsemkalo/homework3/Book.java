package com.tsemkalo.homework3;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class Book extends AbstractEntity {
    private String name;
    private Author author;
}
