//package com.microservice_jwt.api_gateway.config;
//
//import org.springdoc.core.properties.SwaggerUiConfigParameters;
//import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Configuration
//public class SwaggerConfig {
//
//    @Bean
//    public List<String> swaggerUiConfigParameters(SwaggerUiConfigParameters configParameters,
//                                                  RouteDefinitionLocator locator) {
//        return locator.getRouteDefinitions().collectList().block().stream()
//                .map(route -> route.getId())
//                .filter(id -> id.matches(".*-service.*"))  // Match "auth-service-swagger"
//                .map(id -> "/v3/api-docs/" + id.replace("-service", ""))
//                .peek(configParameters::addGroup)
//                .collect(Collectors.toList());
//    }
//}
