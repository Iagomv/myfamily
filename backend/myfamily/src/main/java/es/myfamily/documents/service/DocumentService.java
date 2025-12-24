package es.myfamily.documents.service;

import java.util.List;

import es.myfamily.documents.model.DocumentDtoResponse;
import es.myfamily.documents.model.DocumentRequest;
import jakarta.validation.Valid;

public interface DocumentService {
  List<DocumentDtoResponse> getDocumentsByFamily(Long familyId);

  List<DocumentDtoResponse> getDocumentsByFamilyAndCategory(Long familyId, Long categoryId);

  DocumentDtoResponse addDocument(Long familyId, DocumentRequest doc);

  org.springframework.core.io.Resource loadDocumentResource(Long documentId);

  DocumentDtoResponse getDocumentById(Long documentId);
}
