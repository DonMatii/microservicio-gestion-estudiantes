package com.altias.microservicio_gestion_estudiantes.controller;

import static org.mockito.Mockito.doNothing;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print; // Import necesario

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.altias.microservicio_gestion_estudiantes.model.Estudiante;
import com.altias.microservicio_gestion_estudiantes.service.EstudianteService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(EstudianteController.class)
public class EstudianteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EstudianteService estudianteService;

    @Autowired
    private ObjectMapper objectMapper;

    private Estudiante estudianteEjemplo;

    @BeforeEach
    void setUp() {
        estudianteEjemplo = new Estudiante();
        estudianteEjemplo.setIdEstudiante(1L);
        estudianteEjemplo.setNombreEstudiante("Ana");
        estudianteEjemplo.setApellidoPaternoEstudiante("Gómez");
        estudianteEjemplo.setApellidoMaternoEstudiante("López");
        estudianteEjemplo.setEmailEstudiante("ana@example.com");
        estudianteEjemplo.setFechaNacimientoEstudiante(LocalDate.of(2000, 5, 15));
    }

    @Test
    void healthCheck_deberiaRetornarOk() throws Exception {
        mockMvc.perform(get("/edutech/estudiantes/health"))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(content().string("El Microservicio de estudiantes está funcionando correctamente! ✅"));
    }

    @Test
    void crearEstudiante_deberiaRetornarEstudianteCreado() throws Exception {
        when(estudianteService.guardarEstudiante(any(Estudiante.class)))
            .thenReturn(estudianteEjemplo);

        mockMvc.perform(post("/edutech/estudiantes")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(estudianteEjemplo)))
               .andDo(print())
               .andExpect(status().isCreated())
               .andExpect(content().string(containsString("Estudiante creado exitosamente")));
    }

    @Test
    void buscarPorEmail_cuandoExiste_deberiaRetornarEstudiante() throws Exception {
        when(estudianteService.buscarPorEmail("ana@example.com"))
            .thenReturn(Optional.of(estudianteEjemplo));
    
        mockMvc.perform(get("/edutech/estudiantes/buscar-por-email")
               .param("email", "ana@example.com"))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(content().string(containsString("Ana")))
               .andExpect(content().string(containsString("ana@example.com")));
    }

    @Test
    void buscarPorEmail_cuandoNoExiste_deberiaRetornar404() throws Exception {
        when(estudianteService.buscarPorEmail("noexiste@example.com"))
            .thenReturn(Optional.empty());

        mockMvc.perform(get("/edutech/estudiantes/buscar-por-email")
               .param("email", "noexiste@example.com"))
               .andDo(print())
               .andExpect(status().isNotFound());
    }

    @Test
    void eliminarEstudiante_deberiaRetornarOk() throws Exception {
        doNothing().when(estudianteService).eliminarEstudiante(1L);

        mockMvc.perform(delete("/edutech/estudiantes/1"))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(content().string(containsString("Estudiante eliminado correctamente")));
    }
}