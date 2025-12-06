package es.myfamily.families.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.myfamily.families.model.Family;

@Repository
public interface FamilyRepository extends JpaRepository<Family, Long> {

  @Query("SELECT DISTINCT f FROM Family f JOIN f.familyMembers fm WHERE fm.user.id = :userId")
  List<Family> findAllByUserId(@Param("userId") Long userId);

  @Query("SELECT f FROM Family f JOIN f.familyMembers fm WHERE f.id = :familyId AND fm.user.id = :userId")
  Optional<Family> findByIdAndUserId(@Param("familyId") Long familyId, @Param("userId") Long userId);
}
