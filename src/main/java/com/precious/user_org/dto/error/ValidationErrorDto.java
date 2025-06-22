package com.precious.user_org.dto.error;

import lombok.Data;

@Data
public class ValidationErrorDto {
    private final String field;

    private final String message;
}
