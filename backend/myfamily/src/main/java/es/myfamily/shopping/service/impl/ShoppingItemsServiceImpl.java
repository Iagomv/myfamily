package es.myfamily.shopping.service.impl;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import es.myfamily.config.security.AuthContext;
import es.myfamily.exception.MyFamilyException;
import es.myfamily.families.model.Family;
import es.myfamily.families.repository.FamilyRepository;
import es.myfamily.family_member.model.FamilyMember;
import es.myfamily.family_member.repository.FamilyMemberRepository;
import es.myfamily.shopping.mapper.ShoppingItemsMapper;
import es.myfamily.shopping.model.AddShoppingItemDto;
import es.myfamily.shopping.model.AddShoppingItemsDto;
import es.myfamily.shopping.model.GroupedItemsByCategoryDto;
import es.myfamily.shopping.model.ShoppingCategory;
import es.myfamily.shopping.model.ShoppingItem;
import es.myfamily.shopping.model.ShoppingItemDto;
import es.myfamily.shopping.model.ShoppingItemMontlyStatsDto;
import es.myfamily.shopping.repository.ShoppingItemsRepository;
import es.myfamily.shopping.service.ShoppingItemsService;
import es.myfamily.utils.Validations;

@Service
public class ShoppingItemsServiceImpl implements ShoppingItemsService {

  @Autowired
  private ShoppingItemsRepository shoppingItemsRepository;

  @Autowired
  private ShoppingItemsMapper shoppingItemsMapper;

  @Autowired
  private FamilyRepository familyRepo;

  @Autowired
  private FamilyMemberRepository familyMemberRepo;

  @Autowired
  private Validations validations;

  @Override
  public List<ShoppingItemDto> getFamilyShoppingItems(Long familyId) {
    List<ShoppingItem> shoppingItems = shoppingItemsRepository.findByFamilyIdOrderByCategoryNotDeleted(familyId);

    return shoppingItems.stream()
        .map(item -> shoppingItemsMapper.toShoppingItemDto(item))
        .toList();
  }

  @Override
  public List<GroupedItemsByCategoryDto> getFamilyShoppingItemsGroupedByCategory(Long familyId) {
    var shoppingItems = shoppingItemsRepository.findByFamilyIdOrderByCategoryNotDeleted(familyId);
    return shoppingItemsMapper.toGroupedShoppingItemsDtos(shoppingItems);
  }

  @Override
  public ShoppingItemDto addShoppingItem(AddShoppingItemDto dto, Long familyId) {
    Long userId = AuthContext.getUserId();
    Family family = getFamilyFromId(familyId, userId);

    ShoppingItem shoppingItem = createShoppingItemFromDto(dto, userId, family);
    ShoppingItem savedItem = shoppingItemsRepository.save(shoppingItem);
    // Refresh the entity to load relationships
    ShoppingItem refreshedItem = shoppingItemsRepository.findById(savedItem.getId())
        .orElseThrow(() -> new MyFamilyException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve saved shopping item"));
    return shoppingItemsMapper.toShoppingItemDto(refreshedItem);
  }

  @Override
  public void addShoppingItems(AddShoppingItemsDto dto) {
    Long userId = AuthContext.getUserId();

    if (dto.getFamilyId() == null) {
      throw new MyFamilyException(HttpStatus.BAD_REQUEST, "Family ID cannot be null");
    }
    Family family = getFamilyFromId(dto.getFamilyId(), userId);

    for (AddShoppingItemDto item : dto.getItems()) {
      ShoppingItem shoppingItem = createShoppingItemFromDto(item, userId, family);
      shoppingItemsRepository.save(shoppingItem);
    }
  }

  @Override
  public ShoppingItemDto updateShoppingItemStatus(Long itemId, Boolean isPurchased) {
    ShoppingItem shoppingItem = shoppingItemsRepository.findById(itemId)
        .orElseThrow(() -> new MyFamilyException(HttpStatus.NOT_FOUND, "Shopping item not found"));

    if (Boolean.TRUE.equals(shoppingItem.getIsDeleted())) {
      throw new MyFamilyException(HttpStatus.BAD_REQUEST, "Cannot update a deleted shopping item");
    }

    shoppingItem.setIsPurchased(isPurchased);
    ShoppingItem updatedItem = shoppingItemsRepository.save(shoppingItem);
    // Refresh to ensure relationships are loaded
    ShoppingItem refreshedItem = shoppingItemsRepository.findById(updatedItem.getId())
        .orElseThrow(() -> new MyFamilyException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve updated shopping item"));
    return shoppingItemsMapper.toShoppingItemDto(refreshedItem);
  }

  @Override
  public void deleteShoppingItem(Long itemId) {
    ShoppingItem shoppingItem = shoppingItemsRepository.findById(itemId)
        .orElseThrow(() -> new MyFamilyException(HttpStatus.NOT_FOUND, "Shopping item not found"));

    if (Boolean.TRUE.equals(shoppingItem.getIsDeleted())) {
      throw new MyFamilyException(HttpStatus.BAD_REQUEST, "Shopping item is already deleted");
    }

    Long userId = AuthContext.getUserId();
    shoppingItem.setBoughtByUserId(shoppingItem.getBoughtByUserId() == null ? userId : shoppingItem.getBoughtByUserId());
    shoppingItem.setBoughtDate(shoppingItem.getBoughtDate() == null ? new Date() : shoppingItem.getBoughtDate());
    shoppingItem.setIsDeleted(true);
    shoppingItemsRepository.save(shoppingItem);
  }

  @Override
  public ShoppingItemMontlyStatsDto getMonthlyShoppingStats(Long familyId) {
    Integer month = LocalDate.now().getMonthValue();
    Integer year = LocalDate.now().getYear();
    Integer purchasedItems = shoppingItemsRepository.purchasedItemsCurrentMonthCount(familyId, month, year);
    Integer pendingItems = shoppingItemsRepository.countByFamilyIdAndIsPurchasedFalse(familyId);
    ShoppingCategory mostPurchasedCategory = shoppingItemsRepository.findMostPurchasedCategoryCurrentMonth(familyId, month, year);
    return new ShoppingItemMontlyStatsDto(purchasedItems, pendingItems, mostPurchasedCategory);
  }

  private ShoppingItem createShoppingItemFromDto(AddShoppingItemDto dto, Long userId, Family family) {
    ShoppingItem shoppingItem = shoppingItemsMapper.toEntity(dto);
    shoppingItem.setFamily(family);
    shoppingItem.setAddedByUserId(userId);
    FamilyMember familyMember = familyMemberRepo.findByFamilyIdAndUserId(family.getId(), userId);
    shoppingItem.setAddedBy(familyMember);
    return shoppingItem;
  }

  private Family getFamilyFromId(Long familyId, Long userId) {
    return familyRepo.findByIdAndUserId(familyId, userId)
        .orElseThrow(() -> new MyFamilyException(HttpStatus.FORBIDDEN,
            "The family does not exist or does not belong to the user"));
  }

}
