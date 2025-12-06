package es.myfamily.families.service;

import java.util.List;

import es.myfamily.families.model.CreateFamilyInputDto;
import es.myfamily.families.model.FamilyDto;

public interface FamilyService {

  FamilyDto createFamily(CreateFamilyInputDto dto);

  List<FamilyDto> getMyFamilies();
}