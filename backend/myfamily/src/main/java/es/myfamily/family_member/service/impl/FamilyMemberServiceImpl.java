package es.myfamily.family_member.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.myfamily.family_member.mapper.FamilyMemberMapper;
import es.myfamily.family_member.model.FamilyMember;
import es.myfamily.family_member.model.FamilyMemberDto;
import es.myfamily.family_member.model.FamilyMemberIconUpdateRequest;
import es.myfamily.family_member.repository.FamilyMemberRepository;
import es.myfamily.family_member.service.FamilyMemberService;
import es.myfamily.users.model.Users;
import es.myfamily.utils.SecurityUtils;
import es.myfamily.utils.Validations;
import lombok.val;

@Service
public class FamilyMemberServiceImpl implements FamilyMemberService {

  @Autowired
  private FamilyMemberRepository fmRepo;

  @Autowired
  private FamilyMemberMapper fmMapper;

  @Autowired
  private Validations validations;

  @Autowired
  private SecurityUtils securityUtils;

  @Override
  public List<FamilyMemberDto> getFamilyMembers(Long familyId) {
    return fmRepo.findAllByFamilyId(familyId).stream().map(fmMapper::toFamilyMemberDto).toList();
  }

  @Override
  public String getFamilyMemberIcon(Long familyId, Long userId) {
    return fmRepo.findSelectedIconByFamilyIdAndUserId(familyId, userId);
  }

  @Override
  public Integer countFamiliesByUserId(Long userId) {
    return fmRepo.countByUserId(userId);
  }

  @Override
  public FamilyMember getFamilyMember(Long familyId, Long userId) {
    return fmRepo.findByFamilyIdAndUserId(familyId, userId);
  }

  @Override
  public void updateFamilyMemberIcon(FamilyMemberIconUpdateRequest request) {
    if (securityUtils.getUserFromContext().getId().equals(request.getUserId()) == false) {
      throw new SecurityException("You can only update your own icon");
    }
    validations.familyExistsAndUserInFamily(request.getFamilyId(), request.getUserId());
    FamilyMember familyMember = fmRepo.findByFamilyIdAndUserId(request.getFamilyId(), request.getUserId());
    familyMember.setSelectedIcon(request.getNewIconName());
    fmRepo.save(familyMember);
  }

}
