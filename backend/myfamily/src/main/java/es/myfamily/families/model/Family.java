package es.myfamily.families.model;

import java.util.Date;
import java.util.List;

import es.myfamily.calendar_events.model.CalendarEvent;
import es.myfamily.family_member.model.FamilyMember;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "families")
public class Family {

  @Id
  @Column(name = "family_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "family_name", nullable = false)
  private String familyName;

  @Column(name = "invitation_code", nullable = false, unique = true)
  private String invitationCode;

  @Column(name = "created_at", nullable = false)
  private Date createdAt;

  @OneToMany(mappedBy = "family", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<FamilyMember> familyMembers;

  @OneToMany(mappedBy = "family", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<CalendarEvent> calendarEvents;

  @PrePersist
  protected void onCreate() {
    this.createdAt = new Date();
  }
}
