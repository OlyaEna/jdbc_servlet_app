package com.application.servlets;

import com.application.dao.AuthorDao;
import com.application.dao.jdbc.AuthorDaoJDBC;
import com.application.model.Author;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/author/*")
public class AuthorServlet  extends HttpServlet {
    private final AuthorDao authorDao;


    public AuthorServlet() {
        super();
        authorDao = new AuthorDaoJDBC();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleRequest(request, response);
    }

    private void handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idParameter = request.getParameter("id");
        String nameParameter = request.getParameter("name");

        if (idParameter != null) {
            Author author = authorDao.get(Long.valueOf(idParameter));
            writeResponse(response, author);
        }
        if (nameParameter != null) {
            Author author = authorDao.get(nameParameter);
            writeResponse(response, author);
        } else {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            List<Author> authors = authorDao.getAll();
            try (PrintWriter writer = response.getWriter()) {
                writer.write(new Gson().toJson(authors));
            }
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }

    private void writeResponse(HttpServletResponse resp, Author author) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try (PrintWriter writer = resp.getWriter()) {
            writer.write(new Gson().toJson(author));
        }
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        Author author = new Gson().fromJson(request.getReader(), Author.class);
        author = authorDao.save(author);
        writeResponse(response, author);
        response.setStatus(HttpServletResponse.SC_CREATED);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        Author author = new Gson().fromJson(request.getReader(), Author.class);
        try {
            author= authorDao.update(author);
            writeResponse(response, author);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = Long.parseLong(req.getParameter("id"));
        authorDao.delete(id);
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}
