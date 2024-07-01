package br.com.springsecurityjwt.framework.config.web;

import br.com.springsecurityjwt.application.domain.authentication.Token;
import br.com.springsecurityjwt.framework.config.authentication.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    private AuthenticationService authenticationService;

    public AuthenticationController(
            final AuthenticationService authenticationService
    ) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/authenticate")
    public Token authenticate(
            Authentication authentication) {
        return authenticationService.authenticate(authentication);
    }
}
