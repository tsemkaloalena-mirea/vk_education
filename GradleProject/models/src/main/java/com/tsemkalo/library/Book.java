package com.tsemkalo.library;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class Book extends AbstractEntity {
	private String title;
	private Author author;
	private String language;
	private LocalDate creationDate;
}
