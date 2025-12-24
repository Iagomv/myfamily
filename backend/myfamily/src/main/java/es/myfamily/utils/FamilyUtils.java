package es.myfamily.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import es.myfamily.exception.MyFamilyException;
import es.myfamily.families.model.Family;
import es.myfamily.families.repository.FamilyRepository;

@Component
public class FamilyUtils {

  @Autowired
  private FamilyRepository familyRepo;

  public Family getFamilyByIdIfExists(Long familyId) {
    return familyRepo.findById(familyId).orElseThrow(
        () -> new MyFamilyException(HttpStatus.NOT_FOUND, "La familia no existe"));
  }
}
