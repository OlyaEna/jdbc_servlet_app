package com.application.dao;

import com.application.model.Publisher;

import java.util.List;
import java.util.Optional;

public interface PublisherDao  {
    Publisher get(Long id);
    List<Publisher> getAll();
    Publisher save(Publisher publisher);
    Publisher update(Publisher publisher);
    void delete(Long id);
}
