package com.jax.core.dvo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BaseDVO {
    private String delete;
    private String createdBy;
    private String modifiedBy;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
