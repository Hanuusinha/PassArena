package com.passArena.api_gateway.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class FallbackController {

    @RequestMapping(value = "/fallback/user-service", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT})
    public Mono<String> userServiceCircuitBreakerFallback() {
        return Mono.just("User Service is currently unavailable. Please try again later.");
    }

    @RequestMapping(value = "/fallback/movie-service", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT})
    public Mono<String> movieServiceCircuitBreakerFallback() {
        return Mono.just("Movie Service is currently unavailable. Please try again later.");
    }
}
