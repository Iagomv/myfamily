package es.myfamily.users.mapper;

import es.myfamily.users.model.CreateUserInputDto;
import es.myfamily.users.model.Users;
import es.myfamily.users.model.UsersDto;
import es.myfamily.users.model.CreatedUserResponse;

public interface UserMapper {

  Users fromCreateUserInputDto(CreateUserInputDto dto);

  CreateUserInputDto toCreateUserInputDto(Users entity);

  CreatedUserResponse toCreatedUserResponse(Users entity);

  UsersDto toUsersDto(Users entity);
}
