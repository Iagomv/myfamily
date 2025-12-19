package es.myfamily.calendar_events.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalendarEventMonthlyStats {
  private Integer totalEvents;
  private Integer pastEvents;
  private Integer upcomingEvents;
  private CalendarEventCategoryEnum mostFrequentCategory;
}
