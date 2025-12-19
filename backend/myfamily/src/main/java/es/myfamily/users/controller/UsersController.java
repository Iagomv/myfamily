package es.myfamily.users.controller;

import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.myfamily.users.model.CreateUserInputDto;
import es.myfamily.users.model.LoginInputDto;
import es.myfamily.users.model.UserToken;
import es.myfamily.users.model.UsersDto;
import es.myfamily.users.service.UsersService;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/users")
public class UsersController {

  @Autowired
  private UsersService usersService;

  @PostMapping()
  public ResponseEntity<UsersDto> createUser(@Valid @RequestBody CreateUserInputDto inputDto) {
    return new ResponseEntity<>(usersService.createUser(inputDto), HttpStatus.CREATED);
  }

  @PostMapping("/login")
  public ResponseEntity<UserToken> loginUser(@Valid @RequestBody LoginInputDto inputDto) {
    return new ResponseEntity<>(usersService.loginUser(inputDto), HttpStatus.OK);
  }

  @GetMapping("/profile/{userId}")
  public ResponseEntity<ProfileInfoDto> getProfileInfo(@PathVariable Long userId) {
    return new ResponseEntity<>(profileInfo, HttpStatus.OK);
  }

}
