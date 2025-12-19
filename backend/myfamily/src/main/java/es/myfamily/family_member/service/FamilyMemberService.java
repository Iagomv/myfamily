package es.myfamily.family_member.service;

import java.util.List;

import es.myfamily.family_member.model.FamilyMemberDto;

public interface FamilyMemberService {
  List<FamilyMemberDto> getFamilyMembers(Long familyId);
}
