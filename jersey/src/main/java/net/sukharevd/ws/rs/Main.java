package net.sukharevd.ws.rs;

import java.io.IOException;

import org.glassfish.grizzly.http.server.HttpServer;

import com.sun.jersey.api.container.grizzly2.GrizzlyServerFactory;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;

/** Entry point that launches Grizzly web-server and deploys books-related web-service to it. */
public class Main {

    public static final String BASE_URI = "http://localhost:9998";

    public static void main(String[] args) throws IOException {
        ResourceConfig rc = new PackagesResourceConfig("net.sukharevd.ws.rs");
        rc.getContainerResponseFilters().add(new JsonpResponseFilter());
        HttpServer httpServer = GrizzlyServerFactory.createHttpServer(BASE_URI, rc);

        System.out.println(String.format("Jersey app started with WADL available at "
                + "%s/books/\nHit enter to stop it...", BASE_URI));
        System.in.read();
        httpServer.stop();
    }
}
