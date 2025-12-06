package es.myfamily.shopping.mapper;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.myfamily.shopping.model.AddShoppingItemDto;
import es.myfamily.shopping.model.AddShoppingItemsDto;
import es.myfamily.shopping.model.GroupedItemsByCategoryDto;
import es.myfamily.shopping.model.ShoppingCategory;
import es.myfamily.shopping.model.ShoppingItem;
import es.myfamily.shopping.model.ShoppingItemDto;
import es.myfamily.shopping.repository.ShoppingCategoryRepository;

@Component
public class ShoppingItemsMapper {

  @Autowired
  ShoppingCategoryRepository categoryRepo;

  /**
   * Maps ShoppingItem entity to ShoppingItemDto
   * 
   * @param shoppingItems
   * @return
   */
  public ShoppingItemDto toShoppingItemDto(ShoppingItem shoppingItems) {
    if (shoppingItems == null) {
      return null;
    }

    ShoppingItemDto dto = new ShoppingItemDto();
    dto.setId(shoppingItems.getId());
    dto.setFamilyId(shoppingItems.getFamily().getId());
    dto.setItemName(shoppingItems.getItemName());
    dto.setQuantity(shoppingItems.getQuantity());

    // Map ShoppingCategory to DTO
    if (shoppingItems.getCategory() != null) {
      dto.setCategoryId(shoppingItems.getCategory().getId());
      dto.setCategoryName(shoppingItems.getCategory().getName());
    }

    dto.setIsPurchased(shoppingItems.getIsPurchased());

    // Map FamilyMember to DTO
    if (shoppingItems.getAddedBy() != null) {
      dto.setAddedByFamilyMemberId(shoppingItems.getAddedByUserId());
      dto.setAddedByFamilyMemberName(shoppingItems.getAddedBy().getFamilyMemberName());
    }

    // Map added date
    dto.setAddedDate(shoppingItems.getAddedDate());

    return dto;
  }

  /**
   * Maps list of ShoppingItem entities to list of GroupedItemsByCategoryDto
   * 
   * @param shoppingItems
   * @return
   */
  public List<GroupedItemsByCategoryDto> toGroupedShoppingItemsDtos(List<ShoppingItem> shoppingItems) {
    // Group items by category, maintaining order
    Map<ShoppingCategory, List<ShoppingItemDto>> groupedMap = shoppingItems.stream()
        .collect(Collectors.groupingBy(
            ShoppingItem::getCategory,
            LinkedHashMap::new,
            Collectors.mapping(this::toShoppingItemDto, Collectors.toList())));

    // Convert to list of GroupedItemsByCategoryDto
    return groupedMap.entrySet().stream()
        .map(entry -> new GroupedItemsByCategoryDto(entry.getKey(), entry.getValue()))
        .collect(Collectors.toList());
  }

  /**
   * Maps AddShoppingItemsDto to list of ShoppingItem entities
   * 
   * @param dto
   * @return
   */
  public List<ShoppingItem> toEntity(AddShoppingItemsDto dto) {
    if (dto == null) {
      return null;
    }

    return dto.getItems().stream()
        .map(this::toEntity)
        .collect(Collectors.toList());
  }

  /**
   * Maps AddShoppingItemDto to ShoppingItem entity
   * 
   * @param dto
   * @return
   */
  public ShoppingItem toEntity(AddShoppingItemDto dto) {
    if (dto == null) {
      return null;
    }

    ShoppingItem shoppingItem = new ShoppingItem();
    shoppingItem.setItemName(dto.getName());
    shoppingItem.setQuantity(dto.getQuantity());

    // Fetch and set category
    shoppingItem.setCategory(categoryRepo.findById(dto.getCategoryId()).orElse(null));

    // Default values
    shoppingItem.setIsPurchased(false);

    return shoppingItem;
  }
}