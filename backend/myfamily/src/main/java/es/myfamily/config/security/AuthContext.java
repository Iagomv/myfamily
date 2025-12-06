package es.myfamily.config.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthContext {

  public static Long getUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.isAuthenticated()) {
      Object principal = authentication.getPrincipal();
      if (principal instanceof UserPrincipal) {
        return ((UserPrincipal) principal).getUserId();
      }
    }
    throw new IllegalStateException("User is not authenticated");
  }

  public static String getEmail() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.isAuthenticated()) {
      Object principal = authentication.getPrincipal();
      if (principal instanceof UserPrincipal) {
        return ((UserPrincipal) principal).getEmail();
      }
    }
    throw new IllegalStateException("User is not authenticated");
  }
}