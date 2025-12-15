package es.myfamily.families.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import es.myfamily.calendar_events.model.CalendarEvent;
import es.myfamily.family_member.model.FamilyMemberDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FamilyDto {
  private Long id;
  private String familyName;
  private String invitationCode;
  private List<CalendarEvent> calendarEvents;
  @JsonFormat(shape = com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private Date createdAt;

  private List<FamilyMemberDto> familyMembers;
}
