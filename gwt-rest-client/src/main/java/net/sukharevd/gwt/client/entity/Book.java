package net.sukharevd.gwt.client.entity;

import com.google.gwt.core.client.JavaScriptObject;

public class Book extends JavaScriptObject {

    protected Book() {
    }

    public final native String getId() /*-{
        return this.id;
    }-*/;

    public final native void setId(String id) /*-{
        this.id = id;
    }-*/;

    public final native String getTitle() /*-{
        return this.title;
    }-*/;

    public final native void setTitle(String title) /*-{
        this.title = title;
    }-*/;

    public final native Object getAuthors() /*-{
        return this.authors;
    }-*/;

    public final native void setAuthors(Object authors) /*-{
        this.authors = authors;
    }-*/;

    public final native String getAttachment() /*-{
        return this.attachment;
    }-*/;

    public final native void setAttachment(String attachment) /*-{
        this.attachment = attachment;
    }-*/;

}
