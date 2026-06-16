package com.kplian.msaccess.api.exception;

import com.kplian.msaccess.api.dto.response.ErrorResponseDTO;
import com.kplian.msaccess.api.service.I18nService;
import com.kplian.msaccess.domain.exception.BusinessException;
import com.kplian.msaccess.domain.exception.InfrastructureException;
import com.kplian.msaccess.domain.exception.SystemException;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.time.LocalDateTime;
import org.jboss.logging.Logger;

@Provider
@ApplicationScoped
public class GlobalExceptionHandler implements ExceptionMapper<Exception> {

    private static final Logger LOG = Logger.getLogger(GlobalExceptionHandler.class);

    @Inject
    I18nService i18nService;

    @Inject
    Tracer tracer;

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(Exception exception) {
        if (exception instanceof NotFoundException) {
            return buildResponse("HTTP_NOT_FOUND", "error.http.not_found", Response.Status.NOT_FOUND);
        }
        if (exception instanceof BusinessException businessException) {
            return buildBusinessResponse(businessException);
        }
        if (exception instanceof InfrastructureException infrastructureException) {
            recordTelemetry(infrastructureException, "infrastructure");
            return buildResponse(infrastructureException.getCode(), infrastructureException.getMessageKey(), Response.Status.INTERNAL_SERVER_ERROR);
        }
        if (exception instanceof SystemException systemException) {
            recordTelemetry(systemException, "system");
            return buildResponse(systemException.getCode(), systemException.getMessageKey(), Response.Status.INTERNAL_SERVER_ERROR);
        }
        if (exception instanceof ConstraintViolationException) {
            return buildResponse("VALIDATION_ERROR", "error.business.validation", Response.Status.BAD_REQUEST);
        }
        if (exception instanceof WebApplicationException webException) {
            Response.StatusType statusType = webException.getResponse().getStatusInfo();
            Response.Status status = statusType instanceof Response.Status
                ? (Response.Status) statusType
                : Response.Status.fromStatusCode(statusType.getStatusCode());
            if (status == Response.Status.BAD_REQUEST) {
                return buildResponse("HTTP_BAD_REQUEST", "error.http.bad_request", Response.Status.BAD_REQUEST);
            }
            if (status == Response.Status.NOT_FOUND) {
                return buildResponse("HTTP_NOT_FOUND", "error.http.not_found", Response.Status.NOT_FOUND);
            }
            return buildResponse("HTTP_ERROR", "error.http.internal_error", Response.Status.INTERNAL_SERVER_ERROR);
        }

        LOG.error("Unexpected error occurred: " + exception.getMessage(), exception);
        recordTelemetry(exception, "system");
        return buildResponse("UNEXPECTED_ERROR", "error.system.unexpected", Response.Status.INTERNAL_SERVER_ERROR);
    }

    private Response buildBusinessResponse(BusinessException exception) {
        Response.Status status = Response.Status.BAD_REQUEST;
        if (exception.getMessageKey() != null && exception.getMessageKey().contains("not_found")) {
            status = Response.Status.NOT_FOUND;
        }
        return buildResponse(exception.getCode(), exception.getMessageKey(), status, exception.getParams());
    }

    private Response buildResponse(String code, String messageKey, Response.Status status, Object... params) {
        String message = i18nService.get(messageKey == null ? "error.system.unexpected" : messageKey, params);
        ErrorResponseDTO dto = new ErrorResponseDTO(
            code == null ? "ERROR" : code,
            message,
            LocalDateTime.now(),
            uriInfo == null ? null : uriInfo.getPath()
        );
        return Response.status(status).entity(dto).build();
    }

    private void recordTelemetry(Exception exception, String errorType) {
        Span span = tracer.spanBuilder("exception").startSpan();
        try {
            span.setStatus(StatusCode.ERROR);
            span.setAttribute("error", true);
            span.setAttribute("error.type", errorType);
            span.setAttribute("error.class", exception.getClass().getName());
            span.setAttribute("error.message", exception.getMessage());
            span.recordException(exception);
        } finally {
            span.end();
        }
    }
}
