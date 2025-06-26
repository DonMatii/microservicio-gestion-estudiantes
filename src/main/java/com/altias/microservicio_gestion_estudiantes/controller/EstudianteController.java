package com.altias.microservicio_gestion_estudiantes.controller;

import com.altias.microservicio_gestion_estudiantes.model.Estudiante;
import com.altias.microservicio_gestion_estudiantes.service.EstudianteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Slf4j
@RestController
@RequestMapping("/edutech/estudiantes")
@RequiredArgsConstructor
public class EstudianteController {

    private final EstudianteService estudianteService;

    @Operation(summary = "Verifica el estado del microservicio")
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        String message = "El Microservicio de estudiantes está funcionando correctamente! ✅";
        log.info(message);
        return ResponseEntity.ok(message);
    }

    @Operation(summary = "Busca un estudiante por su email", description = "Retorna un estudiante con enlaces HATEOAS si se encuentra registrado")
    @GetMapping("/buscar-por-email")
    public ResponseEntity<?> buscarPorEmail(
            @Parameter(description = "Correo electrónico del estudiante que se quiere buscar")
            @RequestParam String email) {

        log.info("🔍 Buscando estudiante con email: {}", email);
        Optional<Estudiante> estudianteOpt = estudianteService.buscarPorEmail(email);

        if (estudianteOpt.isEmpty()) {
            String msg = "⚠️ No se encontró estudiante con email: " + email;
            log.warn(msg);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
        }

        Estudiante estudiante = estudianteOpt.get();
        EntityModel<Estudiante> recurso = EntityModel.of(estudiante);

        recurso.add(linkTo(methodOn(EstudianteController.class)
                .buscarPorEmail(email)).withSelfRel());

        recurso.add(linkTo(methodOn(EstudianteController.class)
                .actualizarEstudiante(estudiante.getIdEstudiante(), estudiante)).withRel("actualizar"));

        recurso.add(linkTo(methodOn(EstudianteController.class)
                .eliminarEstudiante(estudiante.getIdEstudiante())).withRel("eliminar"));

        return ResponseEntity.ok(recurso);
    }

    @Operation(summary = "Obtiene todos los estudiantes registrados", description = "Devuelve una lista HATEOAS con todos los estudiantes del sistema")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Estudiante>>> obtenerTodos() {
        List<Estudiante> estudiantes = estudianteService.obtenerTodosLosEstudiantes();

        List<EntityModel<Estudiante>> estudiantesConLinks = estudiantes.stream().map(est -> {
            EntityModel<Estudiante> model = EntityModel.of(est);
            model.add(linkTo(methodOn(EstudianteController.class)
                    .buscarPorEmail(est.getEmailEstudiante())).withRel("self"));
            model.add(linkTo(methodOn(EstudianteController.class)
                    .actualizarEstudiante(est.getIdEstudiante(), est)).withRel("actualizar"));
            model.add(linkTo(methodOn(EstudianteController.class)
                    .eliminarEstudiante(est.getIdEstudiante())).withRel("eliminar"));
            return model;
        }).collect(Collectors.toList());

        CollectionModel<EntityModel<Estudiante>> collection =
                CollectionModel.of(estudiantesConLinks);
        collection.add(linkTo(methodOn(EstudianteController.class).obtenerTodos()).withSelfRel());

        return ResponseEntity.ok(collection);
    }

    @Operation(summary = "Crea un nuevo estudiante")
    @PostMapping
    public ResponseEntity<String> crearEstudiante(
            @Valid @RequestBody Estudiante estudiante) {

        Estudiante creado = estudianteService.guardarEstudiante(estudiante);
        String message = "Estudiante creado exitosamente! ID: " + creado.getIdEstudiante();
        log.info(message);
        return ResponseEntity.status(HttpStatus.CREATED).body(message + "\n" + creado);
    }

    @Operation(summary = "Actualiza los datos de un estudiante existente")
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarEstudiante(
            @Parameter(description = "ID del estudiante que se desea actualizar")
            @PathVariable Long id,
            @Valid @RequestBody Estudiante estudiante) {

        estudiante.setIdEstudiante(id);
        Estudiante actualizado = estudianteService.guardarEstudiante(estudiante);
        log.info("🔄 Estudiante actualizado! ID: {}", id);

        EntityModel<Estudiante> recurso = EntityModel.of(actualizado);

        recurso.add(linkTo(methodOn(EstudianteController.class)
                .buscarPorEmail(actualizado.getEmailEstudiante())).withRel("self"));
        recurso.add(linkTo(methodOn(EstudianteController.class)
                .actualizarEstudiante(id, actualizado)).withRel("actualizar"));
        recurso.add(linkTo(methodOn(EstudianteController.class)
                .eliminarEstudiante(id)).withRel("eliminar"));

        return ResponseEntity.ok(recurso);
    }

    @Operation(summary = "Elimina un estudiante por su ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarEstudiante(
            @Parameter(description = "ID del estudiante que se desea eliminar")
            @PathVariable Long id) {

        estudianteService.eliminarEstudiante(id);
        String message = "🗑️ Estudiante eliminado correctamente. ID: " + id;
        log.info(message);
        return ResponseEntity.ok(message);
    }
}