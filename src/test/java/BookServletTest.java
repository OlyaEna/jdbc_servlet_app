import com.application.dao.jdbc.BookDaoJDBC;
import com.application.model.Author;
import com.application.model.Book;
import com.application.servlets.AuthorServlet;
import com.application.servlets.BookServlet;
import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BookServletTest {
    @Mock
    private BookDaoJDBC bookDaoJDBC;
    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    private BookServlet bookServlet;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        bookServlet = new BookServlet();
        bookServlet.setBookDao(bookDaoJDBC);
    }

    @Test
    public void testDoGet() throws Exception {
        Long id = 1L;
        Book expectedBook = new Book();

        when(request.getParameter("id")).thenReturn(String.valueOf(id));
        when(bookDaoJDBC.get(id)).thenReturn(expectedBook);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        when(response.getWriter()).thenReturn(writer);

        bookServlet.doGet(request, response);

        ArgumentCaptor<Book> argument = ArgumentCaptor.forClass(Book.class);
        verify(bookDaoJDBC).get(eq(id));
        verify(response).setContentType(eq("application/json"));
        writer.flush();

        String result = stringWriter.toString().trim();
        Book actualBook = new Gson().fromJson(result, Book.class);

        assertEquals(expectedBook, actualBook);
    }

    @Test
    public void testDoPost() throws Exception {
        Book expectedBook = new Book();

        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(new Gson().toJson(expectedBook))));
        when(bookDaoJDBC.save(any(Book.class))).thenReturn(expectedBook);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        when(response.getWriter()).thenReturn(writer);

        bookServlet.doPost(request, response);

        ArgumentCaptor<Book> argument = ArgumentCaptor.forClass(Book.class);
        verify(bookDaoJDBC).save(argument.capture());
        verify(response).setStatus(eq(HttpServletResponse.SC_CREATED));
        verify(response).setContentType(eq("application/json"));
        writer.flush();

        Book actualBook = argument.getValue();
        String result = stringWriter.toString().trim();
        assertEquals(expectedBook, actualBook);
    }

    @Test
    public void testDoPut() throws Exception {
        Long id = 1L;
        Book expectedBook = new Book();

        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(new Gson().toJson(expectedBook))));
        when(bookDaoJDBC.update(any(Book.class))).thenReturn(expectedBook);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        when(response.getWriter()).thenReturn(writer);

        bookServlet.doPut(request, response);

        ArgumentCaptor<Book> argument = ArgumentCaptor.forClass(Book.class);
        verify(bookDaoJDBC).update(argument.capture());
        verify(response).setStatus(eq(HttpServletResponse.SC_OK));
        verify(response).setContentType(eq("application/json"));
        writer.flush();


        Book actualBook = argument.getValue();
        String result = stringWriter.toString().trim();
        assertEquals(expectedBook, actualBook);
    }

    @Test
    public void testDoDelete() throws Exception {
        Long id = 1L;

        when(request.getParameter("id")).thenReturn(String.valueOf(id));

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        when(response.getWriter()).thenReturn(writer);

        bookServlet.doDelete(request, response);

        verify(bookDaoJDBC).delete(eq(id));
        verify(response).setStatus(eq(HttpServletResponse.SC_NO_CONTENT));
    }
}
