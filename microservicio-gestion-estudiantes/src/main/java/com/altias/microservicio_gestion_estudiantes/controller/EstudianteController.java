package com.altias.microservicio_gestion_estudiantes.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.altias.microservicio_gestion_estudiantes.model.Estudiante;
import com.altias.microservicio_gestion_estudiantes.service.EstudianteService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/edutech/estudiantes")
@RequiredArgsConstructor
public class EstudianteController {

    private final EstudianteService estudianteService;

    // Health Check
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        String message = "✅ Microservicio de estudiantes funcionando correctamente!";
        log.info(message); // Log en consola
        return ResponseEntity.ok(message);
    }

    // 1. Buscar por email (con mensaje personalizado)
    @GetMapping("/buscar-por-email")
    public ResponseEntity<?> buscarPorEmail(@RequestParam String email) {
        log.info("🔍 Buscando estudiante con email: {}", email);
        return estudianteService.buscarPorEmail(email)
                .map(estudiante -> {
                    String msg = "🎯 Estudiante encontrado: " + estudiante.getNombreEstudiante();
                    log.info(msg);
                    return ResponseEntity.ok().body(msg + "\n" + estudiante);
                })
                .orElseGet(() -> {
                    String msg = "⚠️ No se encontró estudiante con email: " + email;
                    log.warn(msg);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
                });
    }

    // 2. Crear estudiante (con mensaje de éxito)
    @PostMapping
    public ResponseEntity<String> crearEstudiante(@Valid @RequestBody Estudiante estudiante) {
        Estudiante creado = estudianteService.guardarEstudiante(estudiante);
        String message = "✨ Estudiante creado exitosamente! ID: " + creado.getIdEstudiante();
        log.info(message);
        return ResponseEntity.status(HttpStatus.CREATED).body(message + "\n" + creado);
    }

    // 3. Actualizar estudiante (nuevo endpoint con mensaje)
    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarEstudiante(
            @PathVariable Long id,
            @Valid @RequestBody Estudiante estudiante) {
        estudiante.setIdEstudiante(id);
        Estudiante actualizado = estudianteService.guardarEstudiante(estudiante);
        String message = "🔄 Estudiante actualizado! ID: " + id;
        log.info(message);
        return ResponseEntity.ok(message + "\n" + actualizado);
    }

    // 4. Eliminar estudiante (con mensaje mejorado)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarEstudiante(@PathVariable Long id) {
        estudianteService.eliminarEstudiante(id);
        String message = "🗑️ Estudiante eliminado correctamente. ID: " + id;
        log.info(message);
        return ResponseEntity.ok(message);
    }
}