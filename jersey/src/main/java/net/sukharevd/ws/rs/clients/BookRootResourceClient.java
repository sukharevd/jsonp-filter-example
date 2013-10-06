package net.sukharevd.ws.rs.clients;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;

import net.sukharevd.ws.rs.entities.Book;

import com.google.common.io.CharStreams;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.multipart.BodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;

public class BookRootResourceClient {

    private static final String SERVICE_URI = "http://localhost:9998";

    ClientConfig clientConfig = new DefaultClientConfig();
    Client client = Client.create(clientConfig);
    WebResource webResource = client.resource(getBaseURI());
    
    private static URI getBaseURI() {
        return UriBuilder.fromUri(SERVICE_URI).build();
    }
    
    public void getAllViaJson() {
        ClientResponse response = webResource.path("books").accept(MediaType.APPLICATION_XML_TYPE).get(ClientResponse.class);
        final String jsonString = response.getEntity(String.class);
        
//        JAXBContext jc = JAXBContext.newInstance(Books.class);
//        Unmarshaller u = jc.createUnmarshaller();
//        Books books = (Books) u.unmarshal(new ByteArrayInputStream(jsonString.getBytes()));
//        System.out.println(books.getBook());
        
        System.out.println(jsonString);
    }
    
    public void getAllViaXml() throws JAXBException {
        ClientResponse response = webResource.path("books").accept(MediaType.APPLICATION_XML_TYPE).get(ClientResponse.class);
        final String jsonString = response.getEntity(String.class);
        
        JAXBContext jc = JAXBContext.newInstance(Books.class);
        Unmarshaller u = jc.createUnmarshaller();
        Books books = (Books) u.unmarshal(new ByteArrayInputStream(jsonString.getBytes()));
        System.out.println(books.getBook());
        
        System.out.println(jsonString);
    }
    
    public void get() {
        ClientResponse response = webResource.path("books")
            .path("9780321356680")
            .accept(MediaType.APPLICATION_JSON_TYPE)
            .get(ClientResponse.class);
        final int status = response.getStatus();
        final String jsonString = response.getEntity(String.class);
        System.out.println(status + ": " + jsonString);
    }
    
    @Deprecated public void addBook() {
        Book newbie = new Book("011", "New book", new String[] {"Smith, J."});
        // important: type
        ClientResponse response = webResource.path("books").type(MediaType.APPLICATION_JSON).put(ClientResponse.class, newbie);
        int status = response.getStatus();
        // 201 means created resource
        System.out.println("Add: " + status + response.getEntity(String.class));
    }
    
    public void createBook(Book newbie) {        
        ClientResponse response = webResource.path("books")
            .type(MediaType.APPLICATION_JSON)
            .post(ClientResponse.class, newbie);
        int status = response.getStatus();
        // 201 means created resource
        System.out.println("Create: " + status + response.getEntity(String.class));
    }
    
    public void updateBook() {
        Book book = new Book("011", "VERY New book", new String[] {"Smith, J."});
        ClientResponse response = webResource.path("books")
            .path(book.getId()).type(MediaType.APPLICATION_JSON)
            .put(ClientResponse.class, book);
        System.out.println("Update: " + response.getStatus());
    }
    
    public void deleteBook() {
        ClientResponse response = webResource.path("books").path("011").delete(ClientResponse.class);
        final String jsonString = response.getEntity(String.class);
        int status = response.getStatus();
        // 200 means OK, 404 means Resource Not Found
        System.out.print("Delete: " + status);
        System.out.println(jsonString);
    }
    
    public void upload() throws IllegalArgumentException {
        InputStream stream = new ByteArrayInputStream("Content of file".getBytes());
//        new FormDataContentDisposition(null).FormDataContentDispositionBuilder(null).fileName("file.png").build();
        
        BodyPart part = new FormDataMultiPart().field("file", stream, MediaType.valueOf("image/png"));//.contentDisposition(new ContentDisposition("form-data; name=\"file\"; filename=\"filename.png\""));
        ClientResponse response = webResource.path("books")
        .path("multipart_upload").type(MediaType.MULTIPART_FORM_DATA_TYPE).post(ClientResponse.class, part);
        int status = response.getStatus();
        System.out.println("Upload: " + status);
    }

    public void simpleUpload() {
        byte[] data = "Content of file".getBytes();
        ClientResponse response = webResource.path("books")
            .path("simple_upload").path("file.png").post(ClientResponse.class, data);
        int status = response.getStatus();
        System.out.println("Upload: " + status);
    }
    
    public void download() throws IOException {
        InputStream in = webResource.path("books")
            .path("download").path("file.png").get(InputStream.class);
        String content = CharStreams.toString(new InputStreamReader(in));
        System.out.println(content);
        in.close();
    }
    
    public static void main(String[] args) throws IOException, IllegalArgumentException, JAXBException {
      final BookRootResourceClient bookClient = new BookRootResourceClient();
      Book newbie = new Book("011", "New book", new String[] {"Smith, J."});
      bookClient.getAllViaJson();
      bookClient.getAllViaXml();
      bookClient.get();
      //bookClient.addBook();
      bookClient.createBook(newbie);
      bookClient.updateBook();
      bookClient.deleteBook();
      bookClient.upload();
      bookClient.simpleUpload();
      bookClient.download();
    }
    
}

@XmlRootElement
class Books {
    private ArrayList<Book> book;

    public ArrayList<Book> getBook() {
        return book;
    }

    public void setBook(ArrayList<Book> book) {
        this.book = book;
    }
}
