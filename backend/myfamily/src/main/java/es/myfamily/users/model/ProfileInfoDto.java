package es.myfamily.users.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileInfoDto {
  private Long userId;
  private String username;
  private String email;
  private String familyMemberIcon;
  private UserStatsDto userStats;
}
