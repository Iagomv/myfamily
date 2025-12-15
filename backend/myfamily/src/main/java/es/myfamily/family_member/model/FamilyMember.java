package es.myfamily.family_member.model;

import java.util.Date;

import es.myfamily.families.model.Family;
import es.myfamily.users.model.Users;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "family_members")
public class FamilyMember {

  @EmbeddedId
  private FamilyMemberId id;

  @ManyToOne
  @MapsId("familyId")
  @JoinColumn(name = "family_id", nullable = false)
  private Family family;

  @ManyToOne
  @MapsId("userId")
  @JoinColumn(name = "user_id", nullable = false)
  private Users user;

  @Column(name = "created_at", nullable = false)
  private Date createdAt;

  @Column(name = "family_member_name", nullable = false)
  private String familyMemberName;

  @PrePersist
  protected void onCreate() {
    this.createdAt = new Date();
  }
}
