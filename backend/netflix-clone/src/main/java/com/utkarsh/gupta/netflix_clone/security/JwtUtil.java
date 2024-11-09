package com.utkarsh.gupta.netflix_clone.security;

import com.utkarsh.gupta.netflix_clone.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {
    @Value("${jwt.secret.key}")
    private String SECRET_KEY;
    @Value("${jwt.max.age}")
    public Integer MAX_AGE;
    public static String JWT_COOKIE_NAME = "jwt-netflix-app";

    private Key getSigningKey(){
        byte[] keyBytes = SECRET_KEY.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUserId(String token){
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
        return claimsResolver.apply(claims);
    }

    public boolean isTokenExpired(String token){
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    public String generateToken(User userDetails){
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getId());
    }

    public String createToken(Map<String, Object> claims, String userId){
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userId)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + MAX_AGE * 1000))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Boolean validateToken(String token, User user){
        final String userId = extractUserId(token);
        return (userId.equals(user.getId()) && !isTokenExpired(token));
    }


    public Cookie getCookie(User user){
        String token = generateToken(user);
        Cookie cookie = new Cookie(JWT_COOKIE_NAME, token);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(MAX_AGE);
        cookie.setSecure(true);
        cookie.setPath("/");
        return cookie;
    }
}
