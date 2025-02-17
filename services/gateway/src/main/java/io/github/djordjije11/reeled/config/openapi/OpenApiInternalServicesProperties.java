package io.github.djordjije11.reeled.config.openapi;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Set;

/**
 * @author Djordjije Radovic
 */
@ConfigurationProperties("reeled.gateway.openapi.internal-services")
record OpenApiInternalServicesProperties(Set<String> urls) {

}
