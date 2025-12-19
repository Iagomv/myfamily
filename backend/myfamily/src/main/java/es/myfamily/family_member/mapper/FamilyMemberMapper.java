package es.myfamily.family_member.mapper;

import org.springframework.stereotype.Component;

import es.myfamily.family_member.model.FamilyMember;
import es.myfamily.family_member.model.FamilyMemberDto;
import es.myfamily.family_member.model.FamilyMemberId;

@Component
public class FamilyMemberMapper {

  public FamilyMemberDto toFamilyMemberDto(FamilyMember familyMember) {
    if (familyMember == null) {
      return null;
    }

    return new FamilyMemberDto(
        familyMember.getId().getFamilyId(),
        familyMember.getUser().getId(),
        familyMember.getUser().getUsername(),
        familyMember.getUser().getEmail(),
        familyMember.getFamilyMemberName(),
        familyMember.getSelectedIcon(),
        familyMember.getCreatedAt());
  }

  public FamilyMember toFamilyMember(FamilyMemberDto dto) {
    if (dto == null) {
      return null;
    }

    FamilyMember familyMember = new FamilyMember();
    FamilyMemberId compositeId = new FamilyMemberId(dto.getFamilyId(), dto.getUserId());
    familyMember.setId(compositeId);
    familyMember.setFamilyMemberName(dto.getFamilyMemberName());
    familyMember.setCreatedAt(dto.getCreatedAt());
    familyMember.setSelectedIcon(dto.getSelectedIcon());
    return familyMember;
  }
}