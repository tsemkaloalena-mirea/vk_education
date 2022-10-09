package com.tsemkalo.library;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Author extends AbstractEntity {
	private String name;
	private LocalDate dateOfBirth;
}
