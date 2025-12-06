package es.myfamily.shopping.service;

import java.util.List;

import es.myfamily.shopping.model.AddShoppingItemDto;
import es.myfamily.shopping.model.AddShoppingItemsDto;
import es.myfamily.shopping.model.GroupedItemsByCategoryDto;
import es.myfamily.shopping.model.ShoppingItemDto;

public interface ShoppingItemsService {

  List<ShoppingItemDto> getFamilyShoppingItems();

  List<GroupedItemsByCategoryDto> getFamilyShoppingItemsGroupedByCategory(Long familyId);

  ShoppingItemDto addShoppingItem(AddShoppingItemDto dto, Long familyId);

  void addShoppingItems(AddShoppingItemsDto dto);

  ShoppingItemDto updateShoppingItemStatus(Long itemId, Boolean isPurchased);

  void deleteShoppingItem(Long itemId);
}
