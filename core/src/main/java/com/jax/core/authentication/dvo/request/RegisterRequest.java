package com.jax.core.authentication.dvo.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotNull(message = "Username is not empty")
    private String username;
    @Email(message = "Valid email only")
    private String email;

    @NotNull(message = "Password is not empty")
    private String password;

    @NotNull(message = "Password is not empty")
    private String re_password;
}
