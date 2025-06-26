package com.altias.microservicio_gestion_estudiantes.exception;

public class EstudianteNoEncontradoException extends RuntimeException {
    
    // Constructor para ID (mantén este si ya lo tenías)
    public EstudianteNoEncontradoException(Long id) {
        super("Estudiante con ID " + id + " no encontrado");
    }

    // Constructor nuevo para mensaje personalizado (String)
    public EstudianteNoEncontradoException(String message) {
        super(message);
    }
}