package io.github.djordjije11.reeled.config.openapi;

import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Djordjije Radovic
 */
class InternalServiceEndpointGatewayOperationDataMapProvider {

    private final Map<Endpoint, GatewayOperationData> internalServiceEndpointGatewayOperationDataMap;

    InternalServiceEndpointGatewayOperationDataMapProvider(GatewayProperties gatewayProperties) {
        this.internalServiceEndpointGatewayOperationDataMap = initInternalServiceEndpointGatewayOperationDataMap(gatewayProperties);
    }

    private static Map<Endpoint, GatewayOperationData> initInternalServiceEndpointGatewayOperationDataMap(GatewayProperties gatewayProperties) {
        final Map<Endpoint, GatewayOperationData> internalServiceEndpointGatewayOperationDataMap = new HashMap<>();

        Optional.ofNullable(gatewayProperties.getRoutes()).ifPresent(routeDefinitions -> routeDefinitions.forEach(routeDefinition -> {
            final String gatewayOperationId = routeDefinition.getId();
            final String gatewayPath = getPredicate(routeDefinition, "Path");
            final String gatewayMethod = getPredicate(routeDefinition, "Method");
            final String internalServicePath = getFilter(routeDefinition, "SetPath");

            internalServiceEndpointGatewayOperationDataMap.put(Endpoint.of(internalServicePath, gatewayMethod),
                    new GatewayOperationData(gatewayOperationId, Endpoint.of(gatewayPath, gatewayMethod)));
        }));

        return internalServiceEndpointGatewayOperationDataMap;
    }

    private static String getPredicate(RouteDefinition routeDefinition, String predicateName) {
        return routeDefinition.getPredicates()
                .stream()
                .filter(predicateDefinition -> predicateDefinition.getName().equals(predicateName))
                .map(PredicateDefinition::getArgs)
                .map(Map::values)
                .flatMap(Collection::stream)
                .findFirst()
                .orElse(null);
    }

    private static String getFilter(RouteDefinition routeDefinition, String filterName) {
        return routeDefinition.getFilters()
                .stream()
                .filter(filterDefinition -> filterDefinition.getName().equals(filterName))
                .map(FilterDefinition::getArgs)
                .map(Map::values)
                .flatMap(Collection::stream)
                .findFirst()
                .orElse(null);
    }

    Map<Endpoint, GatewayOperationData> getInternalServiceEndpointGatewayOperationDataMap() {
        return internalServiceEndpointGatewayOperationDataMap;
    }
}
