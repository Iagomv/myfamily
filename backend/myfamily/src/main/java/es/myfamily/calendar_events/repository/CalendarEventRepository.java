package es.myfamily.calendar_events.repository;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.myfamily.calendar_events.model.CalendarEvent;
import es.myfamily.calendar_events.model.CalendarEventCategoryEnum;

@Repository
public interface CalendarEventRepository extends JpaRepository<CalendarEvent, Long> {

        public List<CalendarEvent> findAllByFamilyIdAndIsDeletedFalseOrderByEventDateAsc(Long familyId);

        // Return the next three upcoming (future) non-deleted events for a family
        public List<CalendarEvent> findTop3ByFamilyIdAndIsDeletedFalseAndEventDateAfterOrderByEventDateAsc(Long familyId, Instant date);

        // Return the next three upcoming (from today onwards) non-deleted events for a
        // family
        @Query("SELECT ce FROM CalendarEvent ce WHERE ce.family.id = :familyId AND ce.isDeleted = false AND ce.eventDate >= :date ORDER BY ce.eventDate ASC LIMIT 3")
        List<CalendarEvent> findTop3UpcomingEventsFromToday(@Param("familyId") Long familyId, @Param("date") Instant date);

        @Query("SELECT COUNT(ce) FROM CalendarEvent ce WHERE ce.family.id = :familyId AND EXTRACT(MONTH FROM ce.eventDate) = :month AND EXTRACT(YEAR FROM ce.eventDate) = :year")
        Integer countEventsByFamilyIdAndCurrentMonth(@Param("familyId") Long familyId, @Param("month") Integer month, @Param("year") Integer year);

        @Query("SELECT COUNT(ce) FROM CalendarEvent ce WHERE ce.family.id = :familyId AND EXTRACT(MONTH FROM ce.eventDate) = :month AND EXTRACT(YEAR FROM ce.eventDate) = :year AND ce.eventDate > CURRENT_TIMESTAMP")
        Integer countEventsByFamilyIdAndCurrentMonthAndFutureDates(@Param("familyId") Long familyId, @Param("month") Integer month,
                        @Param("year") Integer year);

        @Query("SELECT ce.eventCategory FROM CalendarEvent ce WHERE ce.family.id = :familyId AND EXTRACT(MONTH FROM ce.eventDate) = :month AND EXTRACT(YEAR FROM ce.eventDate) = :year GROUP BY ce.eventCategory ORDER BY COUNT(ce) DESC")
        List<CalendarEventCategoryEnum> findMostFrequentEventCategoriesCurrentMonth(@Param("familyId") Long familyId, @Param("month") Integer month,
                        @Param("year") Integer year);

        Integer countByCreatedByUserId(Long userId);

}
