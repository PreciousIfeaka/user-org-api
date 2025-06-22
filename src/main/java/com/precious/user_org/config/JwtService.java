package com.precious.user_org.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${security.jwt.jwt-secret}")
    private String secretKey;

    @Value("${security.jwt.jwt-expiry}")
    private long jwtExpiry;

    private Key getSigningkey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String jwt) {
        return extractClaim(jwt, Claims::getSubject);
    }

    public Claims extractClaims(String jwt) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningkey())
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }

    public <T> T extractClaim(String jwt, Function<Claims, T> claimsResolver) {
        final Claims claims = this.extractClaims(jwt);
        return claimsResolver.apply(claims);
    }

    public String generateJwt(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiry))
                .signWith(getSigningkey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateJwt(
            UserDetails userDetails
    ) {
        return generateJwt(new HashMap<>(), userDetails);
    }

    public boolean isTokenValid(String jwt, UserDetails userDetails) {
        return this.extractClaim(jwt, Claims::getExpiration).after(new Date(System.currentTimeMillis())) &&
                this.extractUsername(jwt).equals(userDetails.getUsername());
    }
}
