package es.myfamily.documents.service;

import java.util.List;

import es.myfamily.documents.model.DocumentCategoryDto;
import es.myfamily.documents.model.DocumentCategoryRequestDto;

public interface DocumentCategoryService {

  List<DocumentCategoryDto> getDocumentCategoriesByFamilyId(Long familyId);

  DocumentCategoryDto createDocumentCategory(DocumentCategoryRequestDto dto, Long familyId);

  void deleteDocumentCategory(Long familyId, Long categoryId);
}
