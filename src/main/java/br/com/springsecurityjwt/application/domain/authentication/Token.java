package br.com.springsecurityjwt.application.domain.authentication;

public class Token {

    private final String token;

    public Token(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
