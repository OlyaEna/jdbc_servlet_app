package com.application.dao.jdbc;

import com.application.dao.AuthorDao;
import com.application.exceptions.NotFoundException;
import com.application.model.Author;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.application.util.Constants.*;

public class AuthorDaoJDBC implements AuthorDao {

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Author get(Long id) {
        String sql = "SELECT * from author a where a.id=?";
        Author author = null;

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Long author_id = resultSet.getLong("id");
                String name = resultSet.getString("name");

                author = new Author(author_id, name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (author == null) {
            throw new NotFoundException("Author not found");
        }
        return author;
    }

    @Override
    public Author get(String name) {
        String sql = "SELECT * from author a where a.name=?";
        Author author = null;

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Long author_id = resultSet.getLong("id");
                String author_name = resultSet.getString("name");

                author = new Author(author_id, author_name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (author == null) {
            throw new NotFoundException("Author not found");
        }
        return author;
    }

    @Override
    public List<Author> getAll() {
        String sql = "SELECT * from author a";
        List<Author> authors = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Long author_id = resultSet.getLong("id");
                String name = resultSet.getString("name");

                Author author = new Author(author_id, name);
                authors.add(author);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return authors;
    }


    @Override
    public Author save(Author author) {
        String sql = "INSERT INTO author (name) VALUES( ?)";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            PreparedStatement preparedStatement =
                    connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, author.getName());

            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                author.setId(generatedKeys.getLong(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return author;

    }


    @Override
    public Author update(Author author) {
        String sql = "UPDATE author SET name = ? WHERE id =?";
        get(author.getId());
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            PreparedStatement preparedStatement =
                    connection.prepareStatement(sql);
            preparedStatement.setString(1, author.getName());
            preparedStatement.setLong(2, author.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return author;
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM author WHERE id = ?";
        get(id);
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
