package com.application.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public  class Book {
    private Long id;
    private String title;
    private String description;
    private String ISBN;
    private List<Author> authors;
    private Publisher publisher;
}
