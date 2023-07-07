import com.application.dao.jdbc.AuthorDaoJDBC;
import com.application.dao.jdbc.PublisherDaoJDBC;
import com.application.model.Author;
import com.application.model.Publisher;
import com.application.servlets.AuthorServlet;
import com.application.servlets.PublisherServlet;
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

public class PublisherServletTest {
    @Mock
    private PublisherDaoJDBC publisherDaoJDBC;
    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    private PublisherServlet publisherServlet;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        publisherServlet = new PublisherServlet();
        publisherServlet.setPublisherDao(publisherDaoJDBC);
    }

    @Test
    public void testDoGet() throws Exception {
        Long id = 1L;
        Publisher expectedPublisher = new Publisher();

        when(request.getParameter("id")).thenReturn(String.valueOf(id));
        when(publisherDaoJDBC.get(id)).thenReturn(expectedPublisher);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        when(response.getWriter()).thenReturn(writer);

        publisherServlet.doGet(request, response);

        ArgumentCaptor<Author> argument = ArgumentCaptor.forClass(Author.class);
        verify(publisherDaoJDBC).get(eq(id));
        verify(response).setContentType(eq("application/json"));
        writer.flush();

        String result = stringWriter.toString().trim();
        Publisher actualPublisher = new Gson().fromJson(result, Publisher.class);

        assertEquals(expectedPublisher, actualPublisher);
    }

    @Test
    public void testDoPost() throws Exception {
        Publisher expectedPublisher = new Publisher();

        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(new Gson().toJson(expectedPublisher))));
        when(publisherDaoJDBC.save(any(Publisher.class))).thenReturn(expectedPublisher);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        when(response.getWriter()).thenReturn(writer);

        publisherServlet.doPost(request, response);

        ArgumentCaptor<Publisher> argument = ArgumentCaptor.forClass(Publisher.class);
        verify(publisherDaoJDBC).save(argument.capture());
        verify(response).setStatus(eq(HttpServletResponse.SC_CREATED));
        verify(response).setContentType(eq("application/json"));
        writer.flush();

        Publisher actualPublisher = argument.getValue();
        String result = stringWriter.toString().trim();
        assertEquals(expectedPublisher, actualPublisher);
    }

    @Test
    public void testDoPut() throws Exception {
        Long id = 1L;
        Publisher expectedPublisher = new Publisher();

        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(new Gson().toJson(expectedPublisher))));
        when(publisherDaoJDBC.update(any(Publisher.class))).thenReturn(expectedPublisher);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        when(response.getWriter()).thenReturn(writer);

        publisherServlet.doPut(request, response);

        ArgumentCaptor<Publisher> argument = ArgumentCaptor.forClass(Publisher.class);
        verify(publisherDaoJDBC).update(argument.capture());
        verify(response).setStatus(eq(HttpServletResponse.SC_OK));
        verify(response).setContentType(eq("application/json"));
        writer.flush();

        Publisher actualPublisher = argument.getValue();
        String result = stringWriter.toString().trim();
        assertEquals(expectedPublisher, actualPublisher);
    }

    @Test
    public void testDoDelete() throws Exception {
        Long id = 1L;

        when(request.getParameter("id")).thenReturn(String.valueOf(id));

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        when(response.getWriter()).thenReturn(writer);

        publisherServlet.doDelete(request, response);


        verify(publisherDaoJDBC).delete(eq(id));
        verify(response).setStatus(eq(HttpServletResponse.SC_NO_CONTENT));

    }
}
