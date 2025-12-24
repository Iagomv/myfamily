package es.myfamily.family_member.utils;

import java.util.Optional;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.time.Instant;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import es.myfamily.documents.model.DocumentCategory;
import es.myfamily.documents.model.DocumentRequest;
import es.myfamily.documents.repository.DocumentCategoryRepository;
import es.myfamily.exception.MyFamilyException;
import es.myfamily.families.model.Family;
import es.myfamily.family_member.model.FamilyMember;
import es.myfamily.family_member.repository.FamilyMemberRepository;
import es.myfamily.utils.FamilyUtils;
import es.myfamily.utils.SecurityUtils;
import es.myfamily.utils.Validations;

@Component
public class DocumentUtils {

  @Autowired
  private DocumentCategoryRepository docCategoryRepo;
  @Autowired
  private Validations validations;
  @Autowired
  private FamilyMemberRepository fmRepo;
  @Autowired
  private SecurityUtils securityUtils;
  @Autowired
  private FamilyUtils familyUtils;

  public String generateFilePath(DocumentRequest docRequest, Long familyId) {

    String title = docRequest.getTitle().replaceAll("[^a-z0-9_\\-]", "_").toLowerCase();
    String contentType = getFileType(docRequest);
    String ext = extensionFromContentType(contentType);
    long ts = Instant.now().toEpochMilli();
    String filename = String.format("%s-%d.%s", title, ts, ext);
    return String.format("/uploads/%d/%s", familyId, filename);
  }

  public String getFileType(DocumentRequest docRequest) {
    if (docRequest == null || docRequest.getFileData() == null) {
      return "application/octet-stream";
    }
    byte[] bytes = toPrimitive(docRequest.getFileData());
    try (InputStream is = new ByteArrayInputStream(bytes)) {
      String guess = URLConnection.guessContentTypeFromStream(is);
      return guess != null ? guess : "application/octet-stream";
    } catch (IOException e) {
      return "application/octet-stream";
    }
  }

  public Long getFileSize(DocumentRequest docRequest) {
    if (docRequest == null || docRequest.getFileData() == null) {
      return 0L;
    }
    return (docRequest == null || docRequest.getFileData() == null) ? 0L : (long) docRequest.getFileData().length;
  }

  public DocumentCategory getDocumentCategory(DocumentRequest docRequest) {
    Long categoryId = docRequest.getCategoryId();
    return docCategoryRepo.findById(categoryId).orElseThrow(
        () -> new RuntimeException("No se ha encontrado la categoría del documento: " + categoryId));
  }

  public FamilyMember getFamilyMember(Long familyId) {
    Long userId = securityUtils.getUserFromContext().getId();
    validations.UserInFamily(familyId, userId);

    FamilyMember member = fmRepo.findByFamilyIdAndUserId(familyId, userId);
    if (member == null) {
      throw new MyFamilyException(HttpStatus.NOT_FOUND,
          "No se ha encontrado el miembro de la familia para el usuario: " + userId);
    }
    return member;
  }

  public Family getFamily(Long familyId) {
    return familyUtils.getFamilyByIdIfExists(familyId);
  }

  private String extensionFromContentType(String contentType) {
    if (contentType == null)
      return "bin";
    switch (contentType) {
      case "application/pdf":
        return "pdf";
      case "image/png":
        return "png";
      case "image/jpeg":
        return "jpg";
      case "image/gif":
        return "gif";
      case "text/plain":
        return "txt";
      case "application/zip":
        return "zip";
      default:
        if (contentType.contains("/")) {
          String subtype = contentType.substring(contentType.indexOf('/') + 1);
          return subtype.replaceAll("[^a-zA-Z0-9]", "");
        }
        return "bin";
    }
  }

  private byte[] toPrimitive(Byte[] bytes) {
    if (bytes == null)
      return new byte[0];
    byte[] primitive = new byte[bytes.length];
    for (int i = 0; i < bytes.length; i++) {
      primitive[i] = bytes[i];
    }
    return primitive;
  }

}
