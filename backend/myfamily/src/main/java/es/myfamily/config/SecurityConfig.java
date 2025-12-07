package es.myfamily.config;

import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {

  @Autowired
  private JwtAuthenticationFilter jwtAuthenticationFilter;

  @Value("${app.cors.allowed-origins:http://localhost:4200}")
  private String allowedOrigins;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .cors(Customizer.withDefaults())
        .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(HttpMethod.POST, "/users", "/users/login").permitAll()
            .requestMatchers("/auth/**").permitAll()
            // Allow unauthenticated access to actuator health/info for quick testing
            // through the tunnel
            .requestMatchers("/api/actuator/**").permitAll()
            .requestMatchers("/actuator/**").permitAll()
            .requestMatchers(HttpMethod.OPTIONS).permitAll()
            // Require authentication for all other requests
            .anyRequest().authenticated())
        .httpBasic(basic -> basic.disable())
        .formLogin(login -> login.disable())
        .exceptionHandling(ex -> ex.authenticationEntryPoint(
            (req, res, ex2) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED)))
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();
    // Read allowed origins from property `app.cors.allowed-origins`
    // (comma-separated)
    // Fallback to localhost for convenience
    String[] originsArray = allowedOrigins.split(",");
    List<String> origins = Arrays.stream(originsArray)
        .map(String::trim)
        .filter(s -> !s.isEmpty())
        .collect(Collectors.toList());
    // Use setAllowedOriginPatterns to allow wildcards and credentials
    if (origins.isEmpty() || (origins.size() == 1 && origins.get(0).equals("*"))) {
      config.setAllowedOriginPatterns(List.of("*"));
    } else {
      config.setAllowedOriginPatterns(origins);
    }

    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
    config.setAllowedHeaders(List.of("*"));
    config.setAllowCredentials(true);
    config.setExposedHeaders(List.of("Location"));

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return source;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
