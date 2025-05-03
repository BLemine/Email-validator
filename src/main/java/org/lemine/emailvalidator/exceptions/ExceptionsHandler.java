package org.lemine.emailvalidator.exceptions;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.lemine.emailvalidator.domain.responses.BaseResponse;
import org.lemine.emailvalidator.domain.responses.ValidationErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.lang.NonNull;             // <—

import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionsHandler extends ResponseEntityExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionsHandler.class);

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request
    ) {

        var fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fe -> fe.getDefaultMessage() != null ? fe.getDefaultMessage() : "Invalid value",
                        (m1, m2) -> m1 + "; " + m2
                ));

        var resp = new ValidationErrorResponse(
                "VALIDATION_FAILED",
                "One or more fields are invalid",
                fieldErrors
        );
        return ResponseEntity
                .status(status)
                .headers(headers)
                .body(resp);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ValidationErrorResponse> handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, String> violations = ex.getConstraintViolations().stream()
                .collect(Collectors.toMap(
                        v -> v.getPropertyPath().toString(),
                        ConstraintViolation::getMessage,
                        (m1, m2) -> m1 + "; " + m2
                ));

        ValidationErrorResponse resp = new ValidationErrorResponse(
                "VALIDATION_FAILED",
                "Request parameters failed validation",
                violations
        );
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(resp);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse> globalHandler(final Exception exception) {
        LOGGER.warn(exception.getMessage());
        return new ResponseEntity<>(new BaseResponse(
                "001",
                exception.getMessage()
        ), HttpStatus.BAD_REQUEST);
    }
}
