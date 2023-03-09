package com.jax.core.authentication.rest;

import com.jax.core.authentication.dvo.UserPrincipal;
import com.jax.core.authentication.dvo.request.LoginRequest;
import com.jax.core.authentication.dvo.request.RegisterRequest;
import com.jax.core.authentication.dvo.responses.AuthenticationResponse;
import com.jax.core.authentication.dvo.responses.UserInfo;
import com.jax.core.authentication.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/authentication", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest) {
        return authenticationService.login(loginRequest);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public AuthenticationResponse register(@RequestBody RegisterRequest request) {
        return authenticationService.register(request);
    }

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public UserInfo info(@AuthenticationPrincipal UserPrincipal currentUser) {
        return authenticationService.info(currentUser);
    }
}
