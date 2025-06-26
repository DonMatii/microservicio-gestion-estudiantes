package com.altias.microservicio_gestion_estudiantes.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.altias.microservicio_gestion_estudiantes.exception.EmailDuplicadoException;
import com.altias.microservicio_gestion_estudiantes.exception.EstudianteNoEncontradoException;
import com.altias.microservicio_gestion_estudiantes.model.Estudiante;
import com.altias.microservicio_gestion_estudiantes.repository.EstudianteRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EstudianteService {

    private final EstudianteRepository estudianteRepository;

    // 1. Buscar por email (retorna Optional<Estudiante>)
    public Optional<Estudiante> buscarPorEmail(String email) {
        return estudianteRepository.findByEmailEstudiante(email);
    }

    // 2. Buscar por email (lanza excepción personalizada si no existe)
    public Estudiante buscarPorEmailOrThrow(String email) {
        return estudianteRepository.findByEmailEstudiante(email)
                .orElseThrow(() -> new EstudianteNoEncontradoException("Estudiante con email '" + email + "' no encontrado"));
    }

    // 3. Buscar por ID (con excepción personalizada)
    public Estudiante buscarPorIdOrThrow(Long id) {
        return estudianteRepository.findById(id)
                .orElseThrow(() -> new EstudianteNoEncontradoException(id));
    }

    // 4. Buscar por nombre exacto (retorna List<Estudiante>)
    public List<Estudiante> buscarPorNombreExacto(String nombre) {
        return estudianteRepository.findByNombreEstudiante(nombre);
    }

    // 5. Buscar por nombre (ignorando mayúsculas/minúsculas)
    public List<Estudiante> buscarPorNombreIgnoreCase(String nombre) {
        return estudianteRepository.findByNombreEstudianteIgnoreCase(nombre);
    }

    // 6. Búsqueda parcial por nombre
    public List<Estudiante> buscarPorNombreParcial(String keyword) {
        return estudianteRepository.findByNombreEstudianteContaining(keyword);
    }

    // 7. Buscar por nombre y apellido paterno
    public List<Estudiante> buscarPorNombreYApellido(String nombre, String apellidoPaterno) {
        return estudianteRepository.findByNombreEstudianteAndApellidoPaternoEstudiante(nombre, apellidoPaterno);
    }

    // 8. Guardar un estudiante (con validación de email único)
    public Estudiante guardarEstudiante(Estudiante estudiante) {
        validarEmailUnico(estudiante.getEmailEstudiante(), estudiante.getIdEstudiante());
        return estudianteRepository.save(estudiante);
    }

    // 9. Eliminar un estudiante por ID (con excepción personalizada)
    public void eliminarEstudiante(Long id) {
        if (!estudianteRepository.existsById(id)) {
            throw new EstudianteNoEncontradoException(id);
        }
        estudianteRepository.deleteById(id);
    }

    // 10. Validar email único (con excepción personalizada)
public void validarEmailUnico(String email, Long idIgnorar) {
    estudianteRepository.findByEmailEstudiante(email)
        .ifPresent(estudiante -> {
            if (!estudiante.getIdEstudiante().equals(idIgnorar)) {
                throw new EmailDuplicadoException(email); // 👈 Usa la nueva excepción
            }
        });
    }

    // 11. Obtener todos los estudiantes (para HATEOAS)
public List<Estudiante> obtenerTodosLosEstudiantes() {
    return estudianteRepository.findAll();
}
}