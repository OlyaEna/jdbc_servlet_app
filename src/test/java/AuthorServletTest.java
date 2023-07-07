import com.application.dao.jdbc.AuthorDaoJDBC;
import com.application.model.Author;
import com.application.servlets.AuthorServlet;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;

import static org.mockito.Mockito.when;;
import static org.mockito.Mockito.verify;


public class AuthorServletTest {
    @Mock
    private AuthorDaoJDBC authorDaoJDBC;
    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    private AuthorServlet authorServlet;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        authorServlet = new AuthorServlet();
        authorServlet.setAuthorDao(authorDaoJDBC);
    }

    @Test
    public void testDoGet() throws Exception {
        Long id = 1L;
        Author expectedAuthor = new Author();

        when(request.getParameter("id")).thenReturn(String.valueOf(id));
        when(authorDaoJDBC.get(id)).thenReturn(expectedAuthor);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        when(response.getWriter()).thenReturn(writer);

        authorServlet.doGet(request, response);

        ArgumentCaptor<Author> argument = ArgumentCaptor.forClass(Author.class);
        verify(authorDaoJDBC).get(eq(id));
        verify(response).setContentType(eq("application/json"));
        writer.flush();

        String result = stringWriter.toString().trim();
        Author actualAuthor = new Gson().fromJson(result, Author.class);

        assertEquals(expectedAuthor, actualAuthor);
    }

    @Test
    public void testDoPost() throws Exception {
        Author expectedAuthor = new Author();

        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(new Gson().toJson(expectedAuthor))));
        when(authorDaoJDBC.save(any(Author.class))).thenReturn(expectedAuthor);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        when(response.getWriter()).thenReturn(writer);

        authorServlet.doPost(request, response);

        ArgumentCaptor<Author> argument = ArgumentCaptor.forClass(Author.class);
        verify(authorDaoJDBC).save(argument.capture());
        verify(response).setStatus(eq(HttpServletResponse.SC_CREATED));
        verify(response).setContentType(eq("application/json"));
        writer.flush();

        Author actualAuthor = argument.getValue();
        String result = stringWriter.toString().trim();
        assertEquals(expectedAuthor, actualAuthor);
    }

    @Test
    public void testDoPut() throws Exception {
        Long id = 1L;
        Author expectedAuthor = new Author();

        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(new Gson().toJson(expectedAuthor))));
        when(authorDaoJDBC.update(any(Author.class))).thenReturn(expectedAuthor);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        when(response.getWriter()).thenReturn(writer);

        authorServlet.doPut(request, response);

            ArgumentCaptor<Author> argument = ArgumentCaptor.forClass(Author.class);
        verify(authorDaoJDBC).update(argument.capture());
        verify(response).setStatus(eq(HttpServletResponse.SC_OK));
        verify(response).setContentType(eq("application/json"));
        writer.flush();

        Author actualAuthor = argument.getValue();
        String result = stringWriter.toString().trim();
        assertEquals(expectedAuthor, actualAuthor);
    }

    @Test
    public void testDoDelete() throws Exception {
        Long id = 1L;

        when(request.getParameter("id")).thenReturn(String.valueOf(id));

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        when(response.getWriter()).thenReturn(writer);

        authorServlet.doDelete(request, response);

        verify(authorDaoJDBC).delete(eq(id));
        verify(response).setStatus(eq(HttpServletResponse.SC_NO_CONTENT));
    }
}
