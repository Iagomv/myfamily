package es.myfamily.users.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import org.springframework.security.crypto.password.PasswordEncoder;

import es.myfamily.config.JwtTokenUtil;
import es.myfamily.exception.MyFamilyException;
import es.myfamily.users.mapper.UserMapper;
import es.myfamily.users.model.CreateUserInputDto;
import es.myfamily.users.model.LoginInputDto;
import es.myfamily.users.model.UserToken;
import es.myfamily.users.model.Users;
import es.myfamily.users.model.UsersDto;
import es.myfamily.users.repository.UsersRepository;
import es.myfamily.users.service.UsersService;

@Service
public class UsersServiceImpl implements UsersService {
  @Autowired
  private UsersRepository usersRepository;
  @Autowired
  private UserMapper userMapper;
  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  private JwtTokenUtil jwtTokenUtil;

  @Override
  public UsersDto createUser(CreateUserInputDto userInputDto) {
    if (usersRepository.findByEmail(userInputDto.getEmail()).isPresent()) {
      throw new MyFamilyException(HttpStatus.BAD_REQUEST, "Email already in use");
    }

    Users user = userMapper.fromCreateUserInputDto(userInputDto);
    user.setPassword_hash(passwordEncoder.encode(user.getPassword_hash()));

    return userMapper.toUsersDto(usersRepository.save(user));
  }

  @Override
  public UserToken loginUser(LoginInputDto loginDto) {

    Users user = usersRepository.findByEmail(loginDto.getEmail())
        .orElseThrow(() -> new MyFamilyException(HttpStatus.UNAUTHORIZED, "Invalid email or password"));

    if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword_hash())) {
      throw new MyFamilyException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
    }

    // Generate and return JWT token
    return new UserToken(jwtTokenUtil.generateToken(user));
  }
}
