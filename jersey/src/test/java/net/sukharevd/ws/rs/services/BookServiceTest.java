package net.sukharevd.ws.rs.services;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;

import javax.ws.rs.core.MediaType;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import net.sukharevd.ws.rs.entities.Book;

import com.google.common.io.CharStreams;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.container.grizzly2.GrizzlyServerFactory;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.BodyPart;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;

public class BookServiceTest {

    private static final int NOT_FOUND_STATUS_CODE = 404;
    private static final int CREATED_STATUS_CODE = 201;
    private static final int OK_STATUS_CODE = 200;
    private static final String CONTENT_OF_FILE = "Content of file";
    private static final String SERVICE_URI = "http://localhost:9996";
    private static HttpServer httpServer;
    private ClientConfig clientConfig = new DefaultClientConfig();
    private Client client = Client.create(clientConfig);
    private WebResource webResource = client.resource(SERVICE_URI);
    
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        ResourceConfig rc = new PackagesResourceConfig("net.sukharevd.ws.rs");
        httpServer = GrizzlyServerFactory.createHttpServer(SERVICE_URI, rc);
    }

    @AfterClass
    public static void tearDownAfterClass() {
        httpServer.stop();
    }

    @Test
    public void getBookViaJson() {
        ClientResponse response = webResource.path("books").path("9780321356680")
            .accept(MediaType.APPLICATION_JSON_TYPE).get(ClientResponse.class);
        assertEquals(OK_STATUS_CODE, response.getStatus());
        Book book = response.getEntity(Book.class);
        assertEquals("Effective Java™, Second Edition", book.getTitle());
    }
    @Test
    public void getBookViaXml() {
        ClientResponse response = webResource.path("books").path("9780321356680")
        .accept(MediaType.APPLICATION_XML_TYPE).get(ClientResponse.class);
        assertEquals(OK_STATUS_CODE, response.getStatus());
        Book book = response.getEntity(Book.class);
        assertEquals("Effective Java™, Second Edition", book.getTitle());
    }

    @Test
    public void getAllBooksViaJson() {
        ClientResponse response = webResource.path("books").accept(MediaType.APPLICATION_JSON_TYPE).get(ClientResponse.class);
        assertEquals(OK_STATUS_CODE, response.getStatus());
        Books booksWrapper = response.getEntity(Books.class);
        ArrayList<Book> books = booksWrapper.getBook();
        assertEquals(BookServiceImpl.index.size(), books.size());
    }
    
    @Test
    public void getAllBooksViaXml() {
        ClientResponse response = webResource.path("books").accept(MediaType.APPLICATION_XML_TYPE).get(ClientResponse.class);
        assertEquals(OK_STATUS_CODE, response.getStatus());
        Books booksWrapper = response.getEntity(Books.class);
        ArrayList<Book> books = booksWrapper.getBook();
        assertEquals(BookServiceImpl.index.size(), books.size());
    }

    @Test
    public void createBookViaJson() {
        Book newbie = new Book("011", "New book", new String[] {"Smith, J."});
        ClientResponse response = webResource.path("books")
            .type(MediaType.APPLICATION_JSON).post(ClientResponse.class, newbie);
        assertEquals(CREATED_STATUS_CODE, response.getStatus());
    }
    
    @Test
    public void createBookViaXml() {
        Book newbie = new Book("011", "New book", new String[] {"Smith, J."});
        ClientResponse response = webResource.path("books")
        .type(MediaType.APPLICATION_XML).post(ClientResponse.class, newbie);
        assertEquals(CREATED_STATUS_CODE, response.getStatus());
    }

    @Test
    public void updateBookViaJson() {
        createBookViaJson();
        Book book = new Book("011", "VERY New book", new String[] {"Smith, J."});
        ClientResponse response = webResource.path("books")
            .path(book.getId()).type(MediaType.APPLICATION_JSON)
            .put(ClientResponse.class, book);
        assertEquals(OK_STATUS_CODE, response.getStatus());
    }
    
    @Test
    public void updateBookViaXml() {
        createBookViaXml();
        Book book = new Book("011", "VERY New book", new String[] {"Smith, J."});
        ClientResponse response = webResource.path("books")
        .path(book.getId()).type(MediaType.APPLICATION_XML)
        .put(ClientResponse.class, book);
        assertEquals(OK_STATUS_CODE, response.getStatus());
    }
    
    @Test
    public void deleteBook() {
        createBookViaJson();
        ClientResponse response = webResource.path("books").path("011").delete(ClientResponse.class);
        assertEquals(OK_STATUS_CODE, response.getStatus());
        response = webResource.path("books").path("011").delete(ClientResponse.class);
        assertEquals(NOT_FOUND_STATUS_CODE, response.getStatus());
    }

    @Test
    public void download() throws IOException, IllegalArgumentException, ParseException {
        upload();
        InputStream in = webResource.path("books").path("download").path("file.png").get(InputStream.class);
        String expected = CONTENT_OF_FILE;
        String actual = CharStreams.toString(new InputStreamReader(in));
        in.close();
        assertEquals(expected, actual);
    }

    @Test
    public void upload() throws IllegalArgumentException, ParseException {
        InputStream stream = new ByteArrayInputStream(CONTENT_OF_FILE.getBytes());
        FormDataMultiPart form = new FormDataMultiPart();
        FormDataContentDisposition contentDisposition = new FormDataContentDisposition("form-data; name=\"file\"; filename=\"file.png\"");
        BodyPart part = new FormDataBodyPart("file", stream, MediaType.valueOf("image/png"));
        part.setContentDisposition(contentDisposition);
        form.bodyPart(part);
        
        ClientResponse response = webResource.path("books").path("upload")
        .type(MediaType.MULTIPART_FORM_DATA_TYPE).post(ClientResponse.class, form);
        assertEquals(OK_STATUS_CODE, response.getStatus());
    }

}
