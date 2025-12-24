package es.myfamily.documents.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.myfamily.documents.model.DocumentCategory;
import java.util.List;

@Repository
public interface DocumentCategoryRepository extends JpaRepository<DocumentCategory, Long> {
  List<DocumentCategory> findAllByFamilyId(Long familyId);

  boolean existsByNameAndFamilyId(String name, Long familyId);

}
