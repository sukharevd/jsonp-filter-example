package net.sukharevd.ws.rs.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import net.sukharevd.ws.rs.entities.Book;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

/** Interface of book-related web-service. */
public interface BookService {

    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, "application/x-javascript", "application/javascript" })
    /** Returns list of all books. */
    List<Book> getAllBooks();

    @POST
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, "application/x-javascript", "application/javascript" })
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    /** Adds book to the list of books. Returns code 201 if succeeds. */
    Response createBook(Book book);

    @GET
    @Path("/{id}")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, "application/x-javascript", "application/javascript" })
    /** Returns books with the specified identifier. */
    Book getBook(@PathParam("id") String id);

    @PUT
    @Path("/{id}")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, "application/x-javascript", "application/javascript" })
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    /** Modifies existing book. Returns code 200 if succeeds. */
    Response updateBook(@PathParam("id") String id, Book book);

    @DELETE
    @Path("/{id}")
    /** Removes book from the list of books. Returns code 200 if succeed.
     *  If there's no book with the specified identifier, returns code 404. */
    Response deleteBook(@PathParam("id") String id);

    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    /** Uploads file to web-service. Returns code 200 if succeed.
     *  If file format differs from required, returns code 415.
     *  If file size exceeds limit, returns code 413. */
    Response uploadFile(
            @FormDataParam("file") InputStream fileStream,
            @FormDataParam("file") FormDataContentDisposition fileDetail) throws IOException;


    @GET
    @Path("/download/{filename}")
    /** Downloads file from web-service. Returns code 200 if succeed. */
    public Response downloadFile(@PathParam("filename") String filename) throws IOException;
}
