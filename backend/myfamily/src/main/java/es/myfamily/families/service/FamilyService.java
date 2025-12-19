package es.myfamily.families.service;

import java.util.List;

import es.myfamily.families.model.CreateFamilyInputDto;
import es.myfamily.families.model.FamilyDashboardDto;
import es.myfamily.families.model.FamilyDto;
import es.myfamily.families.model.JoinFamilyInputDto;

public interface FamilyService {

  FamilyDto createFamily(CreateFamilyInputDto dto);

  List<FamilyDto> getMyFamilies();

  void leaveFamily(Long familyId);

  FamilyDto joinFamily(JoinFamilyInputDto dto);

  FamilyDashboardDto getFamilyDashboard(Long familyId);
}