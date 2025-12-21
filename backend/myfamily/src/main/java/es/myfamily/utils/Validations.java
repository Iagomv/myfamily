package es.myfamily.utils;

import java.util.Calendar;
import java.util.Date;

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

  public void UserInFamily(Long familyId, Long userId) {
    boolean isMember = familyMemberRepo.existsByFamilyIdAndUserId(familyId, userId);
    if (!isMember) {
      throw new MyFamilyException(HttpStatus.FORBIDDEN, "El usuario no es miembro de la familia");
    }
  }

  public void familyExistsAndUserInFamily(Long familyId, Long userId) {
    familyExists(familyId);
    UserInFamily(familyId, userId);
  }

  public void validateUserAge(Date birthdate) {
    Calendar today = Calendar.getInstance();
    Calendar birth = Calendar.getInstance();
    birth.setTime(birthdate);

    int age = today.get(Calendar.YEAR) - birth.get(Calendar.YEAR);
    if (today.get(Calendar.MONTH) < birth.get(Calendar.MONTH) ||
        (today.get(Calendar.MONTH) == birth.get(Calendar.MONTH) && today.get(Calendar.DAY_OF_MONTH) < birth.get(Calendar.DAY_OF_MONTH))) {
      age--;
    }

    if (age < 6) {
      throw new MyFamilyException(HttpStatus.BAD_REQUEST, "User must be at least 6 years old");
    }
  }

}