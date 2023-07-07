package com.application.servlets;

import com.application.dao.BookDao;
import com.application.dao.jdbc.BookDaoJDBC;
import com.application.model.Book;
import com.google.gson.Gson;
import lombok.Setter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/books/*")
@Setter
public class BookServlet extends HttpServlet {

    private BookDao bookDao;
    public BookServlet() {
        super();
        bookDao = new BookDaoJDBC();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleRequest(request, response);
    }

    private void handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idParameter = request.getParameter("id");
        String nameParameter = request.getParameter("name");

        if (idParameter != null) {
            Book book = bookDao.get(Long.valueOf(idParameter));
            writeResponse(response, book);
        }
        if (nameParameter != null) {
            Book book = bookDao.get(nameParameter);
            writeResponse(response, book);
        } else {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            List<Book> books = bookDao.getAll();
            try (PrintWriter writer = response.getWriter()) {
                writer.write(new Gson().toJson(books));
            }
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }


    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        Book book = new Gson().fromJson(request.getReader(), Book.class);
        book = bookDao.save(book);
        writeResponse(response, book);
        response.setStatus(HttpServletResponse.SC_CREATED);
    }

    private void writeResponse(HttpServletResponse resp, Book book) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try (PrintWriter writer = resp.getWriter()) {
            writer.write(new Gson().toJson(book));
        }
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        Book book = new Gson().fromJson(request.getReader(), Book.class);
        try {
            book = bookDao.update(book);
            writeResponse(response, book);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = Long.parseLong(req.getParameter("id"));
        bookDao.delete(id);
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}
