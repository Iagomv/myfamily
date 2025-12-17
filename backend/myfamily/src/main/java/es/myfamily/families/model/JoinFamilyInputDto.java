package es.myfamily.families.model;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JoinFamilyInputDto {

  @NotBlank(message = "Invitation code must not be blank")
  @Length(min = 8, max = 8, message = "Invalid invitation code")
  private String invitationCode;
}
