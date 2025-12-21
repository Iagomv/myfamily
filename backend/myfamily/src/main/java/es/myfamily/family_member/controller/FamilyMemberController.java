package es.myfamily.family_member.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.myfamily.family_member.model.FamilyMemberIconUpdateRequest;
import es.myfamily.family_member.service.FamilyMemberService;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/family-members")
public class FamilyMemberController {

  @Autowired
  private FamilyMemberService fmService;

  @PatchMapping("icon-update")
  public ResponseEntity<Void> updateFamilyMemberIcon(@Valid @RequestBody FamilyMemberIconUpdateRequest request) {
    fmService.updateFamilyMemberIcon(request);
    return ResponseEntity.noContent().build();
  }
}
