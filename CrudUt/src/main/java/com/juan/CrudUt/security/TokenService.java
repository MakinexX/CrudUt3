package com.juan.CrudUt.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

@Service
public class TokenService {

    private final String SECRET_KEY = "ZnJhc2VzbGFyZ2FzcGFyYWNvbG9jYXJjb21vY2xhdmVlbnVucHJvamVjdG9kZWVtZXBsb3BhcmFqd3Rjb25zcHJpbmdzZWN1cml0eQ==bWlwcnVlYmFkZWVqbXBsb3BhcmFiYXNlNjQ="; // Generar una SECRET_KEY de 32 bytes
    public static final long TOKEN_EXPIRATION_TIME = 864_000_000;
    
    private String generateSecretKey(int length) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] key = new byte[length];
        secureRandom.nextBytes(key);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(key);
    }

    public String generateToken(String apiKey) {
        Map<String, Object> claims = new HashMap<>();
        return "Bearer " + Jwts.builder()
                .setClaims(claims)
                .setSubject(apiKey)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 horas de validez
                .signWith(getSigningKey(SECRET_KEY) ,SignatureAlgorithm.HS256)                
                .compact();
    }
    public static Key getSigningKey(String secret) {
		byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
		return Keys.hmacShaKeyFor(keyBytes);
	}
    public boolean validateToken(String token, String apiKey) {
        try {
            String subject = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey(SECRET_KEY))
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
            return subject.equals(apiKey) && !isTokenExpired(token);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }  
    private boolean isTokenExpired(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }
}
