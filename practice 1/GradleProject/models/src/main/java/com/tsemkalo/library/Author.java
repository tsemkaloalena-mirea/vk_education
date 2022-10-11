package com.tsemkalo.library;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
public class Author extends AbstractEntity {
    private String name;
    private LocalDate dateOfBirth;
}
