package com.altias.microservicio_gestion_estudiantes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.hateoas.HypermediaAutoConfiguration;

@SpringBootApplication(exclude = {
    HypermediaAutoConfiguration.class
})
public class MicroservicioGestionEstudiantesApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicroservicioGestionEstudiantesApplication.class, args);
    }
}