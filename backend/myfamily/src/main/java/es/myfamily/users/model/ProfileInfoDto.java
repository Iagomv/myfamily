package es.myfamily.users.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileInfoDto {
  private Long userId;
  private String username;
  private Date birthdate;
  private String familyMemberName;
  private String email;
  private String familyMemberIcon;
  private Date memberSince;
  private ProfileInfoUserStats userStats;
}
