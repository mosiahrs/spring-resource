package br.com.springsecurityjwt.framework.config.web.exceptionhandler;

import br.com.springsecurityjwt.framework.config.web.errors.InvalidTagInputException;
import br.com.springsecurityjwt.framework.config.web.errors.ResourceAlreadyExistsException;
import br.com.springsecurityjwt.framework.config.web.errors.ResourceNotFoundException;
import br.com.springsecurityjwt.framework.config.web.errors.TagAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerComponent {

    private static final Logger log = LoggerFactory.getLogger(ExceptionHandlerComponent.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        var statusHTTP = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(statusHTTP).body(new ErrorResponse(statusHTTP.value(),"An error has occurred"));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(ResourceNotFoundException ex) {
        var statusHTTP = HttpStatus.NOT_FOUND;
        return ResponseEntity.status(statusHTTP).body(new ErrorResponse(statusHTTP.value(), ex.getMessage()));
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleResourceAlreadyExistsException(ResourceAlreadyExistsException ex) {
        log.warn(ex.getMessage());
        var statusHTTP = HttpStatus.CONFLICT;
        return ResponseEntity.status(statusHTTP).body(new ErrorResponse(statusHTTP.value(), ex.getMessage()));
    }

    @ExceptionHandler(TagAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleTagAlreadyExistsException(TagAlreadyExistsException ex) {
        log.warn(ex.getMessage());
        var statusHTTP = HttpStatus.CONFLICT;
        return ResponseEntity.status(statusHTTP).body(new ErrorResponse(statusHTTP.value(), ex.getMessage()));
    }

    @ExceptionHandler(InvalidTagInputException.class)
    public ResponseEntity<ErrorResponse> handleInvalidTagInputException(InvalidTagInputException ex) {
        log.warn(ex.getMessage());
        var statusHTTP = HttpStatus.UNPROCESSABLE_ENTITY;
        return ResponseEntity.status(statusHTTP).body(new ErrorResponse(statusHTTP.value(), ex.getMessage()));
    }

}