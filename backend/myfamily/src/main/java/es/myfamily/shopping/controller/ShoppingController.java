package es.myfamily.shopping.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;

import es.myfamily.shopping.model.AddShoppingItemDto;
import es.myfamily.shopping.model.AddShoppingItemsDto;
import es.myfamily.shopping.model.GroupedItemsByCategoryDto;
import es.myfamily.shopping.model.ShoppingCategory;
import es.myfamily.shopping.model.ShoppingItemDto;
import es.myfamily.shopping.repository.ShoppingCategoryRepository;
import es.myfamily.shopping.service.ShoppingItemsService;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/shopping")
public class ShoppingController {

  @Autowired
  private ShoppingItemsService shoppingItemsService;

  @Autowired
  private ShoppingCategoryRepository shoppingCategoryRepository;

  @GetMapping("/categories")
  public ResponseEntity<List<ShoppingCategory>> getShoppingCategories() {
    return ResponseEntity.ok(shoppingCategoryRepository.findAll());
  }

  @GetMapping("/family/{familyId}/items")
  public ResponseEntity<List<GroupedItemsByCategoryDto>> getFamilyShoppingItemsGrouped(
      @PathVariable Long familyId) {
    return ResponseEntity.ok(shoppingItemsService.getFamilyShoppingItemsGroupedByCategory(familyId));
  }

  @PostMapping("/shopping-item/{familyId}")
  public ResponseEntity<ShoppingItemDto> addShoppingItem(@Valid @RequestBody AddShoppingItemDto dto, @PathVariable Long familyId) {

    return ResponseEntity.ok(shoppingItemsService.addShoppingItem(dto, familyId));
  }

  @PutMapping("/shopping-item/{itemId}")
  public ResponseEntity<ShoppingItemDto> updateShoppingItem(@Valid @RequestBody AddShoppingItemDto entity, @PathVariable Long itemId) {
    return new ResponseEntity<>(shoppingItemsService.updateShoppingItem(itemId, entity), HttpStatus.OK);
  }

  @PostMapping("/shopping-items/{familyId}")
  public ResponseEntity<Void> addShoppingItems(@Valid @RequestBody AddShoppingItemsDto dto, @PathVariable Long familyId) {
    dto.setFamilyId(familyId);
    shoppingItemsService.addShoppingItems(dto);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @PatchMapping("/shopping-item/{itemId}/status")
  public ResponseEntity<ShoppingItemDto> updateShoppingItemStatus(
      @PathVariable Long itemId,
      @RequestParam Boolean isPurchased) {
    return ResponseEntity.ok(shoppingItemsService.updateShoppingItemStatus(itemId, isPurchased));
  }

  @DeleteMapping("/shopping-item/{itemId}")
  public ResponseEntity<Void> deleteShoppingItem(@PathVariable Long itemId) {
    shoppingItemsService.deleteShoppingItem(itemId);
    return ResponseEntity.noContent().build();
  }

}
