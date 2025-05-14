package com.altias.microservicio_gestion_estudiantes.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EstudianteNoEncontradoException.class)
    public ResponseEntity<String> handleEstudianteNoEncontrado(EstudianteNoEncontradoException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND) // Código HTTP 404
                .body(ex.getMessage()); // Mensaje de la excepción
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error interno: " + ex.getMessage());
    }
    
}