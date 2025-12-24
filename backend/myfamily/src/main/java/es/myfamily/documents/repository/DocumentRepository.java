package es.myfamily.documents.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.myfamily.documents.model.Document;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

  List<Document> findAllByFamilyIdAndCategoryId(Long familyId, Long categoryId);

  List<Document> findAllByFamilyId(Long familyId);
}
