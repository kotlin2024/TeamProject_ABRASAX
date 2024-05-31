package com.teamsparta.abrasax.infa.swagger

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

private const val SECURITY_SCHEME_NAME = "authorization"    // 추가

@Configuration
class SwaggerConfig {
    @Bean
    fun openApi(): OpenAPI = OpenAPI()
        .components(
            Components().addSecuritySchemes(
                SECURITY_SCHEME_NAME,
                SecurityScheme()
                    .name(SECURITY_SCHEME_NAME)
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT"),
            ),
        ).addSecurityItem(SecurityRequirement().addList(SECURITY_SCHEME_NAME))
        .info(
            Info()
                .title("Abrasax API")
                .description("Abrasaxs API schema")
                .version("1.0.0"),
        )
}
