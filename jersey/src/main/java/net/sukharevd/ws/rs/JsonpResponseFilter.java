package net.sukharevd.ws.rs;

import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.sun.jersey.api.json.JSONWithPadding;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;

/**
 * Filter that optionally wraps a JSON response in a JSONP callback.
 * <p/>
 * For this wrapping to happen two things need to be true:
 * <ul>
 * <li>The Media type of the response must be set to JSON or application/javascript</li>
 * <li>The request must have a query parameter called {@code callback}</li>
 * </ul>
 * <p/>
 *
 * @see <a href="http://weblogs.java.net/blog/felipegaucho/archive/2010/02/25/jersey-feat-jquery-jsonp">JSONP with
 *      Jersey and jQuery</a>
 */
public class JsonpResponseFilter implements ContainerResponseFilter {

    @Override
    public ContainerResponse filter(ContainerRequest request, ContainerResponse response) {
        if (response.getMediaType() == null || !response.getMediaType().equals(MediaType.APPLICATION_JSON_TYPE)
                && !response.getMediaType().equals(new MediaType("application", "javascript"))
                && !response.getMediaType().equals(new MediaType("application", "x-javascript"))) {
            return response;
        }

        String callback = request.getQueryParameters().getFirst("callback");
        if (callback == null || callback.isEmpty()) {
            return response;
        }

        if (response.getEntity() != null && response.getEntity().getClass() != GenericEntity.class
                && response.getEntity().getClass() != JSONWithPadding.class) {
            @SuppressWarnings("unchecked")
            final GenericEntity<?> genericEntity = new GenericEntity(response.getEntity(), response.getEntityType());
            JSONWithPadding jsonp = new JSONWithPadding(genericEntity, callback);
            response.setEntity(jsonp);
            response.setResponse(Response.fromResponse(response.getResponse())
                    .type("application/javascript")
                    .entity(jsonp).build());
            return response;
        }

        return response;
    }
}
