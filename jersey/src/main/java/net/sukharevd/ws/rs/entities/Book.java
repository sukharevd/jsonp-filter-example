package net.sukharevd.ws.rs.entities;

import java.util.Arrays;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
/** Class that represents book of books-related web-service. */
public class Book {

    /** ISBN */
    private String id;
    private String title;
    private String[] authors;
    private String attachment;

    public Book() {
        // empty constructor for JAXB
    }
    public Book(String id, String title, String[] authors) {
        this.id = id;
        this.title = title;
        this.authors = authors;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String[] getAuthors() {
        return authors;
    }
    public void setAuthors(String[] authors) {
        this.authors = authors;
    }
    public String getAttachment() {
        return attachment;
    }
    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Book [id=").append(id).append(", title=").append(title)
                .append(", authors=").append(Arrays.toString(authors)).append("]");
        return builder.toString();
    }

}
