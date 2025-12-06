package es.myfamily.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import es.myfamily.users.model.Users;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenUtil {

  @Value("${jwt.secret}")
  private String secret;

  @Value("${jwt.expiration}")
  private long expiration;

  public String generateToken(Users user) {
    Map<String, Object> claims = new HashMap<>();
    return doGenerateToken(claims, user);
  }

  private String doGenerateToken(Map<String, Object> claims, Users user) {
    Key key = Keys.hmacShaKeyFor(secret.getBytes());
    return Jwts.builder()
        .setClaims(claims)
        .setSubject(user.getEmail())
        .claim("userId", user.getId())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
        .signWith(key)
        .compact();
  }

  public Long getUserIdFromToken(String token) {
    Key key = Keys.hmacShaKeyFor(secret.getBytes());
    Claims claims = Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .getBody();
    return claims.get("userId", Long.class);
  }
}