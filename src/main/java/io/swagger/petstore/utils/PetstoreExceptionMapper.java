/**
* Cloned and extended from {@link io.swagger.oas.inflector.utils.DefaultExceptionMapper} in 
* order to customize error response.
*/

package io.swagger.petstore.utils;

import io.swagger.oas.inflector.processors.EntityProcessor;
import io.swagger.oas.inflector.processors.EntityProcessorFactory;
import io.swagger.oas.inflector.utils.ApiException;
import io.swagger.oas.inflector.utils.ContentTypeSelector;
import io.swagger.oas.inflector.utils.DefaultExceptionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Providers;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class PetstoreExceptionMapper implements ExceptionMapper<Exception> {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultExceptionMapper.class);

    @Context
    Providers providers;

    @Context
    private HttpHeaders headers;

    public PetstoreExceptionMapper() {
        System.out.println("HELLO THERE!");
    }

    public Response toResponse(Exception exception) {
        final ApiError error = createError(exception);
        final int code = error.getCode();
        if (code != Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(error.getMessage(), exception);
            }
        } else {
            LOGGER.error(error.getMessage(), exception);
        }

        final Response.ResponseBuilder builder = Response.status(code).entity(error);

        MediaType responseMediaType = null;
        List<EntityProcessor> processors = EntityProcessorFactory.getProcessors();
        for (EntityProcessor processor : processors) {
            if(responseMediaType != null) {
                break;
            }
            for (MediaType mt : headers.getAcceptableMediaTypes()) {
                LOGGER.debug("checking type " + mt.toString() + " against " + processor.getClass().getName());
                if (processor.supports(mt)) {
                    builder.type(mt);
                    responseMediaType = mt;
                    break;
                }
            }
        }

        if(responseMediaType == null) {
            // no match based on Accept header, use first processor in list
            for (EntityProcessor processor : processors) {
                List<MediaType> supportedTypes = processor.getSupportedMediaTypes();
                if (supportedTypes.size() > 0) {
                    MediaType mt = supportedTypes.get(0);
                    builder.type(mt);
                    responseMediaType = mt;
                    break;
                }
            }
        }

        if(responseMediaType == null) {
            responseMediaType = MediaType.WILDCARD_TYPE;
        }

        final ContextResolver<ContentTypeSelector> selector = providers.getContextResolver(
                ContentTypeSelector.class, responseMediaType);
        if (selector != null) {
            selector.getContext(getClass()).apply(headers.getAcceptableMediaTypes(), builder);
        }
        return builder.build();
    }

    private ApiError createError(Exception exception) {
        if (exception instanceof ApiException) {
            final io.swagger.oas.inflector.models.ApiError sourceError = ((ApiException) exception).getError();
            return new ApiError(exception).code(sourceError.getCode()).message(sourceError.getMessage());
        } else if (exception instanceof WebApplicationException) {
            final WebApplicationException e = (WebApplicationException) exception;
            return new ApiError(exception).code(e.getResponse().getStatus()).message(e.getMessage());
        } else {
            final String message = String.format("There was an error processing your request."
                    + " It has been logged (ID: %016x)", ThreadLocalRandom.current().nextLong());
            return new ApiError(exception).code(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())
                    .message(message);
        }
    }
}
