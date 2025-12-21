package es.myfamily.users.mapper;

import es.myfamily.users.model.CreateUserInputDto;
import es.myfamily.users.model.Users;
import es.myfamily.users.model.UsersDto;
import es.myfamily.users.model.CreatedUserResponse;

import org.springframework.stereotype.Component;

@Component
public class UserMapperImpl implements UserMapper {

  @Override
  public Users fromCreateUserInputDto(CreateUserInputDto dto) {
    if (dto == null) {
      return null;
    }
    Users users = new Users();
    users.setEmail(dto.getEmail());
    users.setPassword_hash(dto.getPassword());
    users.setUsername(dto.getUsername());
    return users;
  }

  @Override
  public CreateUserInputDto toCreateUserInputDto(Users entity) {
    if (entity == null) {
      return null;
    }
    CreateUserInputDto dto = new CreateUserInputDto();
    dto.setEmail(entity.getEmail());
    dto.setPassword(entity.getPassword_hash());
    return dto;
  }

  @Override
  public CreatedUserResponse toCreatedUserResponse(Users entity) {
    if (entity == null) {
      return null;
    }
    CreatedUserResponse dto = new CreatedUserResponse();
    dto.setId(entity.getId());
    dto.setEmail(entity.getEmail());
    return dto;
  }

  @Override
  public UsersDto toUsersDto(Users entity) {
    return entity == null
        ? null
        : new UsersDto(
            entity.getId(),
            entity.getUsername(),
            entity.getEmail(),
            entity.getBirthdate());
  }
}