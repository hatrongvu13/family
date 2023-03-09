package com.jax.core.authentication.service;

import com.jax.core.authentication.dvo.UserPrincipal;
import com.jax.core.authentication.dvo.request.LoginRequest;
import com.jax.core.authentication.dvo.request.RegisterRequest;
import com.jax.core.authentication.dvo.responses.AuthenticationResponse;
import com.jax.core.authentication.dvo.responses.UserInfo;

public interface AuthenticationService {
    AuthenticationResponse login(LoginRequest loginRequest);

    AuthenticationResponse register(RegisterRequest request);

    UserInfo info(UserPrincipal currentUser);
}
