package es.myfamily.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import es.myfamily.config.security.AuthContext;
import es.myfamily.exception.MyFamilyException;
import es.myfamily.users.model.Users;
import es.myfamily.users.repository.UsersRepository;

@Component
public class SecurityUtils {

  @Autowired
  UsersRepository usersRepo;

  public Users getUserFromContext() {
    return usersRepo.findById(AuthContext.getUserId()).orElseThrow(
        () -> new MyFamilyException(HttpStatus.NOT_FOUND, "User not found"));
  }
}
