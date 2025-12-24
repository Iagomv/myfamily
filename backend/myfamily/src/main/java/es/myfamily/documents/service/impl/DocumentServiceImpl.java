package es.myfamily.documents.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.myfamily.documents.mapper.DocumentMapper;
import es.myfamily.documents.model.Document;
import es.myfamily.documents.model.DocumentDtoResponse;
import es.myfamily.documents.model.DocumentRequest;
import es.myfamily.documents.repository.DocumentRepository;
import es.myfamily.documents.service.DocumentService;

@Service
public class DocumentServiceImpl implements DocumentService {

  @Autowired
  private DocumentRepository docRepo;

  @Autowired
  private DocumentMapper docMapper;

  @Autowired
  private es.myfamily.documents.service.impl.DocumentStorageService storageService;

  @Autowired
  private es.myfamily.utils.Validations validations;

  @Autowired
  private es.myfamily.utils.SecurityUtils securityUtils;

  @Override
  public List<DocumentDtoResponse> getDocumentsByFamily(Long familyId) {
    List<Document> documents = docRepo.findAllByFamilyId(familyId);
    return documents.stream()
        .map(docMapper::toDto)
        .toList();
  }

  @Override
  public org.springframework.core.io.Resource loadDocumentResource(Long documentId) {
    Document document = docRepo.findById(documentId).orElseThrow(
        () -> new es.myfamily.exception.MyFamilyException(org.springframework.http.HttpStatus.NOT_FOUND, "Document not found"));

    Long userId = securityUtils.getUserFromContext().getId();
    validations.UserInFamily(document.getFamily().getId(), userId);

    return storageService.loadAsResource(document.getFilePath());
  }

  @Override
  public List<DocumentDtoResponse> getDocumentsByFamilyAndCategory(Long familyId, Long categoryId) {
    List<Document> documents = docRepo.findAllByFamilyIdAndCategoryId(familyId, categoryId);
    return documents.stream()
        .map(docMapper::toDto)
        .toList();
  }

  @Override
  public DocumentDtoResponse addDocument(Long familyId, DocumentRequest doc) {
    Document entity = docMapper.toEntity(doc, familyId);
    long size = storageService.storeFile(doc, entity.getFilePath());
    entity.setFileSize(size);
    return docMapper.toDto(docRepo.save(entity));
  }

  @Override
  public DocumentDtoResponse getDocumentById(Long documentId) {
    Document d = docRepo.findById(documentId).orElseThrow(
        () -> new es.myfamily.exception.MyFamilyException(org.springframework.http.HttpStatus.NOT_FOUND, "Document not found"));
    return docMapper.toDto(d);
  }

}