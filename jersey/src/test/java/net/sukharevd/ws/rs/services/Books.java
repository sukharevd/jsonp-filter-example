package net.sukharevd.ws.rs.services;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

import net.sukharevd.ws.rs.entities.Book;

@XmlRootElement
/** Wrapper for the list of Book for marshaling and demarshaling via JAXB. */ 
class Books {
    private ArrayList<Book> book;

//    @XmlElement(name = "book")
    public ArrayList<Book> getBook() {
        return book;
    }

    public void setBook(ArrayList<Book> books) {
        this.book = books;
    }
}
