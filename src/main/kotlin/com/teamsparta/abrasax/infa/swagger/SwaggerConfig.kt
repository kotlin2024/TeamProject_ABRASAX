package com.teamsparta.abrasax.infa.swagger

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class SwaggerConfig {
    @Bean
    fun openApi(): OpenAPI = OpenAPI()
        .components(
            Components()
        )
        .info(
            Info()
                .title("Abrasax API")
                .description("Abrasaxs API schema")
                .version("1.0.0"),
        )
}
