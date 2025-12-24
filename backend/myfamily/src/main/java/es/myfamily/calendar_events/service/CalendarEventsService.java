package es.myfamily.calendar_events.service;

import java.util.List;

import es.myfamily.calendar_events.model.CalendarEventDto;
import es.myfamily.calendar_events.model.CalendarEventInputDto;
import es.myfamily.calendar_events.model.CalendarEventMonthlyStats;

public interface CalendarEventsService {

  List<CalendarEventDto> getCalendarEventsByFamilyId(Long familyId);

  CalendarEventDto createCalendarEvent(Long familyId, CalendarEventInputDto dto);

  CalendarEventDto updateCalendarEvent(Long eventId, CalendarEventInputDto dto);

  void deleteCalendarEvent(Long eventId);

  List<CalendarEventDto> getUpcoming3Events(Long familyId);

  CalendarEventMonthlyStats getMonthlyCalendarEventStats(Long familyId);

  Integer countEventsCreatedByUserId(Long userId);

}
