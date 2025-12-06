package es.myfamily.families.mapper;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.myfamily.families.model.Family;
import es.myfamily.families.model.FamilyDto;
import es.myfamily.familyMember.mapper.FamilyMemberMapper;

@Component
public class FamilyMapper {

  @Autowired
  private FamilyMemberMapper familyMemberMapper;

  public FamilyDto toFamilyDto(Family family) {
    return new FamilyDto(
        family.getId(),
        family.getFamilyName(),
        family.getInvitationCode(),
        family.getCreatedAt(),
        family.getFamilyMembers() == null ? List.of()
            : family.getFamilyMembers().stream()
                .map(familyMemberMapper::toFamilyMemberDto)
                .toList());
  }

}
