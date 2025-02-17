package io.github.djordjije11.reeled.config;

import io.github.djordjije11.reeled.commons.openapi.DurationSchema;
import io.github.djordjije11.reeled.commons.openapi.SchemaReplacer;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * @author Djordjije Radovic
 */
@Configuration
@OpenAPIDefinition(info = @Info(title = "Internal Reeled API (post-service)", version = "1.0.0-SNAPSHOT"))
class OpenApiConfiguration {

    @Bean
    SchemaReplacer schemaReplacer() {
        return new SchemaReplacer.Builder().replace(Duration.class, DurationSchema.class).build();
    }
}
