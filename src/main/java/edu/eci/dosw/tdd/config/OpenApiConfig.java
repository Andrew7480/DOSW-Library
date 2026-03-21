package edu.eci.dosw.tdd.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    //http://localhost:8080/swagger-ui.html
    
    @Bean
    public OpenAPI libraryOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("DOSW Library API")
                        .description("API para gestion de libros, usuarios y prestamos")
                        .version("v1.0.0")
                        .contact(new Contact().name("Equipo DOSW"))
                        .license(new License().name("Uso academico")));
    }
}
