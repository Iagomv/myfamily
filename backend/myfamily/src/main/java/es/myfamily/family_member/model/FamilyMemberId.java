package es.myfamily.family_member.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FamilyMemberId implements Serializable {

  @Column(name = "family_id")
  private Long familyId;

  @Column(name = "user_id")
  private Long userId;
}
