package com.passArena.api_gateway.security;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter {

    private final JwtService jwtService;


    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain)
    {
        String path = exchange.getRequest()
                .getURI()
                .getPath();

        if(path.startsWith("/auth-service/auth"))
        {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);


        if(authHeader == null || !authHeader.startsWith("Bearer "))
        {
            exchange.getResponse()
                    .setStatusCode(HttpStatus.UNAUTHORIZED);

            return exchange.getResponse()
                    .setComplete();
        }

        String token = authHeader.substring(7);

        //Invalid token:
        if(!jwtService.isTokenValid(token))
        {
            exchange.getResponse()
                    .setStatusCode(HttpStatus.UNAUTHORIZED);

            return exchange.getResponse()
                    .setComplete();
        }

        //Extract authenticated user id:
        String userId = jwtService.extractUserId(token);

        //Forward authenticated user to downstream services:
        ServerWebExchange modifiedExchange = exchange.mutate()
                .request(request -> request.headers(headers -> {
                    //Remove client supplied value:
                    headers.remove("X-User-Id");

                    //Add value obtained from jwt:
                    headers.add("X-User-Id", userId);
                }))
                .build();

        //Token is valid:
        return chain.filter(modifiedExchange);
    }
}
