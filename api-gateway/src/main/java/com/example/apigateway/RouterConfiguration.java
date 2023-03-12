package com.example.apigateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.util.Optional;

@Configuration
public class RouterConfiguration {

    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p
                        .path("/get")
                        .filters(f -> f.addRequestParameter("Hello", "World"))
                        .uri("http://httpbin.org:80")
                )
//                .route(p -> p
//                        .path("/api/user")
//                        .uri("http://localhost:8000")
//                )
                .route(p -> p
                        .path("/api/event")
                        .uri("http://localhost:8010")
                )
                .build();

    }
}
