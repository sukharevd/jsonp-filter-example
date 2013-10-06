package net.sukharevd.gwt.client;

import com.github.gwtbootstrap.client.ui.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.jsonp.client.JsonpRequestBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import net.sukharevd.gwt.client.entity.Book;
import net.sukharevd.gwt.client.entity.Books;

import java.util.Arrays;

public class BookList extends Composite {

    @UiTemplate("BookList.ui.xml")
    interface BookListUiBinder extends UiBinder<FlowPanel, BookList> {}

    private static BookListUiBinder uiBinder = GWT.create(BookListUiBinder.class);

    @UiField Button loadBooksButton;
    @UiField FlowPanel bookListSpaceholder;

    public BookList() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @UiHandler("loadBooksButton")
    void loadBooksButtonClicked(@SuppressWarnings("unused") ClickEvent event) {
        JsonpRequestBuilder jsonp = new JsonpRequestBuilder();

        String url = "http://localhost:9998/books";

        jsonp.requestObject(url, new AsyncCallback<Books>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert("Failure:" + caught.getMessage());
            }

            @Override
            public void onSuccess(Books result) {
                if(result.getBooks() != null ) {
                    CellTable<Book> table = new CellTable<Book>();
                    TextColumn<Book> idColumn = new TextColumn<Book>() {
                        @Override
                        public String getValue(Book object) {
                            return object.getId();
                        }
                    };
                    table.addColumn(idColumn, "ID");

                    TextColumn<Book> titleColumn = new TextColumn<Book>() {
                        @Override
                        public String getValue(Book object) {
                            return object.getTitle();
                        }
                    };
                    table.addColumn(titleColumn, "Title");

                    TextColumn<Book> authorsColumn = new TextColumn<Book>() {
                        @Override
                        public String getValue(Book object) {
                            if (object.getAuthors() instanceof JsArrayString) {
                                StringBuilder sb = new StringBuilder();
                                JsArrayString authors = (JsArrayString) object.getAuthors();
                                sb.append(authors.get(0));
                                for (int i = 1; i < authors.length(); ++i)
                                    sb.append(", ").append(authors.get(i));
                                return sb.toString();
                            } else {
                                return object.getAuthors().toString();
                            }
                        }
                    };
                    table.addColumn(authorsColumn, "Authors");

                    TextColumn<Book> attachmentColumn = new TextColumn<Book>() {
                        @Override
                        public String getValue(Book object) {
                            return object.getAttachment() == null ? "None" : object.getAttachment();
                        }
                    };
                    table.addColumn(attachmentColumn, "Attachment");

                    Book[] books = new Book[result.getBooks().length()];
                    for (int i = 0; i < result.getBooks().length(); i++)
                        books[i] = result.getBooks().get(i);
                    table.setRowData(Arrays.asList(books));

                    bookListSpaceholder.clear();
                    bookListSpaceholder.add(table);
                }
            }
        });
    }
}