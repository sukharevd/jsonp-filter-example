package net.sukharevd.gwt.client.entity;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

public class Books extends JavaScriptObject {

    protected Books() {}

    public final native JsArray<Book> getBooks() /*-{
        return this.book;
    }-*/;

}
