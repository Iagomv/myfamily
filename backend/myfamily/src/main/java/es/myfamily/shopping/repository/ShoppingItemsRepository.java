package es.myfamily.shopping.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.myfamily.shopping.model.ShoppingCategory;
import es.myfamily.shopping.model.ShoppingItem;

@Repository
public interface ShoppingItemsRepository extends JpaRepository<ShoppingItem, Long> {

  @Query("SELECT si FROM ShoppingItem si WHERE si.family.id = :familyId AND si.isDeleted = false ORDER BY si.category, si.itemName")
  List<ShoppingItem> findByFamilyIdOrderByCategoryNotDeleted(@Param("familyId") Long familyId);

  @Query("SELECT si FROM ShoppingItem si LEFT JOIN FETCH si.addedBy WHERE si.id = :id")
  Optional<ShoppingItem> findById(@Param("id") Long id);

  List<ShoppingItem> findByFamilyIdAndAddedByUserId(@Param("familyId") Long familyId, @Param("addedByUserId") Long addedByUserId);

  @Query("SELECT COUNT(si) FROM ShoppingItem si WHERE si.family.id = :familyId AND EXTRACT(MONTH FROM si.boughtDate) = :month AND EXTRACT(YEAR FROM si.boughtDate) = :year")
  Integer purchasedItemsCurrentMonthCount(@Param("familyId") Long familyId, @Param("month") Integer month, @Param("year") Integer year);

  Integer countByFamilyIdAndIsPurchasedFalse(@Param("familyId") Long familyId);

  @Query("SELECT sc FROM ShoppingItem si JOIN si.category sc WHERE si.family.id = :familyId AND si.isPurchased = true AND EXTRACT(MONTH FROM si.boughtDate) = :month AND EXTRACT(YEAR FROM si.boughtDate) = :year GROUP BY sc ORDER BY COUNT(si) DESC LIMIT 1")
  ShoppingCategory findMostPurchasedCategoryCurrentMonth(@Param("familyId") Long familyId, @Param("month") Integer month,
      @Param("year") Integer year);

  Integer countByAddedByUserId(Long userId);

  Integer countByBoughtByUserId(Long userId);
}
