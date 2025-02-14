package com.example.tech_store.exception;

import com.example.tech_store.DTO.response.ErrorResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.ConstraintViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ErrorResponse createErrorResponse(Exception e, WebRequest request, int status, String error, String message) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(new Date());
        errorResponse.setStatus(status);
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));
        errorResponse.setError(error);
        errorResponse.setMessage(message);
        return errorResponse;
    }

    private String extractMessageFromException(Exception e) {
        if (e instanceof MethodArgumentNotValidException) {
            String message = e.getMessage();
            int start = message.lastIndexOf("[") + 1;
            int end = message.lastIndexOf("]") - 1;
            return message.substring(start, end);
        } else {
            return e.getMessage();
        }
    }

    @ExceptionHandler({ConstraintViolationException.class,
            MissingServletRequestParameterException.class, MethodArgumentNotValidException.class})
    @ResponseStatus(BAD_REQUEST)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = {@Content(mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(name = "Handle exception when the data invalid", value = """
                                    {
                                         "timestamp": "2024-04-07T11:38:56.368+00:00",
                                         "status": 400,
                                         "path": "/api/v1/...",
                                         "error": "Invalid Payload",
                                         "message": "{data} must be not blank"
                                     }
                                    """
                            ))})
    })
    public ErrorResponse handleValidationException(Exception e, WebRequest request) {
        String message = extractMessageFromException(e);
        String error = e instanceof MethodArgumentNotValidException ? "Invalid Payload" : "Invalid Parameter";
        return createErrorResponse(e, request, BAD_REQUEST.value(), error, message);
    }

    @ExceptionHandler({UnauthorizedException.class, AuthenticationException.class})
    @ResponseStatus(UNAUTHORIZED)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = {@Content(mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(name = "401 Response", value = """
                            {
                              "timestamp": "2023-10-19T06:07:35.321+00:00",\s
                              "status": 401,
                              "path": "/api/v1/...",
                              "error": "Unauthorized",
                              "message": "Invalid or expired token"
                            }
                           \s"""
                            ))})
    })
    public ErrorResponse handleUnauthorizedException(Exception e, WebRequest request) {
        return createErrorResponse(e, request, UNAUTHORIZED.value(),
                UNAUTHORIZED.getReasonPhrase(), e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(FORBIDDEN)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = {@Content(mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(name = "403 Response", value = """
                            {
                              "timestamp": "2023-10-19T06:07:35.321+00:00",
                              "status": 403,\s
                              "path": "/api/v1/...",
                              "error": "Forbidden",
                              "message": "Access Denied: You don't have permission to access this resource"
                            }
                           \s"""
                            ))})
    })
    public ErrorResponse handleAccessDeniedException(AccessDeniedException e, WebRequest request) {
        return createErrorResponse(e, request, FORBIDDEN.value(),
                FORBIDDEN.getReasonPhrase(), e.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = {@Content(mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(name = "404 Response", value = """
                                    {
                                      "timestamp": "2023-10-19T06:07:35.321+00:00",
                                      "status": 404,
                                      "path": "/api/v1/...",
                                      "error": "Not Found",
                                      "message": "{data} not found"
                                    }
                                    """
                            ))})
    })
    public ErrorResponse handleResourceNotFoundException(ResourceNotFoundException e, WebRequest request) {
        return createErrorResponse(e, request, NOT_FOUND.value(), NOT_FOUND.getReasonPhrase(), e.getMessage());
    }

    @ExceptionHandler(InvalidDataException.class)
    @ResponseStatus(CONFLICT)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "409", description = "Conflict",
                    content = {@Content(mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(name = "409 Response", value = """
                                    {
                                      "timestamp": "2023-10-19T06:07:35.321+00:00",
                                      "status": 409,
                                      "path": "/api/v1/...",
                                      "error": "Conflict",
                                      "message": "{data} exists, Please try again!"
                                    }
                                    """
                            ))})
    })
    public ErrorResponse handleDuplicateKeyException(InvalidDataException e, WebRequest request) {
        return createErrorResponse(e, request, CONFLICT.value(), CONFLICT.getReasonPhrase(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = {@Content(mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(name = "500 Response", value = """
                                    {
                                      "timestamp": "2023-10-19T06:35:52.333+00:00",
                                      "status": 500,
                                      "path": "/api/v1/...",
                                      "error": "Internal Server Error",
                                      "message": "Connection timeout, please try again"
                                    }
                                    """
                            ))})
    })
    public ErrorResponse handleException(Exception e, WebRequest request) {
        return createErrorResponse(e, request, INTERNAL_SERVER_ERROR.value(), INTERNAL_SERVER_ERROR.getReasonPhrase(), e.getMessage());
    }
}
