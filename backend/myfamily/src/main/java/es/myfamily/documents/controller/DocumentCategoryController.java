package es.myfamily.documents.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.myfamily.documents.model.DocumentCategoryDto;
import es.myfamily.documents.model.DocumentCategoryRequestDto;
import es.myfamily.documents.service.DocumentCategoryService;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/document-categories")
public class DocumentCategoryController {

  @Autowired
  private DocumentCategoryService documentCategoryService;

  @GetMapping("/{familyId}")
  public ResponseEntity<List<DocumentCategoryDto>> getDocumentCategories(@PathVariable Long familyId) {
    return new ResponseEntity<>(documentCategoryService.getDocumentCategoriesByFamilyId(familyId), HttpStatus.OK);
  }

  @PostMapping("/{familyId}")
  public ResponseEntity<DocumentCategoryDto> createDocumentCategory(@Valid @RequestBody DocumentCategoryRequestDto dto, @PathVariable Long familyId) {
    return new ResponseEntity<>(documentCategoryService.createDocumentCategory(dto, familyId), HttpStatus.CREATED);
  }

  @DeleteMapping("/{familyId}/{categoryId}")
  public ResponseEntity<Void> deleteDocumentCategory(@PathVariable Long familyId, @PathVariable Long categoryId) {
    documentCategoryService.deleteDocumentCategory(familyId, categoryId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

}
