package aiss.bitbucketminer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 404 errors...
    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<String> handleHttpClientError(HttpClientErrorException ex) {
        return ResponseEntity
                .status(ex.getStatusCode())
                .body("HTTP Error: " + ex.getStatusCode() + " - " + ex.getStatusText());
    }

    // Timeout errors...
    @ExceptionHandler(RestClientException.class)
    public ResponseEntity<String> handleRestClientException(RestClientException ex) {
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("External service error: " + ex.getMessage());
    }

    // Anothers errors...
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Unexpected error: " + ex.getMessage());
    }
}