package es.myfamily.documents.mapper;

import org.springframework.stereotype.Component;

import es.myfamily.documents.model.DocumentCategory;
import es.myfamily.documents.model.DocumentCategoryDto;
import es.myfamily.documents.model.DocumentCategoryRequestDto;

@Component
public class DocumentCategoryMapper {

  public DocumentCategory toEntity(DocumentCategoryDto dto) {
    return DocumentCategory.builder()
        .id(dto.getId())
        .name(dto.getName())
        .familyId(null)
        .build();
  }

  public DocumentCategory toEntity(DocumentCategoryRequestDto dto, Long familyId) {
    return DocumentCategory.builder()
        .name(dto.getName())
        .description(dto.getDescription())
        .familyId(familyId)
        .build();
  }

  public DocumentCategoryDto toDto(DocumentCategory entity) {
    return new DocumentCategoryDto(
        entity.getId(),
        entity.getName(),
        entity.getDescription());
  }
}
