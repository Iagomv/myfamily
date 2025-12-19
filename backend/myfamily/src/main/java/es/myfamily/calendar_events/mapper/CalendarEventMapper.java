package es.myfamily.calendar_events.mapper;

import org.springframework.stereotype.Component;

import es.myfamily.calendar_events.model.CalendarEvent;
import es.myfamily.calendar_events.model.CalendarEventDto;
import es.myfamily.calendar_events.model.CalendarEventInputDto;

@Component
public class CalendarEventMapper {

  public CalendarEventDto toDto(CalendarEvent entity) {
    if (entity == null) {
      return null;
    }
    return CalendarEventDto.builder()
        .id(entity.getId())
        .eventName(entity.getEventName())
        .eventDate(entity.getEventDate())
        .eventDescription(entity.getEventDescription())
        .eventCategory(entity.getEventCategory())
        .createdAt(entity.getCreatedAt())
        .createdByUserId(entity.getCreatedByUserId())
        .build();

  }

  public CalendarEvent toEntity(CalendarEventInputDto dto) {
    if (dto == null) {
      return null;
    }
    CalendarEvent e = new CalendarEvent();
    e.setEventName(dto.getEventName());
    e.setEventDate(dto.getEventDate());
    e.setEventDescription(dto.getEventDescription());
    e.setEventCategory(dto.getEventCategory());
    return e;
  }
}
