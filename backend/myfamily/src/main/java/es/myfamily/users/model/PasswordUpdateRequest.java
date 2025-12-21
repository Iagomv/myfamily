package es.myfamily.users.model;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordUpdateRequest {

  @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,20}$", message = "Password must be 8-20 characters, include at least one uppercase letter, one lowercase letter, and one number")
  private String newPassword;
}
