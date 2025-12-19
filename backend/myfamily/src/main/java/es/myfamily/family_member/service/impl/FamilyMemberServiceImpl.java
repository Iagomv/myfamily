package es.myfamily.family_member.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.myfamily.family_member.mapper.FamilyMemberMapper;
import es.myfamily.family_member.model.FamilyMemberDto;
import es.myfamily.family_member.repository.FamilyMemberRepository;
import es.myfamily.family_member.service.FamilyMemberService;

@Service
public class FamilyMemberServiceImpl implements FamilyMemberService {

  @Autowired
  private FamilyMemberRepository fmRepo;

  @Autowired
  private FamilyMemberMapper fmMapper;

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

}
