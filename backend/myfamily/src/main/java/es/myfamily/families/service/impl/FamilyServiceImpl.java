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
import es.myfamily.families.repository.FamilyRepository;
import es.myfamily.families.service.FamilyService;
import es.myfamily.familyMember.model.FamilyMember;
import es.myfamily.familyMember.repository.FamilyMemberRepository;
import es.myfamily.familyMember.utils.FamilyMemberUtils;
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
        null);

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
}