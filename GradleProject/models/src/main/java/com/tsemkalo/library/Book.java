package com.tsemkalo.library;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter // TODO dependencies
@Setter
public class Book extends AbstractEntity {
	private String title;
	private Author author;
	private String language;
	private LocalDate creationDate;
}
