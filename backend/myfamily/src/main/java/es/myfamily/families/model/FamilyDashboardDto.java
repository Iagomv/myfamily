package es.myfamily.families.model;

import java.util.List;

import es.myfamily.calendar_events.model.CalendarEventDto;
import es.myfamily.calendar_events.model.CalendarEventMonthlyStats;
import es.myfamily.family_member.model.FamilyMemberDto;
import es.myfamily.shopping.model.ShoppingItemDto;
import es.myfamily.shopping.model.ShoppingItemMontlyStatsDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FamilyDashboardDto {
  private Long id;
  private String familyName;
  private String invitationCode;
  private List<CalendarEventDto> calendarEvents;
  private List<ShoppingItemDto> shoppingItems;
  private List<FamilyMemberDto> familyMembers;
  private ShoppingItemMontlyStatsDto shoppingItemsMonthlyStats;
  private CalendarEventMonthlyStats calendarEventsMonthlyStats;
}
