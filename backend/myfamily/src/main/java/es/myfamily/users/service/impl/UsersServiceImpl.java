package es.myfamily.users.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Calendar;

import es.myfamily.calendar_events.model.CalendarEvent;
import es.myfamily.calendar_events.service.CalendarEventsService;
import es.myfamily.config.JwtTokenUtil;
import es.myfamily.exception.MyFamilyException;
import es.myfamily.family_member.model.FamilyMember;
import es.myfamily.family_member.repository.FamilyMemberRepository;
import es.myfamily.family_member.service.FamilyMemberService;
import es.myfamily.shopping.service.ShoppingItemsService;
import es.myfamily.users.mapper.UserMapper;
import es.myfamily.users.model.CreateUserInputDto;
import es.myfamily.users.model.LoginInputDto;
import es.myfamily.users.model.PasswordUpdateRequest;
import es.myfamily.users.model.ProfileInfoDto;
import es.myfamily.users.model.ProfileInfoUserStats;
import es.myfamily.users.model.UserToken;
import es.myfamily.users.model.UserUpdateRequest;
import es.myfamily.users.model.Users;
import es.myfamily.users.model.UsersDto;
import es.myfamily.users.model.CreatedUserResponse;
import es.myfamily.users.repository.UsersRepository;
import es.myfamily.users.service.UsersService;
import es.myfamily.utils.SecurityUtils;
import es.myfamily.utils.Validations;

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
  @Autowired
  private SecurityUtils securityUtils;
  @Autowired
  private Validations validations;
  @Autowired
  private FamilyMemberService fmService;
  @Autowired
  private FamilyMemberRepository fmRepo;
  @Autowired
  private CalendarEventsService calendarEventsService;
  @Autowired
  private ShoppingItemsService shoppingItemsService;

  @Override
  public CreatedUserResponse createUser(CreateUserInputDto userInputDto) {
    if (usersRepository.findByEmail(userInputDto.getEmail()).isPresent()) {
      throw new MyFamilyException(HttpStatus.BAD_REQUEST, "Email already in use");
    }

    Users user = userMapper.fromCreateUserInputDto(userInputDto);
    user.setPassword_hash(passwordEncoder.encode(user.getPassword_hash()));

    return userMapper.toCreatedUserResponse(usersRepository.save(user));
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

  @Override
  public ProfileInfoDto getProfileInfo(Long familyId) {

    // Get User Info from context and validate
    Users user = securityUtils.getUserFromContext();
    validations.familyExistsAndUserInFamily(familyId, user.getId());

    FamilyMember familyMember = fmService.getFamilyMember(familyId, user.getId());
    ProfileInfoUserStats userStats = buildUserStatsDto(user.getId());

    ProfileInfoDto profileInfoDto = buildProfileInfoDto(user, familyMember, userStats);
    return profileInfoDto;
  }

  @Override
  public void updatePassword(Long userId, PasswordUpdateRequest request) {
    Users user = securityUtils.getUserFromContext();
    if (user.getId() != userId) {
      throw new MyFamilyException(HttpStatus.FORBIDDEN, "You can only update your own password");
    }

    if (passwordEncoder.matches(request.getNewPassword(), user.getPassword_hash())) {
      throw new MyFamilyException(HttpStatus.BAD_REQUEST, "No se puede reutilizar la contraseña actual");
    }

    user.setPassword_hash(passwordEncoder.encode(request.getNewPassword()));
    usersRepository.save(user);
  }

  @Override
  public UsersDto updateUser(Long familyId, UserUpdateRequest request) {
    Users user = securityUtils.getUserFromContext();
    validations.familyExistsAndUserInFamily(familyId, user.getId());
    FamilyMember familyMember = fmService.getFamilyMember(familyId, user.getId());

    updateUserEmail(user, request.getEmail());
    updateUserUsername(user, request.getUsername());
    updateUserBirthdate(user, request.getBirthdate());
    updateFamilyMemberName(familyMember, request.getFamilyMemberName());

    Users updatedUser = usersRepository.save(user);
    return userMapper.toUsersDto(updatedUser);
  }

  // Private helper methods
  private void updateUserEmail(Users user, String newEmail) {
    if (canChangeEmail(user, newEmail)) {
      if (usersRepository.findByEmail(newEmail).isPresent()) {
        throw new MyFamilyException(HttpStatus.BAD_REQUEST, "Email already in use");
      }
      user.setEmail(newEmail);
    }
  }

  private void updateUserUsername(Users user, String newUsername) {
    if (canChangeUsername(user, newUsername)) {
      user.setUsername(newUsername);
    }
  }

  private void updateUserBirthdate(Users user, String birthdateStr) {
    if (birthdateStr != null) {
      try {
        Date birthdateToSet = new SimpleDateFormat("yyyy-MM-dd").parse(birthdateStr);
        validations.validateUserAge(birthdateToSet);
        if (!birthdateToSet.equals(user.getBirthdate())) {
          user.setBirthdate(birthdateToSet);
        }
      } catch (ParseException e) {
        throw new MyFamilyException(HttpStatus.BAD_REQUEST, "Invalid birthdate format");
      }
    }
  }

  private void updateFamilyMemberName(FamilyMember familyMember, String newFamilyMemberName) {
    if (newFamilyMemberName != null && !newFamilyMemberName.equals(familyMember.getFamilyMemberName())) {
      familyMember.setFamilyMemberName(newFamilyMemberName);
      fmRepo.save(familyMember);
    }
  }

  private ProfileInfoDto buildProfileInfoDto(Users user, FamilyMember familyMember, ProfileInfoUserStats userStats) {
    ProfileInfoDto profileInfo = new ProfileInfoDto();
    profileInfo.setUserId(user.getId());
    profileInfo.setUsername(user.getUsername());
    profileInfo.setBirthdate(user.getBirthdate());
    profileInfo.setFamilyMemberName(familyMember.getFamilyMemberName());
    profileInfo.setEmail(user.getEmail());
    profileInfo.setFamilyMemberIcon(familyMember.getSelectedIcon());
    profileInfo.setMemberSince(familyMember.getCreatedAt());
    profileInfo.setUserStats(userStats);
    return profileInfo;
  }

  private ProfileInfoUserStats buildUserStatsDto(Long userId) {
    ProfileInfoUserStats userStats = new ProfileInfoUserStats();
    userStats.setFamiliesCount(fmService.countFamiliesByUserId(userId));
    userStats.setTotalEventsCreated(calendarEventsService.countEventsCreatedByUserId(userId));
    userStats.setTotalShoppingItemsCreated(shoppingItemsService.countItemsCreatedByUserId(userId));
    userStats.setTotalPurchasedItems(shoppingItemsService.countItemsBoughtByUserId(userId));
    return userStats;
  }

  private boolean canChangeEmail(Users user, String newEmail) {
    return newEmail != null && !newEmail.equals(user.getEmail());
  }

  private boolean canChangeUsername(Users user, String newUsername) {
    return newUsername != null && !newUsername.equals(user.getUsername()) && !newUsername.isBlank();
  }
}
