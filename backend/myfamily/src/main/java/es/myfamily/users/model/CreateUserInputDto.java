package es.myfamily.users.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CreateUserInputDto {

  @Pattern(regexp = "^[a-zA-Z0-9_]{3,20}$", message = "Username must be 3-20 characters and contain only letters, numbers, and underscores")
  private String username;

  @Email
  private String email;

  @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,20}$", message = "Password must be 8-20 characters, include at least one uppercase letter, one lowercase letter, and one number")
  private String password;
}
