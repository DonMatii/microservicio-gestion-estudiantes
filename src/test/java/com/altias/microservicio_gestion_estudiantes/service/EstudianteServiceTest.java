package com.altias.microservicio_gestion_estudiantes.service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.altias.microservicio_gestion_estudiantes.exception.EmailDuplicadoException;
import com.altias.microservicio_gestion_estudiantes.exception.EstudianteNoEncontradoException;
import com.altias.microservicio_gestion_estudiantes.model.Estudiante;
import com.altias.microservicio_gestion_estudiantes.repository.EstudianteRepository;

@ExtendWith(MockitoExtension.class)
public class EstudianteServiceTest {

    @Mock
    private EstudianteRepository estudianteRepository;

    @InjectMocks
    private EstudianteService estudianteService;

    private Estudiante estudianteEjemplo;

    @BeforeEach
    void setUp() {
        estudianteEjemplo = new Estudiante();
        estudianteEjemplo.setIdEstudiante(1L);
        estudianteEjemplo.setNombreEstudiante("Ana");
        estudianteEjemplo.setApellidoPaternoEstudiante("Gómez");
        estudianteEjemplo.setEmailEstudiante("ana@example.com");
        estudianteEjemplo.setFechaNacimientoEstudiante(LocalDate.of(2000, 5, 15));
    }

    // 1. Test para buscarPorEmail (Optional)
    @Test
    void buscarPorEmail_CuandoExiste_DeberiaRetornarOptionalConEstudiante() {
        when(estudianteRepository.findByEmailEstudiante("ana@example.com"))
            .thenReturn(Optional.of(estudianteEjemplo));

        Optional<Estudiante> resultado = estudianteService.buscarPorEmail("ana@example.com");

        assertTrue(resultado.isPresent());
        assertEquals("Ana", resultado.get().getNombreEstudiante());
    }

    // 2. Test para buscarPorEmailOrThrow (con excepción)
    @Test
    void buscarPorEmailOrThrow_CuandoNoExiste_DeberiaLanzarExcepcion() {
        when(estudianteRepository.findByEmailEstudiante("noexiste@example.com"))
            .thenReturn(Optional.empty());

        assertThrows(EstudianteNoEncontradoException.class, () -> {
            estudianteService.buscarPorEmailOrThrow("noexiste@example.com");
        });
    }

    // 3. Test para buscarPorIdOrThrow
    @Test
    void buscarPorIdOrThrow_CuandoExiste_DeberiaRetornarEstudiante() {
        when(estudianteRepository.findById(1L)).thenReturn(Optional.of(estudianteEjemplo));

        Estudiante resultado = estudianteService.buscarPorIdOrThrow(1L);

        assertEquals("Ana", resultado.getNombreEstudiante());
    }

    // 4. Test para buscarPorNombreExacto
    @Test
    void buscarPorNombreExacto_DeberiaRetornarListaFiltrada() {
        List<Estudiante> listaMock = Arrays.asList(estudianteEjemplo);
        when(estudianteRepository.findByNombreEstudiante("Ana")).thenReturn(listaMock);

        List<Estudiante> resultado = estudianteService.buscarPorNombreExacto("Ana");

        assertEquals(1, resultado.size());
        assertEquals("Gómez", resultado.get(0).getApellidoPaternoEstudiante());
    }

    // 5. Test para guardarEstudiante (éxito)
    @Test
    void guardarEstudiante_ConEmailUnico_DeberiaGuardarCorrectamente() {
        when(estudianteRepository.findByEmailEstudiante("ana@example.com"))
            .thenReturn(Optional.empty());
        when(estudianteRepository.save(any(Estudiante.class))).thenReturn(estudianteEjemplo);

        Estudiante resultado = estudianteService.guardarEstudiante(estudianteEjemplo);

        assertNotNull(resultado);
        verify(estudianteRepository, times(1)).save(estudianteEjemplo);
    }

    // 6. Test para guardarEstudiante (email duplicado)
    @Test
    void guardarEstudiante_ConEmailDuplicado_DeberiaLanzarExcepcion() {
        Estudiante existente = new Estudiante();
        existente.setIdEstudiante(2L); // ID diferente al estudiante de prueba

        when(estudianteRepository.findByEmailEstudiante("ana@example.com"))
            .thenReturn(Optional.of(existente));

        assertThrows(EmailDuplicadoException.class, () -> {
            estudianteService.guardarEstudiante(estudianteEjemplo);
        });
    }

    // 7. Test para eliminarEstudiante (éxito)
    @Test
    void eliminarEstudiante_CuandoExiste_DeberiaEliminar() {
        when(estudianteRepository.existsById(1L)).thenReturn(true);
        doNothing().when(estudianteRepository).deleteById(1L);

        assertDoesNotThrow(() -> {
            estudianteService.eliminarEstudiante(1L);
        });

        verify(estudianteRepository, times(1)).deleteById(1L);
    }

    // 8. Test para eliminarEstudiante (no existe)
    @Test
    void eliminarEstudiante_CuandoNoExiste_DeberiaLanzarExcepcion() {
        when(estudianteRepository.existsById(99L)).thenReturn(false);

        assertThrows(EstudianteNoEncontradoException.class, () -> {
            estudianteService.eliminarEstudiante(99L);
        });
    }

    // 9. Test para validarEmailUnico (caso exitoso)
    @Test
    void validarEmailUnico_ConEmailNuevo_NoDeberiaLanzarExcepcion() {
        when(estudianteRepository.findByEmailEstudiante("nuevo@example.com"))
            .thenReturn(Optional.empty());

        assertDoesNotThrow(() -> {
            estudianteService.validarEmailUnico("nuevo@example.com", null);
        });
    }

    // 10. Test para validarEmailUnico (email duplicado)
    @Test
    void validarEmailUnico_ConEmailExistente_DeberiaLanzarExcepcion() {
        when(estudianteRepository.findByEmailEstudiante("existente@example.com"))
            .thenReturn(Optional.of(estudianteEjemplo));

        assertThrows(EmailDuplicadoException.class, () -> {
            estudianteService.validarEmailUnico("existente@example.com", 2L); // ID diferente
        });
    }
}