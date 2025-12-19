package es.myfamily.users.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import org.springframework.security.crypto.password.PasswordEncoder;

import es.myfamily.calendar_events.model.CalendarEvent;
import es.myfamily.calendar_events.service.CalendarEventsService;
import es.myfamily.config.JwtTokenUtil;
import es.myfamily.exception.MyFamilyException;
import es.myfamily.family_member.service.FamilyMemberService;
import es.myfamily.shopping.service.ShoppingItemsService;
import es.myfamily.users.mapper.UserMapper;
import es.myfamily.users.model.CreateUserInputDto;
import es.myfamily.users.model.LoginInputDto;
import es.myfamily.users.model.ProfileInfoDto;
import es.myfamily.users.model.UserStatsDto;
import es.myfamily.users.model.UserToken;
import es.myfamily.users.model.Users;
import es.myfamily.users.model.UsersDto;
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
  private CalendarEventsService calendarEventsService;
  @Autowired
  private ShoppingItemsService shoppingItemsService;

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

  @Override
  public ProfileInfoDto getProfileInfo(Long familyId) {

    // Get User Info from context and validate
    Users user = securityUtils.getUserFromContext();
    validations.familyExistsAndUserInFamily(familyId, user.getId());

    String selectedIcon = fmService.getFamilyMemberIcon(familyId, user.getId());
    UserStatsDto userStats = buildUserStatsDto(user.getId());

    ProfileInfoDto profileInfoDto = buildProfileInfoDto(user, selectedIcon, userStats);
    return profileInfoDto;
  }

  private ProfileInfoDto buildProfileInfoDto(Users user, String selectedIcon, UserStatsDto userStats) {
    ProfileInfoDto profileInfo = new ProfileInfoDto();
    profileInfo.setUserId(user.getId());
    profileInfo.setUsername(user.getUsername());
    profileInfo.setEmail(user.getEmail());
    profileInfo.setFamilyMemberIcon(selectedIcon);
    profileInfo.setUserStats(userStats);
    return profileInfo;
  }

  private UserStatsDto buildUserStatsDto(Long userId) {
    UserStatsDto userStats = new UserStatsDto();
    userStats.setFamiliesCount(fmService.countFamiliesByUserId(userId));
    userStats.setTotalEventsCreated(calendarEventsService.countEventsCreatedByUserId(userId));
    userStats.setTotalShoppingItemsCreated(shoppingItemsService.countItemsCreatedByUserId(userId));
    userStats.setTotalPurchasedItems(shoppingItemsService.countItemsBoughtByUserId(userId));
    return userStats;
  }
}
