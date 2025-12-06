package es.myfamily.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import es.myfamily.config.security.UserPrincipal;
import java.io.IOException;
import java.security.Key;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

  @Value("${jwt.secret}")
  private String jwtSecret;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String header = request.getHeader("Authorization");
    logger.debug("Authorization Header: {}", header);

    if (header != null && header.startsWith("Bearer ")) {
      String token = header.substring(7);
      logger.debug("Extracted Token: {}", token);
      try {
        Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody();

        logger.debug("Token Claims: {}", claims);

        Long userId = claims.get("userId", Long.class);
        String email = claims.getSubject();

        logger.debug("Extracted UserId: {}, Email: {}", userId, email);

        UserPrincipal userPrincipal = new UserPrincipal(userId, email, null);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
            userPrincipal, null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
      } catch (JwtException e) {
        logger.error("JWT validation failed: {}", e.getMessage());
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
        return;
      }
    }
    filterChain.doFilter(request, response);
  }
}