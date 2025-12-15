package es.myfamily.calendar_events.service;

import java.util.List;

import es.myfamily.calendar_events.model.CalendarEventDto;
import es.myfamily.calendar_events.model.CalendarEventInputDto;

public interface CalendarEventsService {

  List<CalendarEventDto> getCalendarEventsByFamilyId(Long familyId);

  CalendarEventDto createCalendarEvent(Long familyId, CalendarEventInputDto dto);

  void deleteCalendarEvent(Long eventId);

}
