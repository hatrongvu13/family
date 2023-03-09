package com.jax.core.authentication.dvo.responses;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class UserInfo {
    private String id;

    private String username;

    private String email;

    private List<String> scopes;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;
}
