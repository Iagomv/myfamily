package es.myfamily.calendar_events.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import es.myfamily.calendar_events.mapper.CalendarEventMapper;
import es.myfamily.calendar_events.model.CalendarEventDto;
import es.myfamily.calendar_events.model.CalendarEventInputDto;
import es.myfamily.calendar_events.repository.CalendarEventRepository;
import es.myfamily.calendar_events.service.CalendarEventsService;
import es.myfamily.exception.MyFamilyException;
import es.myfamily.families.model.Family;
import es.myfamily.families.repository.FamilyRepository;
import es.myfamily.calendar_events.model.CalendarEvent;

@Service
public class CalendarEventsServiceImpl implements CalendarEventsService {

  @Autowired
  CalendarEventRepository calendarEventRepo;

  @Autowired
  CalendarEventMapper calendarEventMapper;

  @Autowired
  private FamilyRepository familyRepository;

  @Override
  public List<CalendarEventDto> getCalendarEventsByFamilyId(Long familyId) {
    return calendarEventRepo.findAllByFamilyIdAndIsDeletedFalse(familyId).stream()
        .map(calendarEventMapper::toDto)
        .toList();
  }

  @Override
  public CalendarEventDto createCalendarEvent(Long familyId, CalendarEventInputDto dto) {
    Family family = familyRepository.findById(familyId)
        .orElseThrow(() -> new MyFamilyException(HttpStatus.NOT_FOUND, "Family not found"));

    CalendarEvent entity = calendarEventMapper.toEntity(dto);
    entity.setFamily(family);

    CalendarEvent saved = calendarEventRepo.save(entity);
    return calendarEventMapper.toDto(saved);
  }

  @Override
  public void deleteCalendarEvent(Long eventId) {
    CalendarEvent event = calendarEventRepo.findById(eventId)
        .orElseThrow(() -> new MyFamilyException(HttpStatus.NOT_FOUND, "Calendar event not found"));

    event.setIsDeleted(true);
    calendarEventRepo.save(event);
  }

}
