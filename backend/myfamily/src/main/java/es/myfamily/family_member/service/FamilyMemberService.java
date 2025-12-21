package es.myfamily.family_member.service;

import java.util.List;

import es.myfamily.family_member.model.FamilyMember;
import es.myfamily.family_member.model.FamilyMemberDto;
import es.myfamily.family_member.model.FamilyMemberIconUpdateRequest;

public interface FamilyMemberService {
  List<FamilyMemberDto> getFamilyMembers(Long familyId);

  FamilyMember getFamilyMember(Long familyId, Long userId);

  String getFamilyMemberIcon(Long familyId, Long userId);

  Integer countFamiliesByUserId(Long userId);

  void updateFamilyMemberIcon(FamilyMemberIconUpdateRequest request);
}
