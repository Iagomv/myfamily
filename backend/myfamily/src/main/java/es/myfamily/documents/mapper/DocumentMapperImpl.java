package es.myfamily.documents.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.myfamily.documents.model.Document;
import es.myfamily.documents.model.DocumentDtoResponse;
import es.myfamily.documents.model.DocumentRequest;
import es.myfamily.family_member.service.FamilyMemberService;
import es.myfamily.family_member.service.impl.FamilyMemberServiceImpl;
import es.myfamily.family_member.utils.DocumentUtils;

@Component
public class DocumentMapperImpl implements DocumentMapper {

  @Autowired
  private DocumentUtils docUtils;

  @Override
  public DocumentDtoResponse toDto(Document document) {
    return DocumentDtoResponse.builder()
        .id(document.getId())
        .title(document.getTitle())
        .filePath(document.getFilePath())
        .fileType(document.getFileType())
        .fileSize(document.getFileSize())
        .categoryName(document.getCategory().getName())
        .familyMemberName(document.getFamilyMember() != null ? document.getFamilyMember().getFamilyMemberName() : null)
        .familyId(document.getFamily().getId())
        .uploadDate(document.getUploadDate())
        .build();
  }

  @Override
  public Document toEntity(DocumentRequest docRequest, Long familyId) {

    return Document.builder()
        .title(docRequest.getTitle())
        .filePath(docUtils.generateFilePath(docRequest, familyId))
        .fileType(docUtils.getFileType(docRequest))
        .category(docUtils.getDocumentCategory(docRequest))
        .familyMemberUserId(docUtils.getFamilyMember(familyId).getUser().getId())
        .family(docUtils.getFamily(familyId))
        .build();
  }

}
