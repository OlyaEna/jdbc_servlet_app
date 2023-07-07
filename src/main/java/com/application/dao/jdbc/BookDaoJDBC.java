package com.application.dao.jdbc;

import com.application.dao.BookDao;
import com.application.exceptions.NotFoundException;
import com.application.model.Author;
import com.application.model.Book;
import com.application.model.Publisher;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.application.util.Constants.*;

public class BookDaoJDBC implements BookDao {


    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Book get(Long id) {
        String sql = "SELECT b.id as book_id, b.isbn as isbn, b.title as title, b.description as description,"
                + "p.id as publisher_id, p.name as  publisher_name, p.address as publisher_address,a.id as author_id," +
                " a.name as author_name FROM book b INNER JOIN publisher p on publisher_id= p.id" +
                " inner join author_book as ab ON b.id = ab.book_id inner join author a on ab.author_id = a.id where b.id=?";
        Book book = null;

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Long book_id = resultSet.getLong("book_id");
                String title = resultSet.getString("title");
                String description = resultSet.getString("description");
                String ISBN = resultSet.getString("isbn");
                Publisher publisher = new Publisher(resultSet.getLong("publisher_id"), resultSet.getString("publisher_name"), resultSet.getString("publisher_address"));

                List<Author> authors = new ArrayList<>();
                Author author = new Author(resultSet.getLong("author_id"), resultSet.getString("author_name"));
                authors.add(author);

                book = new Book(book_id, title, description, ISBN, authors, publisher);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (book == null) {
            throw new NotFoundException("Book not found");
        }
        return book;
    }

    @Override
    public Book get(String name) {
        String sql = "SELECT b.id as book_id, b.isbn as isbn, b.title as title, b.description as description,"
                + "p.id as publisher_id, p.name as  publisher_name, p.address as publisher_address,a.id as author_id," +
                " a.name as author_name FROM book b INNER JOIN publisher p on publisher_id= p.id" +
                " inner join author_book as ab ON b.id = ab.book_id inner join author a on ab.author_id = a.id where b.title=?";
        Book book = null;

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Long book_id = resultSet.getLong("book_id");
                String title = resultSet.getString("title");
                String description = resultSet.getString("description");
                String ISBN = resultSet.getString("isbn");
                Publisher publisher = new Publisher(resultSet.getLong("publisher_id"), resultSet.getString("publisher_name"), resultSet.getString("publisher_address"));

                List<Author> authors = new ArrayList<>();
                Author author = new Author(resultSet.getLong("author_id"), resultSet.getString("author_name"));
                authors.add(author);

                book = new Book(book_id, title, description, ISBN, authors, publisher);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (book == null) {
            throw new NotFoundException("Book not found");
        }
        return book;
    }


    @Override
    public List<Book> getAll() {
        String sql = "SELECT b.id as book_id, b.isbn as isbn, b.title as title, b.description as description,"
                + "p.id as publisher_id, p.name as  publisher_name, p.address as publisher_address,a.id as author_id," +
                " a.name as author_name FROM book b INNER JOIN publisher p on publisher_id= p.id" +
                " inner join author_book as ab ON b.id = ab.book_id inner join author a on ab.author_id = a.id";

        List<Book> books = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Long id = resultSet.getLong("book_id");
                String title = resultSet.getString("title");
                String description = resultSet.getString("description");
                String ISBN = resultSet.getString("isbn");
                Publisher publisher = new Publisher(resultSet.getLong("publisher_id"), resultSet.getString("publisher_name"), resultSet.getString("publisher_address"));

                List<Author> authors = new ArrayList<>();
                Author author = new Author(resultSet.getLong("author_id"), resultSet.getString("author_name"));
                authors.add(author);

                Book book = new Book(id, title, description, ISBN, authors, publisher);
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }


    public Book save(Book book) {
        String sql = "INSERT INTO book (title, description, isbn, publisher_id) VALUES( ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            PreparedStatement preparedStatement =
                    connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, book.getTitle());
            preparedStatement.setString(2, book.getDescription());
            preparedStatement.setString(3, book.getISBN());
            preparedStatement.setLong(4, book.getPublisher().getId());

            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                book.setId(generatedKeys.getLong(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return book;

    }

    @Override
    public Book update(Book book){
        String sql = "UPDATE book SET title = ?, description=?, isbn=?, publisher_id = ? WHERE id =?";
        get(book.getId());
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            PreparedStatement preparedStatement =
                    connection.prepareStatement(sql);

            preparedStatement.setString(1, book.getTitle());
            preparedStatement.setString(2, book.getDescription());
            preparedStatement.setString(3, book.getISBN());
            preparedStatement.setLong(4, book.getPublisher().getId());
            preparedStatement.setLong(5, book.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return book;
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM book WHERE id = ?";
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
