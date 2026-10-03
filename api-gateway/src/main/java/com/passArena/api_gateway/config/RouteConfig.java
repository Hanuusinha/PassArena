package com.passArena.api_gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import java.time.Duration;

@Configuration
public class RouteConfig {

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("AUTH-ROUTE", route ->
                        route.path("/auth-service/**")
                        .filters(f -> f.rewritePath("/auth-service/(?<segment>.*)","/${segment}")
                                .retry(config -> config
                                .setRetries(3)
                                .setMethods(HttpMethod.GET, HttpMethod.POST)
                                .setBackoff(
                                        Duration.ofMillis(100),
                                        Duration.ofMillis(1000),
                                        2,
                                        true
                                )))
                                .uri("lb://auth-service"))
                .route("user-route", route ->
                        route.path("/user-service/**")
                                .filters(f ->
                                        f.circuitBreaker(config -> config.setName("userServiceCircuitBreaker")
                                                .setFallbackUri("forward:/fallback/user-service"))
                                                .rewritePath("/user-service/(?<segment>.*)", "/${segment}"))
                                .uri("lb://user-service"))
                .build();
    }
}
