package br.com.springsecurityjwt.framework.config.web.errors;

public class TagAlreadyExistsException extends RuntimeException{
    public TagAlreadyExistsException(String message){
        super(message);
    }
}
