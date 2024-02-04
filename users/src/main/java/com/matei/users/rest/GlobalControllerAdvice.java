package com.matei.users.rest;

import com.matei.users.service.TextBoardException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalControllerAdvice {

    private static final String RESPONSE_BODY_KEY = "errors";

    private static final String UNEXPECTED_EXCEPTION = "Unexpected Exception";

    @ExceptionHandler(TextBoardException.class)
    public ResponseEntity<?> handleError(TextBoardException exception) {

        log.info("Managed Exception: {}", exception.getMessage(), exception);
        var responseBody = createResponseBody(exception.getMessage());

        return new ResponseEntity<>(responseBody, switch (exception.getType()) {
        case NOT_FOUND -> HttpStatus.NOT_FOUND;
        case BAD_REQUEST -> HttpStatus.BAD_REQUEST;
        case FORBIDDEN -> HttpStatus.FORBIDDEN;
        case CONFLICT -> HttpStatus.CONFLICT;
        case INTERNAL_SERVER_ERROR -> HttpStatus.INTERNAL_SERVER_ERROR;
        });
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleConflict(Exception exception) {

        log.error(UNEXPECTED_EXCEPTION, exception);
        return ResponseEntity.internalServerError().body(createResponseBody(UNEXPECTED_EXCEPTION));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> x.getField() + " " + x.getDefaultMessage())
                .toList();
        return new ResponseEntity<>(Map.of(RESPONSE_BODY_KEY, errors), HttpStatus.BAD_REQUEST);
    }

    private Map<String, List<String>> createResponseBody(String errorMessage) {
        return Map.of(RESPONSE_BODY_KEY, List.of(errorMessage));
    }

}
