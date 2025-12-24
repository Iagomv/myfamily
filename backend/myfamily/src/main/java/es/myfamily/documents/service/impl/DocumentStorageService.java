package es.myfamily.documents.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import es.myfamily.documents.model.DocumentRequest;
import es.myfamily.exception.MyFamilyException;

@Service
public class DocumentStorageService {

  @Value("${app.upload.dir:../uploads}")
  private String uploadDir;

  public long storeFile(DocumentRequest docRequest, String filePath) {
    if (docRequest == null || docRequest.getFileData() == null) {
      throw new MyFamilyException(HttpStatus.BAD_REQUEST, "No file data provided");
    }

    try {
      byte[] bytes = toPrimitive(docRequest.getFileData());
      String relative = (filePath == null) ? "" : filePath.replaceFirst("^/", "");
      Path base = Paths.get(uploadDir).toAbsolutePath().normalize();
      Path target = base.resolve(relative).normalize();
      // Prevent path traversal
      if (!target.startsWith(base)) {
        throw new MyFamilyException(HttpStatus.BAD_REQUEST, "Invalid file path");
      }
      Files.createDirectories(target.getParent());
      Files.write(target, bytes, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
      return Files.size(target);
    } catch (IOException e) {
      throw new MyFamilyException(HttpStatus.INTERNAL_SERVER_ERROR, "Error storing file: " + e.getMessage());
    }
  }

  public org.springframework.core.io.Resource loadAsResource(String filePath) {
    try {
      String relative = (filePath == null) ? "" : filePath.replaceFirst("^/", "");
      Path base = Paths.get(uploadDir).toAbsolutePath().normalize();
      Path target = base.resolve(relative).normalize();
      if (!target.startsWith(base)) {
        throw new MyFamilyException(HttpStatus.BAD_REQUEST, "Invalid file path");
      }
      if (!Files.exists(target)) {
        throw new MyFamilyException(HttpStatus.NOT_FOUND, "File not found");
      }
      return new org.springframework.core.io.UrlResource(target.toUri());
    } catch (IOException e) {
      throw new MyFamilyException(HttpStatus.INTERNAL_SERVER_ERROR, "Error loading file: " + e.getMessage());
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
