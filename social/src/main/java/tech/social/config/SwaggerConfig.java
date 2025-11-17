package tech.social.controllers;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;

import java.util.List;

public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        // Define o esquema de segurança JWT
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                // Informações da API
                .info(new Info()
                        .title("Social Network API")
                        .version("1.0.0")
                        .description("""
                                API RESTful de uma rede social simples desenvolvida com Spring Boot 3.
                                
                                **Funcionalidades:**
                                - Autenticação JWT (Login/Registro)
                                - Gerenciamento de usuários (Promoção/Rebaixamento de roles)
                                - CRUD de posts
                                - Paginação de resultados
                                - Controle de acesso baseado em roles (USER/ADMIN)
                                
                                **Autenticação:**
                                1. Faça login em `/auth/login` para obter o token JWT
                                2. Clique no botão "Authorize" (🔒) no topo
                                3. Cole o token retornado
                                4. Teste os endpoints protegidos
                                """)
                        .contact(new Contact()
                                .name("Seu Nome")
                                .email("seu@email.com")
                                .url("https://github.com/seu-usuario"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))

                // Servidores
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Servidor de Desenvolvimento"),
                        new Server()
                                .url("https://api.exemplo.com")
                                .description("Servidor de Produção (opcional)")
                ))

                // Configuração de segurança JWT
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Insira o token JWT obtido no endpoint /auth/login")))

                // Aplicar segurança globalmente
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName));
    }
}
