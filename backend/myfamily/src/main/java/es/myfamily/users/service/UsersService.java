package es.myfamily.users.service;

import es.myfamily.users.model.CreateUserInputDto;
import es.myfamily.users.model.LoginInputDto;
import es.myfamily.users.model.UserToken;
import es.myfamily.users.model.UsersDto;

public interface UsersService {

  UsersDto createUser(CreateUserInputDto userInputDto);

  UserToken loginUser(LoginInputDto userInputDto);
}
