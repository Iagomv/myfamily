package es.myfamily.calendar_events.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.myfamily.calendar_events.model.CalendarEvent;

@Repository
public interface CalendarEventRepository extends JpaRepository<CalendarEvent, Long> {

  public List<CalendarEvent> findAllByFamilyIdAndIsDeletedFalse(Long familyId);
}
