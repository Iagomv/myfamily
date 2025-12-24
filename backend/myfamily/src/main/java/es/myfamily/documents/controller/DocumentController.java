package es.myfamily.documents.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.myfamily.documents.model.DocumentDtoResponse;
import es.myfamily.documents.model.DocumentRequest;
import es.myfamily.documents.service.DocumentService;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/documents")
public class DocumentController {
  @Autowired
  private DocumentService docService;

  @GetMapping("/{familyId}")
  public ResponseEntity<List<DocumentDtoResponse>> getDocumentsByFamily(@PathVariable Long familyId) {
    return new ResponseEntity<>(docService.getDocumentsByFamily(familyId), HttpStatus.OK);

  }

  @GetMapping("/{familyId}/{categoryId}")
  public ResponseEntity<List<DocumentDtoResponse>> getDocumentsByFamilyAndCategory(@PathVariable Long familyId, @PathVariable Long categoryId) {
    return new ResponseEntity<>(docService.getDocumentsByFamilyAndCategory(familyId, categoryId), HttpStatus.OK);
  }

  @PostMapping("/{familyId}")
  public ResponseEntity<DocumentDtoResponse> addFamilyDocument(@PathVariable Long familyId, @Valid @RequestBody DocumentRequest doc) {
    return new ResponseEntity<>(docService.addDocument(familyId, doc), HttpStatus.OK);
  }

  @GetMapping("/{documentId}/download")
  public ResponseEntity<Resource> downloadDocument(@PathVariable Long documentId) {
    Resource res = docService.loadDocumentResource(documentId);
    DocumentDtoResponse meta = docService.getDocumentById(documentId);
    String filename = meta.getTitle();
    return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType(meta.getFileType()))
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
        .body(res);
  }

}
