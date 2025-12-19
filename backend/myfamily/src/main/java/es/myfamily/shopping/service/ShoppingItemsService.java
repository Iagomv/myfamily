package es.myfamily.shopping.service;

import java.util.List;

import es.myfamily.shopping.model.AddShoppingItemDto;
import es.myfamily.shopping.model.AddShoppingItemsDto;
import es.myfamily.shopping.model.GroupedItemsByCategoryDto;
import es.myfamily.shopping.model.ShoppingItemDto;
import es.myfamily.shopping.model.ShoppingItemMontlyStatsDto;

public interface ShoppingItemsService {

  List<ShoppingItemDto> getFamilyShoppingItems(Long familyId);

  List<GroupedItemsByCategoryDto> getFamilyShoppingItemsGroupedByCategory(Long familyId);

  ShoppingItemDto addShoppingItem(AddShoppingItemDto dto, Long familyId);

  void addShoppingItems(AddShoppingItemsDto dto);

  ShoppingItemDto updateShoppingItemStatus(Long itemId, Boolean isPurchased);

  ShoppingItemMontlyStatsDto getMonthlyShoppingStats(Long familyId);

  Integer countItemsCreatedByUserId(Long userId);

  Integer countItemsBoughtByUserId(Long userId);

  void deleteShoppingItem(Long itemId);
}
