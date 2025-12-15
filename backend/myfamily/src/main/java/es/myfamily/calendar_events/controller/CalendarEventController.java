package es.myfamily.calendar_events.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import es.myfamily.calendar_events.model.CalendarEventDto;
import es.myfamily.calendar_events.model.CalendarEventInputDto;
import es.myfamily.calendar_events.service.CalendarEventsService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/calendar-events")
public class CalendarEventController {

  @Autowired
  CalendarEventsService calendarEventsService;

  @GetMapping("/{familyId}")
  public ResponseEntity<List<CalendarEventDto>> getFamilyEvents(@PathVariable Long familyId) {
    return ResponseEntity.ok(calendarEventsService.getCalendarEventsByFamilyId(familyId));
  }

  @PostMapping("/{familyId}")
  public ResponseEntity<CalendarEventDto> createEvent(@PathVariable Long familyId,
      @Valid @RequestBody CalendarEventInputDto dto) {
    CalendarEventDto created = calendarEventsService.createCalendarEvent(familyId, dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }

  @DeleteMapping("/{eventId}")
  public ResponseEntity<Void> deleteEvent(@PathVariable Long eventId) {
    calendarEventsService.deleteCalendarEvent(eventId);
    return ResponseEntity.noContent().build();
  }

}
