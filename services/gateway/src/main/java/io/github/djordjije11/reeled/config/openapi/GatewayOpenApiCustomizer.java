package io.github.djordjije11.reeled.config.openapi;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.PathItem.HttpMethod;
import io.swagger.v3.oas.models.Paths;
import org.springdoc.core.customizers.OpenApiCustomizer;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * @author Djordjije Radovic
 */
public class GatewayOpenApiCustomizer implements OpenApiCustomizer {

    private static final String GATEWAY_OPERATION_ID_FORMAT = "%s_%s";

    private final List<OpenAPI> internalServiceOpenApis;

    private final Map<Endpoint, GatewayOperationData> internalServiceEndpointGatewayOperationMap;

    GatewayOpenApiCustomizer(List<OpenAPI> internalServiceOpenApis, Map<Endpoint, GatewayOperationData> internalServiceEndpointGatewayOperationMap) {
        this.internalServiceOpenApis = internalServiceOpenApis;
        this.internalServiceEndpointGatewayOperationMap = internalServiceEndpointGatewayOperationMap;
    }

    @Override
    public void customise(OpenAPI openApi) {
        openApi.paths(new Paths()).components(new Components());
        internalServiceOpenApis.forEach(internalServiceOpenApi -> merge(openApi, internalServiceOpenApi));
    }

    private void merge(OpenAPI gatewayOpenApi, OpenAPI internalServiceOpenApi) {
        mergePaths(gatewayOpenApi, internalServiceOpenApi);
        mergeComponents(gatewayOpenApi, internalServiceOpenApi);
    }

    private void mergePaths(OpenAPI gatewayOpenApi, OpenAPI internalServiceOpenApi) {
        final Paths internalServicePaths = internalServiceOpenApi.getPaths();

        if (internalServicePaths == null) {
            return;
        }

        internalServicePaths.forEach((internalServicePath, internalServicePathItem) -> internalServicePathItem.readOperationsMap()
                .forEach((internalServicePathHttpMethod, internalServicePathOperation) -> addOperation(gatewayOpenApi,
                        internalServicePath,
                        internalServicePathHttpMethod,
                        internalServicePathOperation)));
    }

    private void addOperation(OpenAPI gatewayOpenApi,
                              String internalServicePath,
                              HttpMethod internalServicePathHttpMethod,
                              Operation internalServicePathOperation) {
        final GatewayOperationData gatewayOperationData = internalServiceEndpointGatewayOperationMap.get(new Endpoint(internalServicePath,
                internalServicePathHttpMethod));
        final Endpoint gatewayEndpoint = gatewayOperationData.endpoint();
        final Operation gatewayOperation = operationWithFormattedId(internalServicePathOperation, gatewayOperationData.id());

        gatewayOpenApi.getPaths().compute(gatewayEndpoint.path(), (gatewayPath, gatewayPathItem) -> {
            if (gatewayPathItem == null) {
                return createPathItem(gatewayEndpoint.method(), gatewayOperation);
            }

            gatewayPathItem.operation(gatewayEndpoint.method(), gatewayOperation);
            return gatewayPathItem;
        });
    }

    private static Operation operationWithFormattedId(Operation operation, String gatewayOperationId) {
        operation.setOperationId(String.format(GATEWAY_OPERATION_ID_FORMAT, gatewayOperationId, operation.getOperationId()));
        return operation;
    }

    private static PathItem createPathItem(HttpMethod httpMethod, Operation operation) {
        final PathItem pathItem = new PathItem();
        pathItem.operation(httpMethod, operation);
        return pathItem;
    }

    private static void mergeComponents(OpenAPI gatewayOpenApi, OpenAPI internalServiceOpenApi) {
        final Components gatewayComponents = gatewayOpenApi.getComponents();
        final Components internalServiceComponents = internalServiceOpenApi.getComponents();

        if (internalServiceComponents == null) {
            return;
        }

        mergeMapComponent(gatewayComponents, internalServiceComponents, Components::getSchemas, Components::setSchemas);
        mergeMapComponent(gatewayComponents, internalServiceComponents, Components::getResponses, Components::setResponses);
        mergeMapComponent(gatewayComponents, internalServiceComponents, Components::getParameters, Components::setParameters);
        mergeMapComponent(gatewayComponents, internalServiceComponents, Components::getExamples, Components::setExamples);
        mergeMapComponent(gatewayComponents, internalServiceComponents, Components::getRequestBodies, Components::setRequestBodies);
        mergeMapComponent(gatewayComponents, internalServiceComponents, Components::getHeaders, Components::setHeaders);
        mergeMapComponent(gatewayComponents, internalServiceComponents, Components::getSecuritySchemes, Components::setSecuritySchemes);
        mergeMapComponent(gatewayComponents, internalServiceComponents, Components::getLinks, Components::setLinks);
        mergeMapComponent(gatewayComponents, internalServiceComponents, Components::getCallbacks, Components::setCallbacks);
        mergeMapComponent(gatewayComponents, internalServiceComponents, Components::getPathItems, Components::setPathItems);
    }

    private static <T, R> void mergeMapComponent(Components gatewayComponents,
                                                 Components internalServiceComponents,
                                                 Function<Components, Map<T, R>> componentGetter,
                                                 BiConsumer<Components, Map<T, R>> componentSetter) {
        if (componentGetter.apply(internalServiceComponents) == null) {
            return;
        }

        Optional.ofNullable(componentGetter.apply(gatewayComponents))
                .ifPresentOrElse(existing -> existing.putAll(componentGetter.apply(internalServiceComponents)),
                        () -> componentSetter.accept(gatewayComponents, componentGetter.apply(internalServiceComponents)));
    }
}
