package itu.auth.mg.controller;

import itu.auth.mg.args.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;


@ControllerAdvice
public class GlobalExceptionHandler {

    // Gestion des exceptions personnalisées
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<String>> handleBadRequest(IllegalArgumentException ex, WebRequest request) {
        return new ResponseEntity<>(
            new ApiResponse<>(false, ex.getMessage(), null), HttpStatus.BAD_REQUEST
        );
    }

    // Gestion des exceptions générales
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleGeneralError(Exception ex, WebRequest request) {
        return new ResponseEntity<>(
            new ApiResponse<>(false, "Une erreur interne est survenue.", null), HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
