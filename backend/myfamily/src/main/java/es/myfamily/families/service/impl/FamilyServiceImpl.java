package es.myfamily.families.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.myfamily.config.security.AuthContext;
import es.myfamily.exception.MyFamilyException;
import es.myfamily.families.mapper.FamilyMapper;
import es.myfamily.families.model.CreateFamilyInputDto;
import es.myfamily.families.model.Family;
import es.myfamily.families.model.FamilyDto;
import es.myfamily.families.model.JoinFamilyInputDto;
import es.myfamily.families.repository.FamilyRepository;
import es.myfamily.families.service.FamilyService;
import es.myfamily.family_member.model.FamilyMember;
import es.myfamily.family_member.model.FamilyMemberId;
import es.myfamily.family_member.repository.FamilyMemberRepository;
import es.myfamily.family_member.utils.FamilyMemberUtils;
import es.myfamily.shopping.model.ShoppingItem;
import es.myfamily.shopping.repository.ShoppingItemsRepository;
import es.myfamily.users.model.Users;
import es.myfamily.users.repository.UsersRepository;
import es.myfamily.utils.InvitationCodeUtils;
import es.myfamily.utils.Validations;

@Service
public class FamilyServiceImpl implements FamilyService {
  @Autowired
  FamilyMapper familyMapper;

  @Autowired
  FamilyRepository familyRepo;

  @Autowired
  UsersRepository usersRepo;

  @Autowired
  FamilyMemberRepository familyMemberRepo;

  @Autowired
  ShoppingItemsRepository shoppingItemsRepo;

  @Autowired
  private Validations validations;

  @Override
  @Transactional
  public FamilyDto createFamily(CreateFamilyInputDto dto) {
    validations.maxFamiliesPerUser();

    Users user = usersRepo.findById(AuthContext.getUserId()).orElseThrow(
        () -> new MyFamilyException(HttpStatus.NOT_FOUND, "User not found"));

    Family newFamily = new Family(
        null,
        dto.getFamilyName(),
        InvitationCodeUtils.generateInvitationCode(),
        null,
        null, null);

    Family savedFamily = familyRepo.save(newFamily);

    // Create and save family member relationship
    FamilyMember familyMember = FamilyMemberUtils.createFamilyMember(savedFamily, user, user.getUsername());
    familyMemberRepo.save(familyMember);

    return familyMapper.toFamilyDto(savedFamily);
  }

  @Override
  @Transactional
  public List<FamilyDto> getMyFamilies() {
    List<Family> families = familyRepo.findAllByUserId(AuthContext.getUserId());
    return families.stream().map(familyMapper::toFamilyDto).toList();
  }

  @Override
  @Transactional
  public void leaveFamily(Long familyId) {
    Long userId = AuthContext.getUserId();

    // Verify the family exists
    familyRepo.findById(familyId)
        .orElseThrow(() -> new MyFamilyException(HttpStatus.NOT_FOUND, "Family not found"));

    // Verify user is a member
    FamilyMemberId memberId = new FamilyMemberId(familyId, userId);
    if (!familyMemberRepo.existsById(memberId)) {
      throw new MyFamilyException(HttpStatus.BAD_REQUEST, "User is not a member of this family");
    }

    // Clear shopping items attribution for this user in this family
    List<ShoppingItem> userItems = shoppingItemsRepo.findByFamilyIdAndAddedByUserId(familyId, userId);
    for (ShoppingItem item : userItems) {
      item.setAddedByUserId(null);
      item.setAddedBy(null);
    }
    if (!userItems.isEmpty()) {
      shoppingItemsRepo.saveAll(userItems);
    }

    // Delete the FamilyMember using composite key
    familyMemberRepo.deleteById(memberId);

    // If no members left, delete the family
    if (familyMemberRepo.countByFamilyId(familyId) == 0) {
      familyRepo.deleteById(familyId);
    }
  }

  @Override
  @Transactional
  public FamilyDto joinFamily(JoinFamilyInputDto dto) {
    validations.maxFamiliesPerUser();

    Users user = usersRepo.findById(AuthContext.getUserId()).orElseThrow(
        () -> new MyFamilyException(HttpStatus.NOT_FOUND, "User not found"));

    Family family = familyRepo.findByInvitationCode(dto.getInvitationCode()).orElseThrow(
        () -> new MyFamilyException(HttpStatus.NOT_FOUND, "Invalid invitation code"));

    // Check if user is already a member
    FamilyMemberId memberId = new FamilyMemberId(family.getId(), user.getId());
    if (familyMemberRepo.existsById(memberId)) {
      throw new MyFamilyException(HttpStatus.BAD_REQUEST, "User is already a member of this family");
    }

    // Create and save family member relationship
    FamilyMember familyMember = FamilyMemberUtils.createFamilyMember(family, user, user.getUsername());
    familyMemberRepo.save(familyMember);

    return familyMapper.toFamilyDto(family);
  }
}