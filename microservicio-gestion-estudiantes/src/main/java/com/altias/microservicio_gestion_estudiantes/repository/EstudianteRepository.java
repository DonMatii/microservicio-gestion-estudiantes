package com.altias.microservicio_gestion_estudiantes.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.altias.microservicio_gestion_estudiantes.model.Estudiante;

@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {

    // Metodo para buscar por email (exacto)
    Optional<Estudiante> findByEmailEstudiante(String email);

    // Metodo para buscar por nombre (exacto)
    List<Estudiante> findByNombreEstudiante(String nombre);

    // Metodo para buscar por nombre (ignorando mayúsculas/minúsculas)
    List<Estudiante> findByNombreEstudianteIgnoreCase(String nombre);

    // Metodo para buscar por nombre, pero de manera parcial (ej: "Jua" encuentra "Juan")
    List<Estudiante> findByNombreEstudianteContaining(String keyword);

    // Metodo para buscar por nombre Y apellido paterno
    List<Estudiante> findByNombreEstudianteAndApellidoPaternoEstudiante(String nombre, String apellidoPaterno);

    
}