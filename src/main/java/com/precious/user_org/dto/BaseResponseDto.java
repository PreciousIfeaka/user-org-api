package com.precious.user_org.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponseDto<T> {
    @NotBlank
    private String status;

    @NotBlank
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;
}
