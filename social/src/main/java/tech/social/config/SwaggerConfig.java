package tech.social.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("Social Network API")
                        .version("1.0.0")
                        .description("""
                                API REST de uma rede social simples desenvolvida com Spring Boot.
                                
                                **Funcionalidades:**
                                - Autenticação JWT
                                - Gerenciamento de usuários (registro, login, promoção/rebaixamento)
                                - CRUD de posts com paginação
                                - Controle de autorização por roles (USER e ADMIN)
                                
                                **Como usar:**
                                1. Faça login em `/auth/login` com as credenciais (admin/12345 ou crie um usuário)
                                2. Copie o token JWT retornado
                                3. Clique no botão "Authorize" (cadeado verde) no topo da página
                                4. Cole o token no campo "Value" e clique em "Authorize"
                                5. Agora você pode testar os endpoints protegidos!
                                """))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Servidor de Desenvolvimento")
                ))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Insira o token JWT obtido no endpoint /auth/login")))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName));
    }
}