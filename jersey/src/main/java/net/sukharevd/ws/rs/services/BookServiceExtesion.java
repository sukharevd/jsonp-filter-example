package net.sukharevd.ws.rs.services;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import net.sukharevd.ws.rs.entities.Book;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataParam;

public class BookServiceExtesion extends BookServiceImpl {

    private static final String ALLOWED_FILE_TYPE = "png";

    @GET
    @Path("/query")
    @Produces("application/json")
    public Book getBookQuery(@QueryParam("id") String id) {
        return index.get(id);
    }
    
    @GET
    @Path("/matrix")
    @Produces("application/json")
    public Book getBookMatrix(@MatrixParam("id") String id) {
        return index.get(id);
    }
    
    @POST
    @Path("/simple_post")
    public Response addBookFromForm(
            @FormParam("title") String title,
            @FormParam("author[]") List<String> authors,
            @FormParam("isbn") String id) {
        if (title == null || id == null || authors == null
                || title.isEmpty() || id.isEmpty() || authors.isEmpty()) {
            return Response.status(400).entity("title, author[] and isbn parameters are required.").build();
        }
        // TODO here empty authors should be removed
        String[] authorsArray = authors.toArray(new String[authors.size()]);
        Book newbie = new Book(id, title, authorsArray);
        System.out.println("title:" + title + "\nauthor[]:" + authors + "\nid:" + id);
        index.put(newbie.getId(), newbie);
        return Response.ok().build();
    }

    @POST
    @Path("/simple_upload/{filename}")
    public Response simpleUploadFile(
            @PathParam("filename") String filename,
            InputStream fileStream) throws IOException {
        if (filename == null || !filename.endsWith(ALLOWED_FILE_TYPE)) {
            return Response.status(415).entity("Wrong image format").build();
        }
        File file = new File(filename);
        try { 
            return uploadFile(fileStream, file);
        } catch (FileSizeLimitExceededException e) {
            file.delete();
            return Response.status(413).entity("Request Entity Too Large").build();
        }
    }
    
    
    @POST
    @Path("/multipart")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response addBookFromForm(
            @FormDataParam("title") String title,
            @FormDataParam("author[]") List<FormDataBodyPart> authors,
            @FormDataParam("isbn") String id,
            @FormDataParam("file") InputStream fileStream,
            @FormDataParam("file") FormDataContentDisposition fileDetail) throws IOException {
        if (title == null || id == null || authors == null
                || title.isEmpty() || id.isEmpty() || authors.isEmpty()) {
            throw new IllegalArgumentException();
        }
        String[] authorsArray = obtainArrayOfAuthors(authors);
        Book newbie = new Book(id, title, authorsArray);
        System.out.println("title:" + title + "\nauthor[]:" + authors + "\nid:" + id);
        File file = new File(fileDetail.getFileName());
        Response response = null;
        try { 
            response = uploadFile(fileStream, file);
        } catch (FileSizeLimitExceededException e) {
            file.delete();
            response = Response.status(413).entity("Request Entity Too Large").build();
        }
        if (response.getStatus() == 200) {
            newbie.setAttachment(file.getAbsolutePath());
        }
        index.put(newbie.getId(), newbie);
        return response;
    }

    private String[] obtainArrayOfAuthors(List<FormDataBodyPart> authors) {
        List<String> authorsList = new ArrayList<String>();
        for (FormDataBodyPart part : authors) {
            String in = part.getValueAs(String.class);
            if (in != null && !in.isEmpty()) {
                authorsList.add(in);
                System.out.println(in);
            }
        }
        return authorsList.toArray(new String[authorsList.size()]);
    }
}
