package es.myfamily.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import es.myfamily.config.security.AuthContext;
import es.myfamily.exception.MyFamilyException;
import es.myfamily.families.repository.FamilyRepository;
import es.myfamily.family_member.repository.FamilyMemberRepository;

@Component
public class Validations {

  @Autowired
  private FamilyMemberRepository familyMemberRepo;

  @Autowired
  private FamilyRepository familyRepo;

  @Value("${max.families.per.user}")
  private int maxFamiliesPerUser;

  public void maxFamiliesPerUser() {
    long familyCount = familyMemberRepo.countFamiliesByUserId(AuthContext.getUserId());
    if (familyCount >= maxFamiliesPerUser) {
      throw new MyFamilyException(HttpStatus.BAD_REQUEST, "El usuario ha alcanzado el número máximo de familias");
    }
  }

  public void familyExists(Long familyId) {
    boolean exists = familyRepo.existsById(familyId);
    if (!exists) {
      throw new MyFamilyException(HttpStatus.NOT_FOUND, "La familia no existe");
    }
  }

}