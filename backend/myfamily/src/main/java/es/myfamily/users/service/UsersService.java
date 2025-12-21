package es.myfamily.users.service;

import es.myfamily.users.model.CreateUserInputDto;
import es.myfamily.users.model.LoginInputDto;
import es.myfamily.users.model.PasswordUpdateRequest;
import es.myfamily.users.model.ProfileInfoDto;
import es.myfamily.users.model.UserToken;
import es.myfamily.users.model.UserUpdateRequest;
import es.myfamily.users.model.UsersDto;
import es.myfamily.users.model.CreatedUserResponse;

public interface UsersService {

  CreatedUserResponse createUser(CreateUserInputDto userInputDto);

  UserToken loginUser(LoginInputDto userInputDto);

  ProfileInfoDto getProfileInfo(Long familyId);

  void updatePassword(Long userId, PasswordUpdateRequest request);

  UsersDto updateUser(Long familyId, UserUpdateRequest request);
}
