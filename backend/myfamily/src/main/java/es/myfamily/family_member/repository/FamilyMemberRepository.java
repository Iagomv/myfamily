package es.myfamily.family_member.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.myfamily.family_member.model.FamilyMember;
import es.myfamily.family_member.model.FamilyMemberId;

@Repository
public interface FamilyMemberRepository extends JpaRepository<FamilyMember, FamilyMemberId> {

  @Query("SELECT COUNT(DISTINCT fm.family.id) FROM FamilyMember fm WHERE fm.user.id = :userId")
  long countFamiliesByUserId(@Param("userId") Long userId);

  @Query("SELECT fm FROM FamilyMember fm WHERE fm.family.id = :familyId AND fm.user.id = :userId")
  FamilyMember findByFamilyIdAndUserId(@Param("familyId") Long familyId, @Param("userId") Long userId);

  @Query("SELECT fm FROM FamilyMember fm WHERE fm.user.id = :userId")
  List<FamilyMember> findAllByUserId(@Param("userId") Long userId);

  @Query("SELECT COUNT(fm) FROM FamilyMember fm WHERE fm.family.id = :familyId")
  long countByFamilyId(@Param("familyId") Long familyId);

  List<FamilyMember> findAllByFamilyId(Long familyId);

  @Query("SELECT fm.selectedIcon FROM FamilyMember fm WHERE fm.family.id = :familyId AND fm.user.id = :userId")
  String findSelectedIconByFamilyIdAndUserId(@Param("familyId") Long familyId, @Param("userId") Long userId);

  boolean existsByFamilyIdAndUserId(Long familyId, Long userId);

  Integer countByUserId(Long userId);
}