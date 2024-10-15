package br.gov.agu.samir.new_samir_back.infra.exceptions;

import br.gov.agu.samir.new_samir_back.exceptions.ResourceAlreadyExistException;
import br.gov.agu.samir.new_samir_back.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(ResourceAlreadyExistException.class)
    public ResponseEntity<StandardError> resourceAlreadyException(ResourceAlreadyExistException exception, HttpServletRequest request){
        StandardError error = StandardError.builder()
                .error("Resource already exist")
                .path(request.getContextPath())
                .status(409)
                .message(exception.getMessage())
                .timestamp(Instant.now())
                .build();
        return ResponseEntity.status(error.getStatus()).body(error);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> resourceNotFoundException(ResourceNotFoundException exception, HttpServletRequest request){
        StandardError error = StandardError.builder()
                .error("Resource not found")
                .path(request.getContextPath())
                .status(404)
                .message(exception.getMessage())
                .timestamp(Instant.now())
                .build();
        return ResponseEntity.status(error.getStatus()).body(error);
    }
}
