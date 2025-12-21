package es.myfamily.users.controller;

import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.myfamily.users.model.CreateUserInputDto;
import es.myfamily.users.model.LoginInputDto;
import es.myfamily.users.model.PasswordUpdateRequest;
import es.myfamily.users.model.ProfileInfoDto;
import es.myfamily.users.model.UserToken;
import es.myfamily.users.model.UserUpdateRequest;
import es.myfamily.users.model.UsersDto;
import es.myfamily.users.model.CreatedUserResponse;
import es.myfamily.users.service.UsersService;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/users")
public class UsersController {

  @Autowired
  private UsersService usersService;

  @PostMapping()
  public ResponseEntity<CreatedUserResponse> createUser(@Valid @RequestBody CreateUserInputDto inputDto) {
    return new ResponseEntity<>(usersService.createUser(inputDto), HttpStatus.CREATED);
  }

  @PostMapping("/login")
  public ResponseEntity<UserToken> loginUser(@Valid @RequestBody LoginInputDto inputDto) {
    return new ResponseEntity<>(usersService.loginUser(inputDto), HttpStatus.OK);
  }

  @GetMapping("/profile/{familyId}")
  public ResponseEntity<ProfileInfoDto> getProfileInfo(@PathVariable Long familyId) {
    return new ResponseEntity<>(usersService.getProfileInfo(familyId), HttpStatus.OK);
  }

  @PatchMapping("/password/{userId}")
  public ResponseEntity<Void> updatePassword(@PathVariable Long userId, @Valid @RequestBody PasswordUpdateRequest request) {
    usersService.updatePassword(userId, request);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @PutMapping("/update/{familyId}")
  public ResponseEntity<UsersDto> updateUser(@PathVariable Long familyId,
      @Valid @RequestBody UserUpdateRequest request) {

    return new ResponseEntity<>(usersService.updateUser(familyId, request), HttpStatus.OK);
  }

}
