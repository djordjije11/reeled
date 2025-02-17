package io.github.djordjije11.reeled.config.openapi;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author Djordjije Radovic
 */
@Configuration
@OpenAPIDefinition(info = @Info(title = "Reeled API (gateway)", version = "1.0.0-SNAPSHOT"))
class OpenApiConfiguration {

    @Bean
    OpenApiCustomizer openApiCustomizer(InternalServicesOpenApiProvider internalServicesOpenApiProvider,
                                        InternalServiceEndpointGatewayOperationDataMapProvider internalServiceEndpointGatewayOperationDataMapProvider) {

        return new GatewayOpenApiCustomizer(internalServicesOpenApiProvider.getInternalServiceOpenApis(),
                internalServiceEndpointGatewayOperationDataMapProvider.getInternalServiceEndpointGatewayOperationDataMap());
    }

    @Bean
    InternalServicesOpenApiProvider internalServicesOpenApiProvider(OpenApiInternalServicesProperties openApiInternalServicesProperties,
                                                                    WebClient.Builder webClientBuilder) {
        return new InternalServicesOpenApiProvider(openApiInternalServicesProperties, webClientBuilder);
    }

    @Bean
    InternalServiceEndpointGatewayOperationDataMapProvider internalServiceGatewayEndpointsMapProvider(GatewayProperties gatewayProperties) {
        return new InternalServiceEndpointGatewayOperationDataMapProvider(gatewayProperties);
    }
}
