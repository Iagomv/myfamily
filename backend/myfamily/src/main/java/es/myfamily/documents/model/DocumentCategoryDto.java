package es.myfamily.documents.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentCategoryDto {
  private Long id;
  private String name;
  private String description;
}
