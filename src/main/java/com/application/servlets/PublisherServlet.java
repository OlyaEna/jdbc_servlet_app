package com.application.servlets;

import com.application.dao.PublisherDao;
import com.application.dao.jdbc.PublisherDaoJDBC;
import com.application.model.Author;
import com.application.model.Publisher;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/publisher/*")
public class PublisherServlet extends HttpServlet {
    private  final PublisherDao publisherDao;

    public PublisherServlet() {
        super();
        publisherDao = new PublisherDaoJDBC();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleRequest(request, response);
    }

    private void handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idParameter = request.getParameter("id");

        if (idParameter != null) {
            Publisher publisher = publisherDao.get(Long.valueOf(idParameter));
            writeResponse(response, publisher);
        } else {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            List<Publisher> publishers = publisherDao.getAll();
            try (PrintWriter writer = response.getWriter()) {
                writer.write(new Gson().toJson(publishers));
            }
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }

    private void writeResponse(HttpServletResponse resp, Publisher publisher) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try (PrintWriter writer = resp.getWriter()) {
            writer.write(new Gson().toJson(publisher));
        }
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        Publisher publisher = new Gson().fromJson(request.getReader(), Publisher.class);
        publisher = publisherDao.save(publisher);
        writeResponse(response, publisher);
        response.setStatus(HttpServletResponse.SC_CREATED);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        Publisher publisher = new Gson().fromJson(request.getReader(), Publisher.class);
        try {
            publisher= publisherDao.update(publisher);
            writeResponse(response, publisher);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = Long.parseLong(req.getParameter("id"));
        publisherDao.delete(id);
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}
