package es.myfamily.documents.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentRequest {

  @Pattern(regexp = "^[\\p{L}0-9_]{3,20}$", message = "Title must be 3-20 characters and contain only letters, numbers, underscores and accented letters")
  private String title;

  @NotNull(message = "Category ID cannot be null")
  private Long categoryId;

  @NotEmpty(message = "File data cannot be empty")
  private Byte[] fileData;
}
