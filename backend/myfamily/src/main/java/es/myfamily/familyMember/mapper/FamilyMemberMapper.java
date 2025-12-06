package es.myfamily.familyMember.mapper;

import org.springframework.stereotype.Component;

import es.myfamily.familyMember.model.FamilyMember;
import es.myfamily.familyMember.model.FamilyMemberId;
import es.myfamily.familyMember.model.FamilyMemberDto;

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
    return familyMember;
  }
}