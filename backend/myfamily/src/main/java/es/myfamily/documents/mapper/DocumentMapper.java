package es.myfamily.documents.mapper;

import es.myfamily.documents.model.Document;
import es.myfamily.documents.model.DocumentDtoResponse;
import es.myfamily.documents.model.DocumentRequest;

public interface DocumentMapper {

  public DocumentDtoResponse toDto(Document document);

  public Document toEntity(DocumentRequest docRequest, Long familyId);
}
