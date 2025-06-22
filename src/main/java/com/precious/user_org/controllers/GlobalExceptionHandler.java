package com.precious.user_org.controllers;

import com.precious.user_org.dto.error.ExceptionResponseDto;
import com.precious.user_org.dto.error.ValidationErrorDto;
import com.precious.user_org.exceptions.BadRequestException;
import com.precious.user_org.exceptions.ForbiddenException;
import com.precious.user_org.exceptions.ResourceNotFoundException;
import com.precious.user_org.exceptions.UnauthorizedException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<HashMap<String, List<ValidationErrorDto>>> handleValidationException(
            MethodArgumentNotValidException e
    ) {
        List<ValidationErrorDto> errorList = e.getBindingResult().getFieldErrors().stream()
                .map(error -> new ValidationErrorDto(error.getField(), error.getDefaultMessage()))
                .toList();

        HashMap<String, List<ValidationErrorDto>> errorResponse = new HashMap<String, List<ValidationErrorDto>>();
        errorResponse.put("errors", errorList);

        return ResponseEntity.status(422).body(errorResponse);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<HashMap<String, List<ValidationErrorDto>>> handleConstraintViolation(
            ConstraintViolationException e
    ) {
        List<ValidationErrorDto> errorList = e.getConstraintViolations().stream()
                .map(error -> new ValidationErrorDto(error.getPropertyPath().toString(), error.getMessage()))
                .toList();

        HashMap<String, List<ValidationErrorDto>> errorResponse = new HashMap<String, List<ValidationErrorDto>>();
        errorResponse.put("errors", errorList);

        return ResponseEntity.status(422).body(errorResponse);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ExceptionResponseDto> handleUnauthorizedException(
            UnauthorizedException e
    ) {
        ExceptionResponseDto errorResponse = ExceptionResponseDto.builder()
                .status("Unauthorized")
                .message(e.getMessage())
                .statusCode(401)
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ExceptionResponseDto> handleBadRequestException(
            BadRequestException e
    ) {
        ExceptionResponseDto errorResponse = ExceptionResponseDto.builder()
                .message(e.getMessage())
                .status("Bad Request")
                .statusCode(400)
                .build();

        return  ResponseEntity.status(400).body(errorResponse);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ExceptionResponseDto> handleForbiddenException(
            ForbiddenException e
    ) {
        ExceptionResponseDto errorResponse = ExceptionResponseDto.builder()
                .message(e.getMessage())
                .status("Forbidden")
                .statusCode(403)
                .build();

        return  ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionResponseDto> handleNotFoundException(
            ResourceNotFoundException e
    ) {
        ExceptionResponseDto errorResponse = ExceptionResponseDto.builder()
                .message(e.getMessage())
                .status("Not found")
                .statusCode(404)
                .build();

        return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponseDto> handleGeneric(Exception e) {
        String message = e.getMessage().split("\\R", 2)[0];

        if (message.contains("duplicate key") && e.getMessage().contains("email")) {
            message = "Email already exists.";
        }

        System.out.println(e);

        ExceptionResponseDto errorResponse = ExceptionResponseDto.builder()
                .status("Internal Server Error")
                .message(message)
                .statusCode(500)
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
