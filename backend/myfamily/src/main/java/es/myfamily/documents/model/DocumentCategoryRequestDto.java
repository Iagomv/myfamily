package es.myfamily.documents.model;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentCategoryRequestDto {
  @Pattern(regexp = "^[\\p{L}0-9 _-]+$", message = "Name can only contain letters (including accents), numbers, spaces, underscores, and hyphens.")
  @Length(min = 1, max = 50, message = "Name must be between 1 and 50 characters long.")
  private String name;

  @Pattern(regexp = "^[\\p{L}0-9 _.,!\"'()\\-]*$", message = "Description contains invalid characters.")
  @Length(max = 200, message = "Description must be at most 200 characters long.")
  private String description;

}
