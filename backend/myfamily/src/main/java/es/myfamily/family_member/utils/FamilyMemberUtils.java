package es.myfamily.family_member.utils;

import es.myfamily.families.model.Family;
import es.myfamily.family_member.model.FamilyMember;
import es.myfamily.family_member.model.FamilyMemberId;
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
