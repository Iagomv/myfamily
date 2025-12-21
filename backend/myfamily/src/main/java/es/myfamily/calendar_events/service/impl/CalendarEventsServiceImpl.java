package es.myfamily.calendar_events.service.impl;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import es.myfamily.calendar_events.mapper.CalendarEventMapper;
import es.myfamily.calendar_events.model.CalendarEventDto;
import es.myfamily.calendar_events.model.CalendarEventInputDto;
import es.myfamily.calendar_events.model.CalendarEventMonthlyStats;
import es.myfamily.calendar_events.repository.CalendarEventRepository;
import es.myfamily.calendar_events.service.CalendarEventsService;
import es.myfamily.exception.MyFamilyException;
import es.myfamily.families.model.Family;
import es.myfamily.families.repository.FamilyRepository;
import es.myfamily.users.model.Users;
import es.myfamily.utils.SecurityUtils;
import es.myfamily.utils.TimeUtils;
import es.myfamily.calendar_events.model.CalendarEvent;
import es.myfamily.calendar_events.model.CalendarEventCategoryEnum;

@Service
public class CalendarEventsServiceImpl implements CalendarEventsService {

  @Autowired
  CalendarEventRepository calendarEventRepo;

  @Autowired
  CalendarEventMapper calendarEventMapper;

  @Autowired
  private FamilyRepository familyRepository;

  @Autowired
  private SecurityUtils securityUtils;

  @Autowired
  private TimeUtils timeUtils;

  @Override
  public List<CalendarEventDto> getCalendarEventsByFamilyId(Long familyId) {
    return calendarEventRepo.findAllByFamilyIdAndIsDeletedFalseOrderByEventDateAsc(familyId).stream()
        .map(calendarEventMapper::toDto)
        .toList();
  }

  @Override
  public CalendarEventDto createCalendarEvent(Long familyId, CalendarEventInputDto dto) {
    Family family = familyRepository.findById(familyId)
        .orElseThrow(() -> new MyFamilyException(HttpStatus.NOT_FOUND, "Family not found"));
    Users user = securityUtils.getUserFromContext();

    CalendarEvent entity = calendarEventMapper.toEntity(dto);
    entity.setFamily(family);
    entity.setCreatedByUserId(user.getId());

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

  @Override
  public List<CalendarEventDto> getUpcoming3Events(Long familyId) {
    Instant now = LocalDate.now().atStartOfDay().toInstant(ZoneOffset.UTC); // Start of today
    return calendarEventRepo.findTop3UpcomingEventsFromToday(familyId, now).stream()
        .map(calendarEventMapper::toDto)
        .toList();
  }

  @Override
  public CalendarEventMonthlyStats getMonthlyCalendarEventStats(Long familyId) {
    Integer month = timeUtils.getCurrentMonth();
    Integer year = timeUtils.getCurrentYear();
    Integer totalMonthEvents = calendarEventRepo.countEventsByFamilyIdAndCurrentMonth(familyId, month, year);
    Integer pendingEvents = calendarEventRepo.countEventsByFamilyIdAndCurrentMonthAndFutureDates(familyId, month, year);
    Integer pastEvents = totalMonthEvents - pendingEvents;
    List<CalendarEventCategoryEnum> mostFrequentCategories = calendarEventRepo.findMostFrequentEventCategoriesCurrentMonth(familyId, month, year);
    CalendarEventCategoryEnum mostFrequentCategory = mostFrequentCategories.isEmpty() ? null : mostFrequentCategories.get(0);
    return new CalendarEventMonthlyStats(totalMonthEvents, pastEvents, pendingEvents, mostFrequentCategory);
  }

  @Override
  public Integer countEventsCreatedByUserId(Long userId) {
    return calendarEventRepo.countByCreatedByUserId(userId);
  }
}