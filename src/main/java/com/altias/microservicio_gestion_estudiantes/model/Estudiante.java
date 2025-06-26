package com.altias.microservicio_gestion_estudiantes.model;

import java.time.LocalDate;

import org.springframework.hateoas.RepresentationModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "estudiantes")
public class Estudiante extends RepresentationModel<Estudiante> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estudiantes")
    private Long idEstudiante;

    @NotBlank(message = "El nombre del estudiante no puede estar vacio")
    @Size(min = 2, max = 100, message = "El nombre del estudiante debe tener un minimo 2 caracteres y un maximo 100 caracteres")
    @Column(name = "nombre_estudiante", nullable = false, length = 100)
    private String nombreEstudiante;

    @NotBlank(message = "El apellido paterno del estudiante no puede estar vacio")
    @Size(min = 2, max = 100, message = "El apellido paterno del estudiante debe tener un minimo de 2 caracteres y un maximo de 100 caracteres")
    @Column(name = "apellido_paterno_estudiante", nullable = false, length = 100)
    private String apellidoPaternoEstudiante;

    @NotBlank(message = "El apellido materno del estudiante no puede estar vacio")
    @Size(min = 2, max = 100, message = "El apellido materno del estudiante debe tener un minimo de 2 caracteres y un maximo de 100 caracteres")
    @Column(name = "apellido_materno_estudiante")
    private String apellidoMaternoEstudiante;

    @NotBlank(message = "El E-mail del estudiante no puede estar vacio")
    @Email(message = "El E-mail del estudiante debe tener un formato valido. Por ejemplo: tu_correo@gmail.com")
    @Size(max = 150, message = "El E-mail del estudiante no debe superar los 150 caracteres")
    @Column(name = "email_estudiante")
    private String emailEstudiante;

    @NotNull(message = "La fecha de nacimiento del estudiante no puede estar vacia")
    @Column(name = "fecha_nacimiento_estudiante", nullable = false)
    private LocalDate fechaNacimientoEstudiante;
}