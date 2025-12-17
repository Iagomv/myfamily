package es.myfamily.families.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.myfamily.families.model.CreateFamilyInputDto;
import es.myfamily.families.model.FamilyDto;
import es.myfamily.families.model.JoinFamilyInputDto;
import es.myfamily.families.service.FamilyService;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/families")
public class FamilyController {

  @Autowired
  private FamilyService familyService;

  @GetMapping("/my")
  public ResponseEntity<List<FamilyDto>> getMyFamilies() {
    return ResponseEntity.ok(familyService.getMyFamilies());
  }

  @PostMapping("")
  public ResponseEntity<FamilyDto> createFamily(@Valid @RequestBody CreateFamilyInputDto inputDto) {
    return ResponseEntity.ok(familyService.createFamily(inputDto));
  }

  @PostMapping("/join")
  public ResponseEntity<FamilyDto> joinFamily(@Valid @RequestBody JoinFamilyInputDto dto) {
    return ResponseEntity.ok(familyService.joinFamily(dto));
  }

  @DeleteMapping("/leave/{familyId}")
  public ResponseEntity<Void> leaveFamily(@PathVariable Long familyId) {
    familyService.leaveFamily(familyId);
    return ResponseEntity.noContent().build();
  }

}