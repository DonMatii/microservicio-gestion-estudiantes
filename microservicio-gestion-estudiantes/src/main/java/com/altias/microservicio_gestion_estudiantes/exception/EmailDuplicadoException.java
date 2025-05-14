package com.altias.microservicio_gestion_estudiantes.exception;

public class EmailDuplicadoException extends RuntimeException {
    public EmailDuplicadoException(String email) {
        super("El email '" + email + "' ya está registrado"); // Mensaje claro
    }
}