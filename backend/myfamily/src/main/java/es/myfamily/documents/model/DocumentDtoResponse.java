package es.myfamily.documents.model;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocumentDtoResponse {
  private Long id;
  private String title;
  private String filePath;
  private String fileType;
  private Long fileSize;
  private String categoryName;
  private String familyMemberName;
  private Long familyId;
  private LocalDate uploadDate;
}
