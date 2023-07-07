package com.application.dao;

import com.application.model.Author;

import java.util.List;

public interface AuthorDao{
    Author get(Long id);
    Author get(String name);

    List<Author> getAll();
    Author save(Author author);
    Author update(Author author);
    void delete(Long id);
}
