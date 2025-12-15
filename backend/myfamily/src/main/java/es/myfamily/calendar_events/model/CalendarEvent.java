package es.myfamily.calendar_events.model;

import java.time.Instant;

import es.myfamily.families.model.Family;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "calendar_events")
public class CalendarEvent {
  @Id
  @Column(name = "event_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "family_id", nullable = false)
  @JsonIgnoreProperties({ "familyMembers", "calendarEvents", "hibernateLazyInitializer", "handler" })
  private Family family;

  @Column(name = "event_name", nullable = false)
  @NotBlank
  private String eventName;

  @Column(name = "event_description")
  private String eventDescription;

  @Column(name = "event_category")
  @Enumerated(EnumType.STRING)
  private CalendarEventCategoryEnum eventCategory;

  @Column(name = "event_date")
  private Instant eventDate;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  @Column(name = "is_deleted", nullable = false)
  private Boolean isDeleted = false;

  @PrePersist
  protected void onCreate() {
    this.createdAt = Instant.now();
    this.isDeleted = this.isDeleted == null ? false : this.isDeleted;
  }
}
