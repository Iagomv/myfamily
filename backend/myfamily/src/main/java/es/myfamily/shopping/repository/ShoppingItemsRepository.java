package es.myfamily.shopping.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.myfamily.shopping.model.ShoppingItem;

@Repository
public interface ShoppingItemsRepository extends JpaRepository<ShoppingItem, Long> {

  @Query("SELECT si FROM ShoppingItem si WHERE si.family.id = :familyId AND si.isDeleted = false ORDER BY si.category, si.itemName")
  List<ShoppingItem> findByFamilyIdOrderByCategoryNotDeleted(@Param("familyId") Long familyId);

  @Query("SELECT si FROM ShoppingItem si LEFT JOIN FETCH si.addedBy WHERE si.id = :id")
  Optional<ShoppingItem> findById(@Param("id") Long id);
}
