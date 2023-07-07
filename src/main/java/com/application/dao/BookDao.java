package com.application.dao;

import com.application.model.Book;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface BookDao{
    Book get(Long id);

    Book get(String name);

    List<Book> getAll();
    Book save(Book book);
    Book update(Book book);
    void delete(Long id);
}
