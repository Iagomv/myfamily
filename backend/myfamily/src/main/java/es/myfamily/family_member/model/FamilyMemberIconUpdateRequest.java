package es.myfamily.family_member.model;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FamilyMemberIconUpdateRequest {
  private Long familyId;
  private Long userId;
  @NotBlank
  @Length(max = 20, message = "Icon name must be at most 20 characters long")
  private String newIconName;
}
