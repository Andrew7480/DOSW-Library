package edu.eci.dosw.tdd.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    //http://localhost:8080/swagger-ui.html
    //https://localhost:8443/swagger-ui/index.html
    
    @Bean
    public OpenAPI libraryOpenAPI() {
        return new OpenAPI()
            .components(new Components().addSecuritySchemes("bearerAuth",
                new SecurityScheme()
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")))
            .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .info(new Info()
                        .title("DOSW Library API")
                        .description("API para gestion de libros, usuarios y prestamos")
                        .version("v1.0.0")
                        .contact(new Contact().name("Equipo DOSW"))
                        .license(new License().name("Uso academico")));
    }
}
