package es.myfamily.families.model;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateFamilyInputDto {

  @Pattern(regexp = "^[a-zA-ZÀ-ÿ\\s]{3,50}$", message = "Family name must contain only letters and spaces, and be 3 to 50 characters long")
  private String familyName;

}
