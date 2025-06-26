package com.altias.microservicio_gestion_estudiantes.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.server.LinkRelationProvider;
import org.springframework.hateoas.server.core.DelegatingLinkRelationProvider;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("API Estudiantes EDUtech")
                .version("1.0")
                .description("Microservicio para la gestión de estudiantes, utilizando Spring Boot, HATEOAS y Swagger."));
    }

    @Bean
    public LinkRelationProvider linkRelationProvider() {
        return new DelegatingLinkRelationProvider();
    }
}
// Este archivo configura la documentación OpenAPI para el microservicio de gestión de estudiantes.
// Utiliza Swagger para generar la documentación de la API, proporcionando información básica como el título,
// la versión y una breve descripción del servicio. Esta configuración permite a los desarrolladores y usuarios
// entender mejor cómo interactuar con el microservicio y qué funcionalidades ofrece.
// La anotación @Configuration indica que esta clase contiene definiciones de beans que serán gestionados
// por el contenedor de Spring. El método customOpenAPI() crea una instancia de OpenAPI con la información
// deseada, que luego será utilizada por Swagger para generar la documentación de la API.

//Puedes acceder a la documentación de la API generada por Swagger en la URL: http://localhost:9090/swagger-ui/index.html#/