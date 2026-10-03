package com.passArena.api_gateway.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    public boolean isTokenValid(String token)
    {
        System.out.println("Jwt secret length(Validation) = " +
                secretKey.getBytes(StandardCharsets.UTF_8).length);

        System.out.println("Jwt secret hash(Validation) = " +
                Integer.toHexString(secretKey.hashCode()));

        try{
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {

            return false;
        }
    }


    public String extractUserId(String token)
    {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    private SecretKey getSigningKey() {
        byte[] bytes = Decoders.BASE64.decode(secretKey);

        return Keys.hmacShaKeyFor(bytes);
    }
}
