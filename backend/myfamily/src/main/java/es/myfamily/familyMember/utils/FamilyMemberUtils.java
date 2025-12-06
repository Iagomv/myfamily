package es.myfamily.familyMember.utils;

import es.myfamily.familyMember.model.FamilyMember;
import es.myfamily.familyMember.model.FamilyMemberId;
import es.myfamily.families.model.Family;
import es.myfamily.users.model.Users;

public class FamilyMemberUtils {

  public static FamilyMember createFamilyMember(Family family, Users user, String memberName) {
    FamilyMemberId id = new FamilyMemberId();
    id.setFamilyId(family.getId());
    id.setUserId(user.getId());

    FamilyMember member = new FamilyMember();
    member.setId(id);
    member.setFamily(family);
    member.setUser(user);
    member.setFamilyMemberName(memberName);

    return member;
  }
}
