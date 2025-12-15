package es.myfamily.calendar_events.model;

import java.time.Instant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CalendarEventInputDto {

  @NotBlank(message = "Event name must not be blank")
  private String eventName;

  private String eventDescription;

  @NotNull(message = "Event category must be provided")
  private CalendarEventCategoryEnum eventCategory;

  private Instant eventDate;
}
