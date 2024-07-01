package br.com.springsecurityjwt.framework.config.web.errors;

public class InvalidTagInputException extends RuntimeException {
    public InvalidTagInputException(String message) {
        super(message);
    }
}
