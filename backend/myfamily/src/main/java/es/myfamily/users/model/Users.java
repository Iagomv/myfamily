package es.myfamily.users.model;

import java.util.Date;
import java.util.List;

import es.myfamily.family_member.model.FamilyMember;
import jakarta.persistence.Column;
import lombok.Data;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
@Data
public class Users {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  private Long id;

  @Column(name = "email", nullable = false, unique = true)
  private String email;

  @Column(name = "username", nullable = false)
  private String username;

  @Column(name = "password_hash", nullable = false)
  private String password_hash;

  @Column(name = "created_at", nullable = false)
  private Date created_at;

  @OneToMany(mappedBy = "user")
  private List<FamilyMember> familyMembers;

  @PrePersist
  protected void onCreate() {
    created_at = new Date();
  }
}
