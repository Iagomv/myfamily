package es.myfamily.calendar_events.model;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CalendarEventDto {
  private Long id;
  private String eventName;
  private String eventDescription;
  private CalendarEventCategoryEnum eventCategory;
  private Instant eventDate;
  private Instant createdAt;
}
