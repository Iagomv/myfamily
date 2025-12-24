package es.myfamily.documents.service.impl;

import java.util.List;

import org.apache.catalina.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.myfamily.documents.mapper.DocumentCategoryMapper;
import es.myfamily.documents.model.DocumentCategory;
import es.myfamily.documents.model.DocumentCategoryDto;
import es.myfamily.documents.model.DocumentCategoryRequestDto;
import es.myfamily.documents.repository.DocumentCategoryRepository;
import es.myfamily.documents.service.DocumentCategoryService;
import es.myfamily.utils.SecurityUtils;
import es.myfamily.utils.Validations;

@Service
public class DocumentCategoryServiceImpl implements DocumentCategoryService {

  @Autowired
  private DocumentCategoryRepository docCategoryRepo;
  @Autowired
  private DocumentCategoryMapper docCategoryMapper;

  @Autowired
  private Validations validations;

  @Autowired
  private SecurityUtils securityUtils;

  @Override
  public List<DocumentCategoryDto> getDocumentCategoriesByFamilyId(Long familyId) {
    validations.UserInFamily(familyId, securityUtils.getUserFromContext().getId());
    return docCategoryRepo.findAllByFamilyId(familyId).stream()
        .map(docCategoryMapper::toDto)
        .toList();
  }

  @Override
  public DocumentCategoryDto createDocumentCategory(DocumentCategoryRequestDto dto, Long familyId) {
    validations.UserInFamily(familyId, securityUtils.getUserFromContext().getId());
    if (docCategoryRepo.existsByNameAndFamilyId(dto.getName(), familyId)) {
      throw new IllegalArgumentException("Ya existe una categoría de documento con ese nombre en la familia.");
    }
    return docCategoryMapper.toDto(docCategoryRepo.save(docCategoryMapper.toEntity(dto, familyId)));
  }

  @Override
  public void deleteDocumentCategory(Long familyId, Long categoryId) {
    validations.UserInFamily(familyId, securityUtils.getUserFromContext().getId());
    DocumentCategory docCategory = docCategoryRepo.findById(categoryId).orElseThrow(
        () -> new IllegalArgumentException("La categoría de documento no existe."));

    if (!docCategory.getFamilyId().equals(familyId)) {
      throw new IllegalArgumentException("La categoría de documento no pertenece a la familia especificada.");
    }

    docCategoryRepo.deleteById(categoryId);
  }

}