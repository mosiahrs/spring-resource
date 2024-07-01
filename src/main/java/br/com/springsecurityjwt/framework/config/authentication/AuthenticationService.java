package br.com.springsecurityjwt.framework.config.authentication;

import br.com.springsecurityjwt.application.domain.authentication.Token;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private JwtService jwtService;

    public AuthenticationService(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    public Token authenticate(Authentication authentication) {
        return new Token(jwtService.generateToken(authentication));
    }

}
