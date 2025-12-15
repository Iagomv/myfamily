package es.myfamily.families.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.myfamily.families.model.CreateFamilyInputDto;
import es.myfamily.families.model.FamilyDto;
import es.myfamily.families.service.FamilyService;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

  // @PutMapping("/leave")
  // public ResponseEntity<Void> leaveFamily(@RequestBody Long familyId) {
  // familyService.leaveFamily(familyId);
  // return ResponseEntity.noContent().build();
  // }

  // @PutMapping("")
  // public String putMethodName(@RequestBody InvitationCodeDto inputDto) {

  // return inputDto;
  // }

}