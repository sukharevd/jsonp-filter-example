package net.sukharevd.gwt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

public class GwtRestClient implements EntryPoint {

//  private final Messages messages = GWT.create(Messages.class);

  public void onModuleLoad() {
      RootPanel.get("book-list").add(new BookList());
  }
}
