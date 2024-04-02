package com.lurodev.usersmicroservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor

public class JwtService {
    @Value("${spring.application.security.jwt.secret-key}")
    private String SECRET_KEY; //DEBE CUMPLIR CON EL MINIMO DE SEGURIDAD PARA ESTE TIPO DE CLAVES, SI NO ARROJA ERROR
    @Value("${spring.application.security.jwt.expiration}")
    private int DAYS_VALIDITY_TOKEN;
    @Value("${multitenancy.http.header-name}")
    String tenantIdHeaderName;
    @Getter
    @Setter
    private String requestTenantId;


    private Date calculateExpirationDate(int validDays){
        final int HOURS_VALIDITY_TOKEN = 24 * validDays;
        final int MINUTES_VALIDITY_TOKEN = 60 * HOURS_VALIDITY_TOKEN;
        final long SECONDS_VALIDITY_TOKEN = MINUTES_VALIDITY_TOKEN * 60L;
        final long TIME_VALIDITY_TOKEN = 1000L * SECONDS_VALIDITY_TOKEN; //valor en milisegundos
        return new Date(System.currentTimeMillis() + TIME_VALIDITY_TOKEN);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .setSigningKey(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(UserDetails userDetails){
        HashMap<String, Object> extraClaims = new HashMap<String, Object>();
        extraClaims.put(tenantIdHeaderName, requestTenantId);
        return generateToken(extraClaims, userDetails);
    }
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails){
        return Jwts
                .builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(this.calculateExpirationDate(DAYS_VALIDITY_TOKEN))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isValidToken(String jwtToken, UserDetails userDetails, String requestTenantId){
        final String username = extractUsername(jwtToken);
        final String tenantId = extractTenantId(jwtToken);

        return (username.equals(userDetails.getUsername())) &&
                !isTokenExpired(jwtToken) &&
                requestTenantId.equals(tenantId);
    }

    private boolean isTokenExpired(String jwtToken) {
        return extractExpiration(jwtToken).before(new Date());
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String extractTenantId(String token){
        return extractClaim(token, claims -> claims.get(tenantIdHeaderName, String.class));
    }
}
