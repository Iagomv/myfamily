package es.myfamily.documents.model;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentDto {
  private Long id;
  private String title;
  private String filePath;
  private String storageType;
  private String fileType;
  private Long fileSize;
  private Long categoryId;
  private Long familyMemberId;
  private Long familyId;
  private Date uploadDate;
}
