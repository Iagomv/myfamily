package es.myfamily.family_member.service;

import java.util.List;

import es.myfamily.family_member.model.FamilyMemberDto;

public interface FamilyMemberService {
  List<FamilyMemberDto> getFamilyMembers(Long familyId);

  String getFamilyMemberIcon(Long familyId, Long userId);

  Integer countFamiliesByUserId(Long userId);
}
